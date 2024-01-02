package com.gwangya.performance.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gwangya.performance.dto.PerformanceDetailDto;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.service.PerformanceService;
import com.gwangya.performance.service.SeatService;
import com.gwangya.user.dto.UserDto;
import com.gwangya.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatFacade {

	private final UserService userService;

	private final SeatService seatService;

	private final PerformanceService performanceService;

	public List<SeatDto> searchAllRemainingSeats(final Long detailId, final Long userId) {
		UserDto userDto = userService.searchUserById(userId);
		PerformanceDetailDto detailDto = performanceService.searchPurchasablePerformanceDetailById(detailId,
			userDto.getId());
		return seatService.searchAllRemainingSeats(detailDto.getId());
	}

}
