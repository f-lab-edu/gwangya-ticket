package com.gwangya.performance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SeatDto {

	private final long id;

	private final String seatClass;

	private final String floor;

	private final String zone;

	private final String number;

	private final int cost;
}
