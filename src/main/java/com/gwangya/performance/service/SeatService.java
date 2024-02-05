package com.gwangya.performance.service;

import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.util.LockReleaseEventListener;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.dto.SelectSeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;

@Service
public class SeatService {

	private static final String SEAT_SESSION_MAP_NAME = "seatSession";

	private final SeatRepository seatRepository;

	@Qualifier("hazelcastInstance")
	private final HazelcastInstance hazelcastInstance;

	public SeatService(SeatRepository seatRepository, HazelcastInstance hazelcastInstance) {
		this.seatRepository = seatRepository;
		this.hazelcastInstance = hazelcastInstance;
		initializeListeners(hazelcastInstance);
	}

	private void initializeListeners(HazelcastInstance hazelcastInstance) {
		hazelcastInstance.getMap(SEAT_SESSION_MAP_NAME)
			.addEntryListener(
				new LockReleaseEventListener(hazelcastInstance),
				true
			);
	}

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
	public void selectSeat(final SelectSeatInfo selectSeatInfo) {
		final IMap<Long, Long> selectedSeats = hazelcastInstance.getMap(SEAT_SESSION_MAP_NAME);
		final List<FencedLock> fencedLocks = getFencedLocks(selectedSeats, selectSeatInfo);

		final List<FencedLock> validLocks = fencedLocks.stream()
			.filter(fencedLock -> fencedLock.tryLock(3, TimeUnit.MILLISECONDS))
			.toList();
		if (validLocks.size() != fencedLocks.size()) {
			throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
				selectSeatInfo.getPerformanceDetailId());
		}
		validLocks.forEach(lock ->
			selectedSeats.put(Long.valueOf(lock.getName()), selectSeatInfo.getUserId(), 5,
				TimeUnit.MINUTES)
		);
	}

	private List<FencedLock> getFencedLocks(final IMap<Long, Long> selectedSeats,
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

	private boolean isSelected(final IMap<Long, Long> selectedSeats, final FencedLock lock) {
		return selectedSeats.containsKey(lock.getName()) || lock.isLocked();
	}
}
