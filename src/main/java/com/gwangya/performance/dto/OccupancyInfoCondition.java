package com.gwangya.performance.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class OccupancyInfoCondition {

	private final long userId;

	private final long performanceDetailId;
}
