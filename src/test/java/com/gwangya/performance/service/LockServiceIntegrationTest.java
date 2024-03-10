package com.gwangya.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
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

	@DisplayName("[요청 수 100] 좌석의 일부만 점유할 수 없다.")
	@Test
	void cant_select_only_a_part_of_the_selected_seats() throws InterruptedException {
		// given
		int numberOfThreads = 100;
		ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		Map<Long, OccupySeatInfo> requests = new HashMap();
		for (int i = 1; i < numberOfThreads + 1; i++) {
			OccupySeatInfo request = createOccupySeatInfo(i, 3, 1200);
			requests.put((long)i, request);
		}

		// when
		for (int i = 1; i < numberOfThreads + 1; i++) {
			long userId = i;
			service.submit(() -> {
				try {
					seatService.occupySeats(requests.get(userId));
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		for (int i = 1; i < numberOfThreads + 1; i++) {
			long userId = i;
			OccupySeatInfo request = requests.get(userId);
			if (hasAnySeat(request.getSeatIds(), userId)) {
				assertThat(request.getSeatIds().stream())
					.allMatch(seatId -> selectedSeats.containsKey(seatId)
						&& selectedSeats.get(seatId).equals(userId)
					);
			}
		}
	}

	@DisplayName("[요청 수 10000] 좌석의 일부만 점유할 수 없다.")
	@Test
	void cant_select_only_a_part_of_the_selected_seats_request_count_10000() throws InterruptedException {
		int numberOfThreads = 10000;
		ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		Map<Long, OccupySeatInfo> requests = new HashMap();
		for (int i = 1; i < numberOfThreads + 1; i++) {
			OccupySeatInfo request = createOccupySeatInfo(i, 3, 1200);
			requests.put((long)i, request);
		}

		// when
		for (int i = 1; i < numberOfThreads + 1; i++) {
			long userId = i;
			service.submit(() -> {
				try {
					seatService.occupySeats(requests.get(userId));
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		for (int i = 1; i < numberOfThreads + 1; i++) {
			long userId = i;
			OccupySeatInfo request = requests.get(userId);
			if (!Objects.isNull(request) && hasAnySeat(request.getSeatIds(), userId)) {
				assertThat(request.getSeatIds().stream())
					.allMatch(seatId -> selectedSeats.containsKey(seatId)
						&& selectedSeats.get(seatId).equals(userId)
					);
			}
		}
	}

	@DisplayName("이미 lock이 걸린 경우 예외가 발생한다.")
	@Test
	void if_seat_of_request_is_already_locked_then_exception_is_thrown() {
		// given
		long lockedSeatId = 1L;
		OccupySeatInfo request = new OccupySeatInfo(1, 1, List.of(lockedSeatId));
		FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(String.valueOf(lockedSeatId));
		lock.lock();

		// when & then
		assertThatThrownBy(() -> seatService.occupySeats(request))
			.isInstanceOf(UnavailablePurchaseException.class)
			.hasMessageContaining("이미 선택된 좌석입니다.");
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

	private boolean hasAnySeat(List<Long> seatIds, Long userId) {
		return seatIds.stream()
			.anyMatch(seatId -> selectedSeats.containsKey(seatId)
				&& selectedSeats.get(seatId).equals(userId)
			);
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
