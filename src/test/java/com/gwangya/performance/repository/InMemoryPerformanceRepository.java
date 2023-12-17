package com.gwangya.performance.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.util.ObjectUtils;

import com.gwangya.performance.domain.Performance;
import com.gwangya.performance.domain.PerformanceDetail;

public class InMemoryPerformanceRepository implements PerformanceRepository {

	private final Map<Long, PerformanceDetail> performanceDetails = new HashMap<>();
	private final Map<Long, Performance> performances = new HashMap<>();

	public PerformanceDetail save(PerformanceDetail performanceDetail) {
		Performance performance = performances.get(performanceDetail.getPerformance().getId());
		performance.getPerformanceDetails().add(performanceDetail);

		performanceDetails.put(performanceDetail.getId(), performanceDetail);
		return performanceDetail;
	}

	public Performance save(Performance performance) {
		if (!ObjectUtils.isEmpty(performance.getPerformanceDetails())) {
			performance.getPerformanceDetails().forEach(detail -> save(detail));
		}
		performances.put(performance.getId(), performance);
		return performance;
	}

	@Override
	public Optional<PerformanceDetail> findPerformanceDetailById(Long id) {
		return Optional.ofNullable(performanceDetails.get(id));
	}
}
