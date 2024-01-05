package com.gwangya.purchase.domain;

import java.time.LocalDateTime;

import com.gwangya.performance.domain.Seat;

public class PurchaseSeatFixture {

	public static PurchaseSeat createPurchaseSeat(Long id, PurchaseInfo purchaseInfo, Seat seat) {
		return new PurchaseSeat(id, purchaseInfo, seat, LocalDateTime.now(), LocalDateTime.now());
	}
}

