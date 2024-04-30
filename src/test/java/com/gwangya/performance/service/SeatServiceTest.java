package com.gwangya.performance.service;

import static com.gwangya.performance.domain.PerformanceDetailFixture.*;
import static com.gwangya.performance.domain.PerformanceFixture.*;
import static com.gwangya.performance.domain.SeatFixture.*;
import static com.gwangya.user.domain.UserFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gwangya.lock.HazelcastLockService;
import com.gwangya.performance.domain.Performance;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.OccupancyInfoCondition;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.dto.SeatOccupancyInfo;
import com.gwangya.performance.repository.InMemoryHazelcastInstance;
import com.gwangya.performance.repository.InMemoryPerformanceRepository;
import com.gwangya.performance.repository.InMemorySeatRepository;
import com.gwangya.purchase.domain.LockInfo;
import com.gwangya.user.domain.User;
import com.gwangya.user.repository.InMemoryUserRepository;
import com.hazelcast.map.IMap;

class SeatServiceTest {

	InMemoryPerformanceRepository performanceRepository;

	InMemorySeatRepository seatRepository;

	InMemoryUserRepository userRepository;

	InMemoryHazelcastInstance hazelcastInstance;

	SeatService seatService;

	Performance performance;

	PerformanceDetail performanceDetail;

	Seat seat;

	User user;

	IMap<Long, LockInfo> occupiedSeats;

	@BeforeEach
	void setUp() {
		performanceRepository = new InMemoryPerformanceRepository();
		seatRepository = new InMemorySeatRepository();
		userRepository = new InMemoryUserRepository();
		hazelcastInstance = new InMemoryHazelcastInstance();
		seatService = new SeatService(seatRepository, new HazelcastLockService(hazelcastInstance));

		performance = performanceRepository.save(createPerformance(1L));
		performanceDetail = performanceRepository.save(
			createPerformanceDetail(1L, performance, LocalDateTime.now().minusDays(1),
				LocalDateTime.now().plusDays(30))
		);
		performance.getPerformanceDetails().add(performanceDetail);
		seat = seatRepository.save(createSeat(1L, performanceDetail));
		user = userRepository.save(createUser(1L));
		occupiedSeats = hazelcastInstance.getMap("occupiedSeats");
	}

	@DisplayName("공연 상세 식별자로 잔여석을 조회할 수 있다.")
	@Test
	void remaining_seats_is_searched_by_performance_detail_id() {
		// given
		Long performanceDetailId = performanceDetail.getId();

		// when
		List<SeatDto> result = seatService.searchAllRemainingSeats(performanceDetailId);

		// then
		assertThat(result.size()).isEqualTo(1);
		assertThat(result.get(0).getId()).isEqualTo(seat.getId());
		assertThat(result.get(0).getSeatClass()).isEqualTo(seat.getSeatClass());
		assertThat(result.get(0).getFloor()).isEqualTo(seat.getFloor());
		assertThat(result.get(0).getZone()).isEqualTo(seat.getZone());
		assertThat(result.get(0).getNumber()).isEqualTo(seat.getNumber());
		assertThat(result.get(0).getCost()).isEqualTo(seat.getCost());
	}

	@DisplayName("특정 사용자의 점유 좌석 정보 및 좌석 점유율을 조회할 수 있다.")
	@Test
	void can_search_occupied_seat_information_and_seat_occupancy_for_a_specific_user() {
		// given
		Seat secondSeat = seatRepository.save(createSeat(2L, performanceDetail));
		Seat thirdSeat = seatRepository.save(createSeat(3L, performanceDetail));
		seatRepository.save(createSeat(4L, performanceDetail));
		seatRepository.save(createSeat(5L, performanceDetail));

		LockInfo lockInfo = new LockInfo(user.getId(), 1);
		List<Long> seatIds = List.of(seat.getId(), secondSeat.getId(), thirdSeat.getId());
		occupiedSeats.put(seatIds.get(0), lockInfo);
		occupiedSeats.put(seatIds.get(1), lockInfo);
		occupiedSeats.put(seatIds.get(2), lockInfo);
		OccupancyInfoCondition condition = new OccupancyInfoCondition(user.getId(),
			performanceDetail.getId());

		// when
		SeatOccupancyInfo result = seatService.searchSeatOccupancyInfo(condition);

		// then
		double occupancyRate = BigDecimal.valueOf(result.getSeatIds().size())
			.divide(BigDecimal.valueOf(seatRepository.findAllByPerformanceDetail(performanceDetail).size()), 2,
				RoundingMode.HALF_UP)
			.doubleValue();
		assertThat(result.getSeatIds()).containsAll(seatIds);
		assertThat(result.getOccupancyRate()).isEqualTo(occupancyRate);
	}
}