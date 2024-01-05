package com.gwangya.purchase.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.ObjectUtils;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.PurchaseInfo;
import com.gwangya.purchase.domain.PurchaseSeat;

public class InMemoryPurchaseRepository implements PurchaseRepository {

	private final Map<Long, PurchaseSeat> seats = new HashMap<>();
	private final Map<Long, PurchaseInfo> infos = new HashMap<>();

	@Override
	public PurchaseInfo save(PurchaseInfo purchaseInfo) {
		if (!ObjectUtils.isEmpty(purchaseInfo.getPurchaseSeats())) {
			purchaseInfo.getPurchaseSeats().forEach(seat -> save(seat));
		}
		infos.put(purchaseInfo.getId(), purchaseInfo);
		return purchaseInfo;
	}

	public PurchaseSeat save(PurchaseSeat purchaseSeat) {
		return seats.put(purchaseSeat.getId(), purchaseSeat);
	}

	@Override
	public long countPurchasedSeat(PerformanceDetail performanceDetail, long userId) {
		return seats.values().stream()
			.filter(seat -> seat.getPurchaseInfo().getPerformanceDetail().equals(performanceDetail)
				&& seat.getPurchaseInfo().getUser().getId().equals(userId))
			.count();
	}
}
