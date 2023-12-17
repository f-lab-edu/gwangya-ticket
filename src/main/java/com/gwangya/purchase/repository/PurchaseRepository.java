package com.gwangya.purchase.repository;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.PurchaseInfo;
import com.gwangya.user.domain.User;

public interface PurchaseRepository {

	PurchaseInfo save(PurchaseInfo purchaseInfo);

	long countPurchasedSeatByPerformanceDetailAndUser(PerformanceDetail performanceDetail, User user);
}
