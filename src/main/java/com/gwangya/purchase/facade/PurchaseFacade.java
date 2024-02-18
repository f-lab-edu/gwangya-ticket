package com.gwangya.purchase.facade;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.service.PerformanceService;
import com.gwangya.performance.service.SeatService;
import com.gwangya.purchase.domain.PurchaseSeat;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.gwangya.purchase.service.PurchaseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PurchaseFacade {

	private final PerformanceService performanceService;

	private final PurchaseService purchaseService;

	private final SeatService seatService;

	/**
	 * 요청된 좌석의 유효성을 검사하고, Seat의 id(PK)의 Lock을 통해 일정 기간 동안 점유를 설정하는 메서드입니다.
	 * 아래와 같은 상황 발생 시 {@link UnavailablePurchaseException}을 던집니다. <br>
	 *
	 * <li>예매 가능 기간이 아닐 때({@link PerformanceDetail#checkPurchasePeriod(LocalDateTime)}) </li>
	 * <li>예매 가능 매수를 초과했을 때({@link PerformanceDetail#checkTicketLimit(long)})</li>
	 * <li>예매가 완료된 좌석일 때({@link PurchaseSeat})</li>
	 * <li>좌석에 Lock({@link com.hazelcast.cp.lock.FencedLock}이 걸려 있거나 이미 선점된 상태({@link com.hazelcast.map.IMap}에 존재)일 때</li>
	 *
	 * @param occupySeatInfo
	 * @throws UnavailablePurchaseException
	 * @see SeatService#occupySeats(OccupySeatInfo)
	 */
	@Transactional
	public void occupySeats(final OccupySeatInfo occupySeatInfo) throws UnavailablePurchaseException {
		performanceService.checkPurchasablePerformanceDetail(occupySeatInfo.getPerformanceDetailId(),
			occupySeatInfo.getUserId());
		purchaseService.checkValidSeat(occupySeatInfo);
		seatService.occupySeats(occupySeatInfo);
	}
}
