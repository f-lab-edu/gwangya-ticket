package com.gwangya.performance.repository;

import java.util.Optional;

import com.gwangya.performance.domain.PerformanceDetail;

public interface PerformanceRepository {

	Optional<PerformanceDetail> findPerformanceDetailById(Long id);
}
