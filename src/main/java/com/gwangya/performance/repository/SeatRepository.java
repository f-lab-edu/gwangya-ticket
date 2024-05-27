package com.gwangya.performance.repository;

import java.util.List;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;

public interface SeatRepository {

	List<Seat> findRemainingAllByPerformanceDetailId(long performanceDetailId);

	List<Seat> findAllById(List<Long> seatIds);

	List<Seat> findAllByPerformanceDetail(PerformanceDetail performanceDetail);

	long countAllByPerformanceDetailId(long performanceDetailId);
}
