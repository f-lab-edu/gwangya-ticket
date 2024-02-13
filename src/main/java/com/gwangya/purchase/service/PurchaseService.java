package com.gwangya.purchase.service;

import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.gwangya.purchase.repository.PurchaseSeatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseService {

	private final SeatRepository seatRepository;

	private final PurchaseSeatRepository purchaseSeatRepository;

	@Transactional(readOnly = true, isolation = Isolation.READ_UNCOMMITTED)
	public void checkValidSeat(final OccupySeatInfo occupySeatInfo) {
		final List<Seat> seats = seatRepository.findAllById(occupySeatInfo.getSeatIds());
		if (seats.size() < occupySeatInfo.getSeatIds().size()) {
			List<Long> seatIds = seats.stream()
				.map(Seat::getId)
				.toList();
			occupySeatInfo.getSeatIds().stream()
				.filter(seatId -> !seatIds.contains(seatId))
				.findAny()
				.ifPresent(seatId -> {
					throw new EntityNotFoundException("해당 좌석이 존재하지 않습니다.", Seat.class, seatId);
				});
		}
		if (purchaseSeatRepository.existsAnyBySeat(seats)) {
			throw new UnavailablePurchaseException("이미 예매 완료된 좌석입니다.", PURCHASED_SEAT,
				occupySeatInfo.getPerformanceDetailId());
		}
	}
}