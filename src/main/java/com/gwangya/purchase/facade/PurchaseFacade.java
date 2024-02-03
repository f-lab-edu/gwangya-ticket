package com.gwangya.purchase.facade;

import org.springframework.stereotype.Service;

import com.gwangya.performance.dto.PerformanceDetailDto;
import com.gwangya.performance.service.PerformanceService;
import com.gwangya.performance.service.SeatService;
import com.gwangya.purchase.dto.SelectSeatInfo;
import com.gwangya.purchase.service.PurchaseService;
import com.gwangya.user.dto.UserDto;
import com.gwangya.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseFacade {

	private final UserService userService;

	private final PerformanceService performanceService;

	private final PurchaseService purchaseService;

	private final SeatService seatService;

	public void selectAllSeats(final SelectSeatInfo selectSeatInfo) {
		final UserDto userDto = userService.searchUserById(selectSeatInfo.getUserId());
		final PerformanceDetailDto performanceDetailDto = performanceService.searchPurchasablePerformanceDetailById(
			selectSeatInfo.getPerformanceDetailId(),
			userDto.getId());
		purchaseService.checkValidSeat(selectSeatInfo);
		seatService.selectSeat(selectSeatInfo);
	}
}
