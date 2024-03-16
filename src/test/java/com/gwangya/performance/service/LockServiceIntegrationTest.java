package com.gwangya.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.gwangya.global.testConfig.HazelcastTestConfig;
import com.gwangya.global.testConfig.JpaTestConfig;
import com.gwangya.lock.HazelcastLockService;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.performance.repository.impl.SeatRepositoryImpl;
import com.gwangya.purchase.domain.LockInfo;
import com.gwangya.purchase.dto.OccupySeatDto;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

@SpringBootTest(classes = {HazelcastTestConfig.class, JpaTestConfig.class, HazelcastLockService.class,
	SeatRepositoryImpl.class, SeatService.class})
public class LockServiceIntegrationTest {

	private static final Random random = new Random();

	@Qualifier("hazelcastInstance")
	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Autowired
	private HazelcastLockService hazelcastLockService;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private SeatService seatService;

	IMap<Long, LockInfo> selectedSeats;

	@BeforeEach
	void setUp() {
		selectedSeats = hazelcastLockService.getLockMap("seatSession");
	}

	@AfterEach
	void tearDown() {
		selectedSeats.clear();
	}

	@Nested
	@DisplayName("좌석 선택 테스트")
	class SeatService_occupySeats {
		@DisplayName("동일한 좌석을 순차적으로 선택했을 때 늦게 들어온 요청은 좌석을 점유할 수 없다.")
		@Test
		void if_sequential_requests_are_received_for_the_same_seat_late_request_cannot_occupy_the_seat() throws
			InterruptedException {
			// given
			ExecutorService service = Executors.newFixedThreadPool(2);
			CountDownLatch latch = new CountDownLatch(2);
			long seatId = 1L;
			OccupySeatInfo request = new OccupySeatInfo(1, 1, List.of(seatId));
			OccupySeatInfo anotherRequest = new OccupySeatInfo(1, 2, List.of(seatId));

			// when & then
			service.submit(() -> {
				OccupySeatDto result = seatService.occupySeats(request);
				assertThat(result.getSeatIds()).contains(seatId);
				latch.countDown();
			});
			service.submit(() -> {
				assertThatThrownBy(() -> seatService.occupySeats(anotherRequest))
					.isInstanceOf(UnavailablePurchaseException.class)
					.hasMessageContaining("이미 선택된 좌석입니다.");
				latch.countDown();
			});
			latch.await();
		}

		@DisplayName("다른 요청에서 점유한 좌석일 경우 예외가 발생한다.")
		@Test
		void if_the_seat_is_occupied_by_another_request_then_exception_is_thrown() {
			// given
			long occupiedSeatId = 1L;
			long anotherUserId = 2L;
			selectedSeats.put(occupiedSeatId, new LockInfo(anotherUserId, 99));
			OccupySeatInfo request = new OccupySeatInfo(1, 1, List.of(occupiedSeatId));

			// when & then
			assertThatThrownBy(() -> seatService.occupySeats(request))
				.isInstanceOf(UnavailablePurchaseException.class)
				.hasMessageContaining("이미 선택된 좌석입니다.");
		}

		@DisplayName("점유되지 않은 좌석은 점유할 수 있다.")
		@Test
		void unoccupied_seat_can_be_occupied() {
			// given
			long unoccupiedSeatId = 2L;
			long anotherUserId = 2L;
			selectedSeats.put(1L, new LockInfo(anotherUserId, 99));
			OccupySeatInfo request = new OccupySeatInfo(1, 1, List.of(unoccupiedSeatId));

			// when
			OccupySeatDto result = seatService.occupySeats(request);

			// then
			assertThat(result.getSeatIds()).contains(unoccupiedSeatId);
		}
	}

