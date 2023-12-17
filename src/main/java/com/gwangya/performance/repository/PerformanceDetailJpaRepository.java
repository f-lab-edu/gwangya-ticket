package com.gwangya.performance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gwangya.performance.domain.PerformanceDetail;

public interface PerformanceDetailJpaRepository extends JpaRepository<PerformanceDetail, Long> {

}
