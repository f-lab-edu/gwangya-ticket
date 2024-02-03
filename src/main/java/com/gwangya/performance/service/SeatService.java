package com.gwangya.performance.service;

import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.dto.SelectSeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

	private final SeatRepository seatRepository;

	@Qualifier("hazelcastInstance")
	private final HazelcastInstance hazelcastInstance;

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

	@Transactional
	public void selectSeat(final SelectSeatInfo selectSeatInfo) {
		final IMap<String, Long> selectedSeats = hazelcastInstance.getMap(
			String.valueOf(selectSeatInfo.getPerformanceDetailId()));
		final List<FencedLock> fencedLocks = getFencedLocks(selectedSeats, selectSeatInfo);

		final List<FencedLock> validLocks = fencedLocks.stream()
			.filter(fencedLock -> fencedLock.tryLock(1, TimeUnit.MINUTES))
			.collect(Collectors.toUnmodifiableList());
		try {
			if (validLocks.size() == fencedLocks.size()) {
				validLocks.forEach(lock -> selectedSeats.put(lock.getName(), selectSeatInfo.getUserId(), 5,
					TimeUnit.MINUTES));
			}
		} finally {
			validLocks.forEach(lock -> lock.unlock());
		}

		if (validLocks.size() != fencedLocks.size()) {
			throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
				selectSeatInfo.getPerformanceDetailId());
		}
	}

	private List<FencedLock> getFencedLocks(final IMap<String, Long> selectedSeats,
		final SelectSeatInfo selectSeatInfo) {

		final List<FencedLock> fencedLocks = new ArrayList<>();
		for (long seatId : selectSeatInfo.getSeatIds()) {
			final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(String.valueOf(seatId));
			if (isSelected(selectedSeats, lock)) {
				throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
					selectSeatInfo.getPerformanceDetailId());
			}
			fencedLocks.add(lock);
		}
		return fencedLocks;
	}

	private boolean isSelected(final IMap<String, Long> selectedSeats, final FencedLock lock) {
		return selectedSeats.containsKey(lock.getName()) || lock.isLocked();
	}
}
