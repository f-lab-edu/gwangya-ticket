package com.gwangya.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;

class SecondSeatServiceTest {

	SecondSeatService seatService;

	HazelcastInstance hazelcastInstance;

	IMap<Long, Long> selectedSeats;

	@BeforeEach
	void setUp() {
		TestHazelcastInstanceFactory factory = new TestHazelcastInstanceFactory(2);
		hazelcastInstance = factory.newHazelcastInstance();
		selectedSeats = hazelcastInstance.getMap("seatSession");
		seatService = new SecondSeatService(hazelcastInstance);
	}

	@DisplayName("n개의 좌석을 점유할 수 있다.")
	@Test
	void can_select_n_seats() {
		// given
		long performanceDetailId = 1L;
		long userId = 1L;
		List<Long> seatIds = List.of(1L, 2L, 3L);

		// when
		seatService.occupySeats_singleLock(new OccupySeatInfo(performanceDetailId, userId, seatIds), 1,
			TimeUnit.SECONDS);

		// then
		for (int i = 0; i < 3; i++) {
			long key = seatIds.get(i);
			assertThat(selectedSeats.containsKey(key)).isTrue();
			assertThat(selectedSeats.get(key)).isEqualTo(userId);
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
					seatService.occupySeats_singleLock(infos.get(finalI), 1, TimeUnit.SECONDS);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		assertThat(selectedSeats.values()).hasSize(3);
		if (selectedSeats.containsKey(1L)) {
			assertThat(selectedSeats.get(1L)).isEqualTo(firstUser.getUserId());
			assertThat(selectedSeats.get(2L)).isEqualTo(firstUser.getUserId());
			assertThat(selectedSeats.get(3L)).isEqualTo(firstUser.getUserId());
		}
		if (selectedSeats.containsKey(4L)) {
			assertThat(selectedSeats.get(2L)).isEqualTo(secondUser.getUserId());
			assertThat(selectedSeats.get(3L)).isEqualTo(secondUser.getUserId());
			assertThat(selectedSeats.get(4L)).isEqualTo(secondUser.getUserId());
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
						assertThatThrownBy(
							() -> seatService.occupySeats_singleLock(infos.get(finalI), 1, TimeUnit.SECONDS))
							.isInstanceOf(UnavailablePurchaseException.class)
							.hasMessageContaining("이미 선택된 좌석입니다.");

					} else {
						seatService.occupySeats_singleLock(infos.get(finalI), 1, TimeUnit.SECONDS);
						assertThat(selectedSeats.keySet()).hasSize(3);
					}
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

	}

	@DisplayName("[서로 다른 좌석을 선택한 경우] Lock 획득 시도 시간이 너무 짧으면 실패한다.")
	@Test
	void if_the_time_to_wait_for_the_lock_is_too_short_the_request_will_fail() throws InterruptedException {
		// given
		long performanceDetailId = 1L;
		OccupySeatInfo request = new OccupySeatInfo(performanceDetailId, 1L, List.of(1L, 2L));
		OccupySeatInfo secondRequest = new OccupySeatInfo(performanceDetailId, 2L, List.of(3L, 4L));
		int numberOfThreads = 2;
		ExecutorService service = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		List<OccupySeatInfo> infos = List.of(request, secondRequest);

		// when & then
		for (int i = 0; i < numberOfThreads; i++) {
			int finalI = i;
			service.submit(() -> {
				try {
					if (finalI == 1) {
						assertThatThrownBy(
							() -> seatService.occupySeats_singleLock(infos.get(finalI), 10, TimeUnit.MICROSECONDS))
							.isInstanceOf(UnavailablePurchaseException.class)
							.hasMessageContaining("이미 선택된 좌석입니다.");

					} else {
						seatService.occupySeats_singleLock(infos.get(finalI), 10, TimeUnit.MICROSECONDS);
					}
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		assertThat(selectedSeats.keySet()).hasSize(2);
		assertThat(selectedSeats.keySet()).containsAll(request.getSeatIds());
	}
}