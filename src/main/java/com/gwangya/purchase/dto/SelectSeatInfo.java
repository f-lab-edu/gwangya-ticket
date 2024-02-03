package com.gwangya.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SelectSeatInfo {

	private final long performanceDetailId;

	private final long userId;

	private final List<Long> seatIds;
}
