package com.gwangya.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gwangya.lock.HazelcastLockService;
import com.gwangya.lock.LockService;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.InMemorySeatRepository;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.domain.LockInfo;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;

class LockServiceTest {

	SeatRepository seatRepository;

	HazelcastInstance hazelcastInstance;

	LockService lockService;

	SeatService seatService;

	IMap<Long, LockInfo> selectedSeats;

	@BeforeEach
	void setUp() {
		TestHazelcastInstanceFactory factory = new TestHazelcastInstanceFactory(2);
		hazelcastInstance = factory.newHazelcastInstance();
		selectedSeats = hazelcastInstance.getMap("seatSession");
		seatRepository = new InMemorySeatRepository();
		lockService = new HazelcastLockService(hazelcastInstance);
		seatService = new SeatService(seatRepository, lockService);
	}

	@DisplayName("n개의 좌석을 점유할 수 있다.")
	@Test
	void can_select_n_seats() {
		// given
		long performanceDetailId = 1L;
		long userId = 1L;
		List<Long> seatIds = List.of(1L, 2L, 3L);

		// when
		seatService.occupySeats(new OccupySeatInfo(performanceDetailId, userId, seatIds));

		// then
		for (int i = 0; i < 3; i++) {
			long key = seatIds.get(i);
			assertThat(selectedSeats.containsKey(key)).isTrue();
			assertThat(selectedSeats.get(key).getUserId()).isEqualTo(userId);
		}

	}

	@DisplayName("선택한 좌석의 일부만 점유할 수 없다.")
	@Test
	void cant_select_only_a_part_of_the_selected_seats() throws InterruptedException {
		// given
		long performanceDetailId = 1L;
		OccupySeatInfo firstUser = new OccupySeatInfo(performanceDetailId, 1L, List.of(1L, 2L, 3L));
		OccupySeatInfo secondUser = new OccupySeatInfo(performanceDetailId, 2L, List.of(2L, 3L, 4L));
		int numberOfThreads = 2;
		ExecutorService service = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		List<OccupySeatInfo> infos = List.of(firstUser, secondUser);

		// when
		for (int i = 0; i < numberOfThreads; i++) {
			int finalI = i;
			service.submit(() -> {
				try {
					seatService.occupySeats(infos.get(finalI));
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		assertThat(selectedSeats.values()).hasSize(3);
		if (selectedSeats.containsKey(1L)) {
			assertThat(selectedSeats.get(1L).getUserId()).isEqualTo(firstUser.getUserId());
			assertThat(selectedSeats.get(2L).getUserId()).isEqualTo(firstUser.getUserId());
			assertThat(selectedSeats.get(3L).getUserId()).isEqualTo(firstUser.getUserId());
		}
		if (selectedSeats.containsKey(4L)) {
			assertThat(selectedSeats.get(2L).getUserId()).isEqualTo(secondUser.getUserId());
			assertThat(selectedSeats.get(3L).getUserId()).isEqualTo(secondUser.getUserId());
			assertThat(selectedSeats.get(4L).getUserId()).isEqualTo(secondUser.getUserId());
		}
	}

	@DisplayName("중복 요청은 한 번만 반영된다.")
	@Test
	void duplicate_requests_are_reflected_only_once() throws InterruptedException {
		// given
		long performanceDetailId = 1L;
		OccupySeatInfo request = new OccupySeatInfo(performanceDetailId, 1L, List.of(1L, 2L, 3L));
		OccupySeatInfo duplicateRequest = new OccupySeatInfo(performanceDetailId, 1L, List.of(1L, 2L, 3L));
		int numberOfThreads = 2;
		ExecutorService service = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		List<OccupySeatInfo> infos = List.of(request, duplicateRequest);

		// when & then
		for (int i = 0; i < numberOfThreads; i++) {
			int finalI = i;
			service.submit(() -> {
				try {
					if (finalI == 1) {
						assertThatThrownBy(() -> seatService.occupySeats(infos.get(finalI)))
							.isInstanceOf(UnavailablePurchaseException.class)
							.hasMessageContaining("이미 선택된 좌석입니다.");

					} else {
						seatService.occupySeats(infos.get(finalI));
						assertThat(selectedSeats.keySet()).hasSize(3);
					}
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

	}

}
