package com.gwangya.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OccupySeatDto {

	private final List<Long> seatIds;
	private final long occupancySeatCount;
}
