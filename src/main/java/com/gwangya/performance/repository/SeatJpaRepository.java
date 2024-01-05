package com.gwangya.performance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;

public interface SeatJpaRepository extends JpaRepository<Seat, Long> {
	List<Seat> findAllByPerformanceDetail(PerformanceDetail performanceDetail);
}
