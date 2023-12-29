package com.gwangya.performance.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SeatDto {

	private long id;

	private String seatClass;

	private String floor;

	private String zone;

	private String number;

	private int cost;
}
