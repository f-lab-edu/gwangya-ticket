package com.gwangya.purchase.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReceivingType {

	DIGITAL("디지털"),
	ONSITE("현장수령"),
	DELIVERY("배송");

	String title;
}
