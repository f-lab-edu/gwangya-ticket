package com.gwangya.performance.domain;

import static com.gwangya.performance.domain.ReceivingType.*;

import java.time.LocalDateTime;

public class PerformanceFixture {

	public static Performance createPerformance(Long id) {
		return new Performance(id, "공연명", "장르", ONSITE_AND_DELIVERY, "80분", "공연장 위치",
			"공연장 주소", LocalDateTime.now(), LocalDateTime.now());
	}
}
