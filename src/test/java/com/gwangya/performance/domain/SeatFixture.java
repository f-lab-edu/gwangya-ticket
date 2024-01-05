package com.gwangya.performance.domain;

import java.time.LocalDateTime;

public class SeatFixture {

	public static Seat createSeat(Long id, PerformanceDetail performanceDetail) {
		return new Seat(id, performanceDetail, "좌석 등급", "층", "구역", "좌석 번호", 150000, LocalDateTime.now(),
			LocalDateTime.now());
	}
}