package com.gwangya.fixture.performance;

import static com.gwangya.performance.domain.ReceivingType.*;

import java.time.LocalDateTime;

import com.gwangya.performance.domain.Performance;
import com.gwangya.performance.domain.vo.SupportedReceivingType;

public class PerformanceFixture {

	public static Performance createPerformance(Long id) {
		return new Performance(id, "공연명", "장르", new SupportedReceivingType(ONSITE_AND_DELIVERY), "80분", "공연장 위치",
			"공연장 주소", LocalDateTime.now(), LocalDateTime.now());
	}
}
