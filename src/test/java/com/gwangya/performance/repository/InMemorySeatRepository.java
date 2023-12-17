package com.gwangya.performance.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;

public class InMemorySeatRepository implements SeatRepository {

	private final Map<Long, Seat> seats = new HashMap<>();

	public Seat save(Seat seat) {
		seats.put(seat.getId(), seat);
		return seat;
	}

	@Override
	public List<Seat> findRemainingAllByPerformanceDetail(PerformanceDetail performanceDetail) {
		return seats.values().stream()
			.filter(seat -> seat.getPerformanceDetail().equals(performanceDetail))
			.collect(Collectors.toUnmodifiableList());
	}
}
