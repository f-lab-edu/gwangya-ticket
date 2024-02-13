package com.gwangya.purchase.facade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.service.PerformanceService;
import com.gwangya.performance.service.SeatService;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.gwangya.purchase.service.PurchaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseFacade {

	private final PerformanceService performanceService;

	private final PurchaseService purchaseService;

	private final SeatService seatService;

	@Transactional
	public void occupySeats(final OccupySeatInfo occupySeatInfo) throws UnavailablePurchaseException {
		performanceService.checkPurchasablePerformanceDetail(occupySeatInfo.getPerformanceDetailId(),
			occupySeatInfo.getUserId());
		purchaseService.checkValidSeat(occupySeatInfo);
		seatService.occupySeats(occupySeatInfo);
	}
}
