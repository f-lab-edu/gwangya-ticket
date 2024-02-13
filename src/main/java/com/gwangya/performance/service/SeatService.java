package com.gwangya.performance.service;

import static com.gwangya.lock.HazelcastLockService.*;
import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.lock.LockService;
import com.gwangya.lock.exception.LockOccupationException;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

	private final SeatRepository seatRepository;

	private final LockService lockService;

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
			)
			.toList();
	}

	@Transactional
	public void occupySeats(final OccupySeatInfo occupySeatInfo) {
		final IMap<Long, Long> occupiedSeats = (IMap<Long, Long>)lockService.getLockMap(SEAT_SESSION_MAP_NAME);
		try {
			final List<FencedLock> validLocks = lockService.tryLockAll(3, TimeUnit.MILLISECONDS,
				occupySeatInfo.getSeatIds().stream()
					.map(seatId -> String.valueOf(seatId))
					.toList(),
				occupiedSeats
			);
			if (!isSuccessfullyOccupied(occupySeatInfo.getSeatIds(), validLocks)) {
				lockService.unlockAll(validLocks);
				throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
					occupySeatInfo.getPerformanceDetailId());
			}
			validLocks.forEach(lock ->
				occupiedSeats.put(Long.valueOf(lock.getName()), occupySeatInfo.getUserId(), 5,
					TimeUnit.MINUTES)
			);
		} catch (LockOccupationException e) {
			throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
				occupySeatInfo.getPerformanceDetailId(), Long.valueOf(e.getName()));
		}
	}

	private boolean isSuccessfullyOccupied(final List<Long> selectSeats, final List<FencedLock> validLocks) {
		final List<Long> lockNames = validLocks.stream()
			.map(lock -> Long.valueOf(lock.getName()))
			.toList();
		return lockNames.containsAll(selectSeats);
	}
}
