package com.gwangya.performance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.facade.SeatFacade;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SeatController {

	private final SeatFacade seatFacade;

	@GetMapping("/api/v1/performance/{performanceId}/{performanceDetailId}/seat")
	public ResponseEntity<List<SeatDto>> searchAllRemainingSeat(@PathVariable long performanceId,
		@PathVariable long performanceDetailId, @RequestAttribute(name = "userId") long userId) {
		return ResponseEntity.ok(seatFacade.searchAllRemainingSeats(performanceDetailId, userId));
	}
}
