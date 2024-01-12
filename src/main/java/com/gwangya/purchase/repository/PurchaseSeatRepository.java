package com.gwangya.purchase.repository;

import com.gwangya.performance.domain.Seat;
import com.gwangya.purchase.domain.PurchaseSeat;

public interface PurchaseSeatRepository {

	boolean existsBySeat(Seat seat);

	PurchaseSeat save(PurchaseSeat purchaseSeat);
}
