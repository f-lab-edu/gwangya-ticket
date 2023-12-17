package com.gwangya.performance.repository.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.repository.PerformanceDetailJpaRepository;
import com.gwangya.performance.repository.PerformanceRepository;

@Repository
public class PerformanceRepositoryImpl implements PerformanceRepository {

	private final PerformanceDetailJpaRepository jpaRepository;

	public PerformanceRepositoryImpl(PerformanceDetailJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	@Override
	public Optional<PerformanceDetail> findPerformanceDetailById(Long id) {
		return jpaRepository.findById(id);
	}
}
