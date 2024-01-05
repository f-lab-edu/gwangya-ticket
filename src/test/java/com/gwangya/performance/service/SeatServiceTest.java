package com.gwangya.performance.service;

import static com.gwangya.performance.domain.PerformanceDetailFixture.*;
import static com.gwangya.performance.domain.PerformanceFixture.*;
import static com.gwangya.performance.domain.SeatFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gwangya.performance.domain.Performance;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.repository.InMemoryPerformanceRepository;
import com.gwangya.performance.repository.InMemorySeatRepository;

class SeatServiceTest {

	InMemoryPerformanceRepository performanceRepository;

	InMemorySeatRepository seatRepository;

	SeatService seatService;

	Performance performance;

	PerformanceDetail performanceDetail;

	Seat seat;

	@BeforeEach
	void setUp() {
		performanceRepository = new InMemoryPerformanceRepository();
		seatRepository = new InMemorySeatRepository();
		seatService = new SeatService(seatRepository);

		performance = performanceRepository.save(createPerformance(1L));
		performanceDetail = performanceRepository.save(
			createPerformanceDetail(1L, performance, LocalDateTime.now().minusDays(1),
				LocalDateTime.now().plusDays(30))
		);
		performance.getPerformanceDetails().add(performanceDetail);
		seat = seatRepository.save(createSeat(1L, performanceDetail));

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
}