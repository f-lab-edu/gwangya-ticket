package com.gwangya.performance.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gwangya.performance.repository.InMemorySeatRepository;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.dto.SelectSeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.test.TestHazelcastInstanceFactory;

public class LockServiceTest {

	SeatRepository seatRepository;

	HazelcastInstance hazelcastInstance;

	SeatService seatService;

	IMap<String, Long> selectedSeats;

	@BeforeEach
	void setUp() {
		TestHazelcastInstanceFactory factory = new TestHazelcastInstanceFactory(2);
		hazelcastInstance = factory.newHazelcastInstance();
		selectedSeats = hazelcastInstance.getMap("seatSession");
		seatRepository = new InMemorySeatRepository();
		seatService = new SeatService(seatRepository, hazelcastInstance);
	}

	@DisplayName("n개의 좌석을 점유할 수 있다.")
	@Test
	void can_select_n_seats() {
		// given
		long performanceDetailId = 1L;
		long userId = 1L;
		List<Long> seatIds = List.of(1L, 2L, 3L);

		// when
		seatService.selectSeat(new SelectSeatInfo(performanceDetailId, userId, seatIds));

		// then
		IMap<String, Long> selectedSeats = hazelcastInstance.getMap(String.valueOf(performanceDetailId));
		for (int i = 0; i < 3; i++) {
			String key = String.valueOf(seatIds.get(i));
			assertThat(selectedSeats.containsKey(key)).isTrue();
			assertThat(selectedSeats.get(key)).isEqualTo(userId);
		}

	}

	@DisplayName("선택한 좌석의 일부만 점유할 수 없다.")
	@Test
	void cant_select_only_a_part_of_the_selected_seats() throws InterruptedException {
		// given
		long performanceDetailId = 1L;
		SelectSeatInfo firstUser = new SelectSeatInfo(performanceDetailId, 1L, List.of(1L, 2L, 3L));
		SelectSeatInfo secondUser = new SelectSeatInfo(performanceDetailId, 2L, List.of(2L, 3L, 4L));
		int numberOfThreads = 2;
		ExecutorService service = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(numberOfThreads);
		List<SelectSeatInfo> infos = List.of(firstUser, secondUser);

		// when
		for (int i = 0; i < numberOfThreads; i++) {
			int finalI = i;
			service.submit(() -> {
				try {
					seatService.selectSeat(infos.get(finalI));
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		assertThat(selectedSeats.values().size()).isEqualTo(3);
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

}
