package com.gwangya.fixture.purchase;

import java.time.LocalDateTime;

import com.gwangya.performance.domain.Seat;
import com.gwangya.purchase.domain.PurchaseInfo;
import com.gwangya.purchase.domain.PurchaseSeat;

public class PurchaseSeatFixture {

	public static PurchaseSeat createPurchaseSeat(Long id, PurchaseInfo purchaseInfo, Seat seat) {
		return new PurchaseSeat(id, purchaseInfo, seat, LocalDateTime.now(), LocalDateTime.now());
	}
}