	@Nested
	@DisplayName("[좌석 선택] 요청 수 별 테스트")
	class SeatService_occupySeats_byNumberOfRequests {
		@DisplayName("요청 수가 100일 때 무결성이 보장된다.")
		@Test
		void integrity_is_guaranteed_when_the_number_of_requests_is_100() throws InterruptedException {
			// given
			int numberOfThreads = 100;
			ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
			CountDownLatch latch = new CountDownLatch(numberOfThreads);
			Map<Long, OccupySeatInfo> requests = new HashMap();
			for (int i = 1; i < numberOfThreads + 1; i++) {
				OccupySeatInfo request = createOccupySeatInfo(i, 3, 20);
				requests.put((long)i, request);
			}
			Map<Long, Long> results = new ConcurrentHashMap<>();
			Map<Long, Long> copyOfSelectedSeats = new ConcurrentHashMap<>();

			// when
			for (int i = 1; i < numberOfThreads + 1; i++) {
				long userId = i;
				service.submit(() -> {
					try {
						OccupySeatDto result = seatService.occupySeats(requests.get(userId));
						if (!Objects.isNull(result)) {
							result.getSeatIds().stream()
								.forEach(seatId -> results.put(seatId, userId));
						}
					} finally {
						latch.countDown();
					}
				});
			}
			latch.await();

			// then
			selectedSeats.entrySet().stream()
				.forEach(entry -> copyOfSelectedSeats.put(entry.getKey(), entry.getValue().getUserId()));

			assertThat(copyOfSelectedSeats).allSatisfy((seatId, userId) -> userId.equals(results.get(seatId)));
		}

		@DisplayName("요청 수가 10000일 때 무결성이 보장된다.")
		@Test
		void integrity_is_guaranteed_when_the_number_of_requests_is_10000() throws InterruptedException {
			int numberOfThreads = 10000;
			ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
			CountDownLatch latch = new CountDownLatch(numberOfThreads);
			Map<Long, OccupySeatInfo> requests = new HashMap();
			for (int i = 1; i < numberOfThreads + 1; i++) {
				OccupySeatInfo request = createOccupySeatInfo(i, 3, 1200);
				requests.put((long)i, request);
			}
			Map<Long, Long> results = new ConcurrentHashMap<>();
			Map<Long, Long> copyOfSelectedSeats = new ConcurrentHashMap<>();

			// when
			for (int i = 1; i < numberOfThreads + 1; i++) {
				long userId = i;
				service.submit(() -> {
					try {
						OccupySeatDto result = seatService.occupySeats(requests.get(userId));
						if (!Objects.isNull(result)) {
							result.getSeatIds().stream()
								.forEach(seatId -> results.put(seatId, userId));
						}
					} finally {
						latch.countDown();
					}
				});
			}
			latch.await();

			// then
			selectedSeats.entrySet().stream()
				.forEach(entry -> copyOfSelectedSeats.put(entry.getKey(), entry.getValue().getUserId()));

			assertThat(copyOfSelectedSeats).allSatisfy((seatId, userId) -> userId.equals(results.get(seatId)));
		}

		@DisplayName("요청 수가 100000일 때 무결성이 보장된다.")
		@Test
		void integrity_is_guaranteed_when_the_number_of_requests_is_100000() throws InterruptedException {
			int numberOfThreads = 100000;
			ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
			CountDownLatch latch = new CountDownLatch(numberOfThreads);
			Map<Long, OccupySeatInfo> requests = new HashMap();
			for (int i = 1; i < numberOfThreads + 1; i++) {
				OccupySeatInfo request = createOccupySeatInfo(i, 3, 12000);
				requests.put((long)i, request);
			}
			Map<Long, Long> results = new ConcurrentHashMap<>();
			Map<Long, Long> copyOfSelectedSeats = new ConcurrentHashMap<>();

			// when
			for (int i = 1; i < numberOfThreads + 1; i++) {
				long userId = i;
				service.submit(() -> {
					try {
						OccupySeatDto result = seatService.occupySeats(requests.get(userId));
						if (!Objects.isNull(result)) {
							result.getSeatIds().stream()
								.forEach(seatId -> results.put(seatId, userId));
						}
					} finally {
						latch.countDown();
					}
				});
			}
			latch.await();

			// then
			selectedSeats.entrySet().stream()
				.forEach(entry -> copyOfSelectedSeats.put(entry.getKey(), entry.getValue().getUserId()));

			assertThat(copyOfSelectedSeats).allSatisfy((seatId, userId) -> userId.equals(results.get(seatId)));
		}
	}

	private OccupySeatInfo createOccupySeatInfo(long userId, int limit, long totalSeatCount) {
		long performanceDetailId = random.nextLong(3) + 1;
		List<Long> seatIds = new ArrayList<>();
		for (int i = 0; i < limit; i++) {
			long seatId = random.nextLong(totalSeatCount) + 1;
			if (!seatIds.contains(seatId)) {
				seatIds.add(seatId);
			}
		}
		return new OccupySeatInfo(performanceDetailId, userId, seatIds);
	}
}
