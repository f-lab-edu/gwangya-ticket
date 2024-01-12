package com.gwangya.purchase.service;

import static org.springframework.transaction.annotation.Isolation.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.domain.PurchaseSeat;
import com.gwangya.purchase.dto.CreateSelectSeatCommand;
import com.gwangya.purchase.repository.PurchaseSeatRepository;
import com.gwangya.user.domain.User;
import com.gwangya.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseService {

	private final UserRepository userRepository;

	private final SeatRepository seatRepository;

	private final PurchaseSeatRepository purchaseSeatRepository;

	@Transactional(isolation = READ_UNCOMMITTED)
	public void selectSeat(final CreateSelectSeatCommand createSelectSeatCommand) {
		final User user = userRepository.findById(createSelectSeatCommand.getUserId())
			.orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다.", User.class,
				createSelectSeatCommand.getUserId()));
		final Seat seat = seatRepository.findById(createSelectSeatCommand.getSeatId())
			.orElseThrow(() -> new EntityNotFoundException("해당 좌석이 존재하지 않습니다.", Seat.class,
				createSelectSeatCommand.getUserId()));
		if (purchaseSeatRepository.existsBySeat(seat)) {
			throw new UnavailablePurchaseException("이미 선택된 좌석입니다.");
		}
		purchaseSeatRepository.save(PurchaseSeat.selectSeat(user, seat));
	}
}
