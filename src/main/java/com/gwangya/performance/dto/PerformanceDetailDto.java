package com.gwangya.performance.dto;

import java.time.LocalDateTime;

import com.gwangya.purchase.domain.PurchaseType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PerformanceDetailDto {

	private Long id;

	private PurchaseType purchaseType;

	private Integer limitCount;

	private LocalDateTime ticketingStartTime;

	private LocalDateTime ticketingCloseTime;
}
