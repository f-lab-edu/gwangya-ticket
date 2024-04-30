package com.gwangya.performance.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;

public class InMemorySeatRepository implements SeatRepository {

	private final Map<Long, Seat> seats = new HashMap<>();

	public Seat save(Seat seat) {
		seats.put(seat.getId(), seat);
		return seat;
	}

	@Override
	public List<Seat> findRemainingAllByPerformanceDetailId(long performanceDetailId) {
		return seats.values().stream()
			.filter(seat -> seat.getPerformanceDetail().getId().equals(performanceDetailId))
			.toList();
	}

	@Override
	public List<Seat> findAllById(List<Long> seatIds) {
		return seats.values().stream()
			.filter(seat -> seatIds.contains(seat.getId()))
			.toList();
	}

	@Override
	public List<Seat> findAllByPerformanceDetail(PerformanceDetail performanceDetail) {
		return seats.values().stream()
			.filter(seat -> seat.getPerformanceDetail().equals(performanceDetail))
			.toList();
	}

	@Override
	public long countAllByPerformanceDetailId(long performanceDetailId) {
		return seats.values().stream()
			.filter(seat -> seat.getPerformanceDetail().getId().equals(performanceDetailId))
			.count();
	}
}
