package com.gwangya.performance.domain;

import static com.gwangya.purchase.domain.PurchaseType.*;

import java.time.LocalDateTime;

public class PerformanceDetailFixture {

	public static PerformanceDetail createPerformanceDetail(Long id, Performance performance, LocalDateTime startTime,
		LocalDateTime closeTime) {
		return new PerformanceDetail(id, performance, GENERAL, 1, startTime, closeTime, LocalDateTime.now(),
			LocalDateTime.now());
	}
}
