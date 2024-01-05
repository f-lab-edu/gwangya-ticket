package com.gwangya.purchase.repository;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.PurchaseInfo;

public interface PurchaseRepository {

	PurchaseInfo save(PurchaseInfo purchaseInfo);

	long countPurchasedSeat(PerformanceDetail performanceDetail, long userId);
}
