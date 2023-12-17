package com.gwangya.fixture.performance;

import static com.gwangya.purchase.domain.PurchaseType.*;

import java.time.LocalDateTime;

import com.gwangya.performance.domain.Performance;
import com.gwangya.performance.domain.PerformanceDetail;

public class PerformanceDetailFixture {

	public static PerformanceDetail createPerformanceDetail(Long id, Performance performance, LocalDateTime startTime,
		LocalDateTime closeTime) {
		return new PerformanceDetail(id, performance, GENERAL, 1, startTime, closeTime, LocalDateTime.now(),
			LocalDateTime.now());
	}
}
