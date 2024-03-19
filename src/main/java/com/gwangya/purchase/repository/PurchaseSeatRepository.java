package com.gwangya.purchase.repository;

import java.util.List;

import com.gwangya.performance.domain.Seat;
import com.gwangya.purchase.domain.PurchaseSeat;

public interface PurchaseSeatRepository {

	boolean existsAnyBySeat(List<Seat> seats);

	PurchaseSeat save(PurchaseSeat purchaseSeat);
}
