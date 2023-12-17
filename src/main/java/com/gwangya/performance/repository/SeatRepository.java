package com.gwangya.performance.repository;

import java.util.List;

import com.gwangya.performance.domain.Seat;

public interface SeatRepository {

	List<Seat> findRemainingAllByPerformanceDetailId(Long performanceDetailId);
}
