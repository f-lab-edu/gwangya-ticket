package com.gwangya.performance.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SeatOccupancyInfo {

	private final long userId;

	private final List<Long> seatIds;

	private final double occupancyRate;
}
