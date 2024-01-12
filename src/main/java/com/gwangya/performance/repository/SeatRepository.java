package com.gwangya.performance.repository;

import java.util.List;
import java.util.Optional;

import com.gwangya.performance.domain.Seat;

public interface SeatRepository {

	List<Seat> findRemainingAllByPerformanceDetailId(long performanceDetailId);

	Optional<Seat> findById(long seatId);
}
