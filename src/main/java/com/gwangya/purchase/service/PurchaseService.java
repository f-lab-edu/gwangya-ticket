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
import com.gwangya.purchase.domain.PurchaseSeat;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.gwangya.purchase.repository.PurchaseSeatRepository;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseService {

	private final SeatRepository seatRepository;

	private final PurchaseSeatRepository purchaseSeatRepository;

	/**
	 * DB에 반영 전인 {@link PurchaseSeat}도 확인하기 위해 트랜잭션 격리수준을 READ_UNCOMMITTED로 설정합니다.
	 * 이 메서드는 '좌석 선택' 단계에서 호출됩니다. 좌석 선택과 예매(PurchaseSeat 저장)는 별개의 트랜잭션으로 이루어지고,
	 * 좌석 선택 데이터는 DB가 아닌 별도의 {@link IMap}에 저장됩니다.
	 * 따라서 commit 전의 PurchaseSeat은 다른 요청에 의해 선점된 {@link Seat}일 수 있습니다.
	 * READ_UNCOMMITTED은 예매 중인 Seat을 유효한 것으로 간주하는 것을 방지합니다.
	 */
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