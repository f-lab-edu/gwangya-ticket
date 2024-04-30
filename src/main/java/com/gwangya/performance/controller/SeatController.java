package com.gwangya.performance.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.gwangya.performance.dto.OccupancyInfoCondition;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.dto.SeatOccupancyInfo;
import com.gwangya.performance.facade.SeatFacade;
import com.gwangya.performance.service.SeatService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController("/api/v1/performance")
public class SeatController {

	private final SeatFacade seatFacade;

	private final SeatService seatService;

	@GetMapping("/{performanceId}/{performanceDetailId}/seat")
	public ResponseEntity<List<SeatDto>> searchAllRemainingSeat(@PathVariable long performanceId,
		@PathVariable long performanceDetailId, @RequestAttribute(name = "userId") long userId) {
		return ResponseEntity.ok(seatFacade.searchAllRemainingSeats(performanceDetailId, userId));
	}

	@GetMapping("/{performanceDetailId}/seat/{userId}")
	public ResponseEntity<SeatOccupancyInfo> searchSeatOccupancyInfo(@PathVariable long performanceDetailId,
		@PathVariable long userId) {
		return ResponseEntity.ok(
			seatService.searchSeatOccupancyInfo(new OccupancyInfoCondition(userId, performanceDetailId)));
	}
}
