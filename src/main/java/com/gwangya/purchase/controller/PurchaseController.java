package com.gwangya.purchase.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gwangya.purchase.dto.CreateOccupySeatCommand;
import com.gwangya.purchase.dto.OccupySeatDto;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.gwangya.purchase.facade.PurchaseFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PurchaseController {

	private final PurchaseFacade purchaseFacade;

	@PostMapping("/api/v1/performance/{performanceDetailId}/seat")
	public ResponseEntity<Void> occupySeat(@PathVariable long performanceDetailId,
		@RequestAttribute(name = "userId") long userId, CreateOccupySeatCommand createOccupySeatCommand) {
		purchaseFacade.occupySeats(
			new OccupySeatInfo(performanceDetailId, userId, createOccupySeatCommand.getSeatIds()));
		return ResponseEntity.ok().build();
	}

	@PostMapping("/api/v1/performance/{performanceDetailId}/seat/test")
	public ResponseEntity<OccupySeatDto> occupySeatForTest(@PathVariable long performanceDetailId,
		@RequestParam(name = "userId") long userId, CreateOccupySeatCommand createOccupySeatCommand) {
		OccupySeatDto result = purchaseFacade.occupySeats(
			new OccupySeatInfo(performanceDetailId, userId, createOccupySeatCommand.getSeatIds()));
		return ResponseEntity.ok(result);
	}
}
