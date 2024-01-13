package com.gwangya.performance.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UnavailablePurchaseType {

	NO_AUTHORITY("예매 권한 없음"),
	NOT_PURCHASE_PERIOD("예매 가능 기간 아님"),
	EXCEED_PURCHASE_LIMITS("예매 가능 매수 초과"),
	SELECTED_SEAT("이미 선택된 좌석");

	private final String description;
}
