package com.gwangya.performance.service;

import static org.springframework.transaction.annotation.Isolation.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.repository.PurchaseSeatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

	private final SeatRepository seatRepository;

	private final PurchaseSeatRepository purchaseSeatRepository;

	@Transactional(readOnly = true)
	public List<SeatDto> searchAllRemainingSeats(final long detailId) {
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

	@Transactional(isolation = READ_UNCOMMITTED)
	public void selectSeat(final long userId, final long seatId) {
		Seat seat = seatRepository.findById(seatId)
			.orElseThrow(() -> new EntityNotFoundException("해당 좌석이 존재하지 않습니다.", Seat.class, seatId));
		if (!purchaseSeatRepository.existsBySeat(seat)) {
			throw new UnavailablePurchaseException("이미 선택된 좌석입니다.");
		}
	}
}
