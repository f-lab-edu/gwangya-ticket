package com.gwangya.purchase.repository;

import com.gwangya.performance.domain.Seat;

public interface PurchaseSeatRepository {

	boolean existsBySeat(Seat seat);
}
