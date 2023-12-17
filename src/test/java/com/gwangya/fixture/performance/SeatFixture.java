package com.gwangya.fixture.performance;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;

public class SeatFixture {

	public static Seat createSeat(Long id, PerformanceDetail performanceDetail) {
		return new Seat(id, performanceDetail, "좌석 등급", "층", "구역", "좌석 번호", 150000);
	}
}