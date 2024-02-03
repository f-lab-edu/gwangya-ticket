package com.gwangya.purchase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.gwangya.purchase.dto.CreateSelectSeatCommand;
import com.gwangya.purchase.dto.SelectSeatInfo;
import com.gwangya.purchase.facade.PurchaseFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PurchaseController {

	private final PurchaseFacade purchaseFacade;

	@PostMapping("/api/v1/performance/{performanceDetailId}/seat")
	public ResponseEntity<Void> selectSeat(@PathVariable long performanceDetailId,
		@RequestAttribute(name = "userId") long userId, CreateSelectSeatCommand createSelectSeatCommand) {
		purchaseFacade.selectAllSeats(
			new SelectSeatInfo(performanceDetailId, userId, createSelectSeatCommand.getSeatIds()));
		return ResponseEntity.ok().build();
	}
}
