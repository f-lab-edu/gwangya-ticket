package com.gwangya.performance.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReceivingType {

	DIGITAL("디지털"),
	ONSITE("현장수령"),
	DELIVERY("배송"),
	ONSITE_AND_DELIVERY("현장수령/배송");

	String title;
}
