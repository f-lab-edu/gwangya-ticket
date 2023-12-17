package com.gwangya.performance.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gwangya.performance.domain.Seat;

public class InMemorySeatRepository implements SeatRepository {

	private final Map<Long, Seat> seats = new HashMap<>();

	public Seat save(Seat seat) {
		seats.put(seat.getId(), seat);
		return seat;
	}

	@Override
	public List<Seat> findRemainingAllByPerformanceDetailId(Long performanceDetailId) {
		return seats.values().stream()
			.filter(seat -> seat.getPerformanceDetail().getId().equals(performanceDetailId))
			.collect(Collectors.toUnmodifiableList());
	}
}
