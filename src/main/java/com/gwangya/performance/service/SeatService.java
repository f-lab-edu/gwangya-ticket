package com.gwangya.performance.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.repository.SeatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

	private final SeatRepository seatRepository;

	@Transactional(readOnly = true)
	public List<SeatDto> searchAllRemainingSeats(final Long detailId) {
		List<Seat> result = seatRepository.findRemainingAllByPerformanceDetailId(detailId);
		return result.stream()
			.map(seat -> new SeatDto(
					seat.getId(),
					seat.getSeatClass(),
					seat.getFloor(),
					seat.getZone(),
					seat.getNumber(),
					seat.getCost()
				)
			).collect(Collectors.toUnmodifiableList());
	}
}
