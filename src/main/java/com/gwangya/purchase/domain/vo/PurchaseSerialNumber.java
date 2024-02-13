package com.gwangya.purchase.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class PurchaseSerialNumber {

	@Column(name = "purchase_serial_number", nullable = false)
	private String number;

	public static PurchaseSerialNumber of() {
		return new PurchaseSerialNumber("");
	}
}
