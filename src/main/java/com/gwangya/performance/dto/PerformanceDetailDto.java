package com.gwangya.performance.dto;

import java.time.LocalDateTime;

import com.gwangya.purchase.domain.PurchaseType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PerformanceDetailDto {

	private final Long id;

	private final PurchaseType purchaseType;

	private final Integer limitCount;

	private final LocalDateTime ticketingStartTime;

	private final LocalDateTime ticketingCloseTime;
}
