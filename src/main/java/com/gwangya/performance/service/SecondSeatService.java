package com.gwangya.performance.service;

import static com.gwangya.lock.HazelcastLockService.*;
import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.lock.LockReleaseEventListener;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SecondSeatService {

	@Qualifier("hazelcastInstance")
	private final HazelcastInstance hazelcastInstance;

	@PostConstruct
	private void initializeListeners() {
		hazelcastInstance.getMap(SEAT_SESSION_MAP_NAME)
			.addEntryListener(
				new LockReleaseEventListener(hazelcastInstance),
				true
			);
	}

	@Transactional
	public void occupySeats_singleLock(final OccupySeatInfo occupySeatInfo, final long tryTime, final TimeUnit unit) {
		final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(SEAT_SESSION_MAP_NAME);
		final IMap<Long, Long> occupiedSeats = hazelcastInstance.getMap(SEAT_SESSION_MAP_NAME);

		try {
			boolean isAcquired = lock.tryLock(tryTime, unit);
			if (isAcquired && isPossible(occupiedSeats, occupySeatInfo.getSeatIds())) {
				for (long seatId : occupySeatInfo.getSeatIds()) {
					occupiedSeats.put(seatId, occupySeatInfo.getUserId(), 5, TimeUnit.MINUTES);
				}
			} else {
				throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
					occupySeatInfo.getPerformanceDetailId());
			}
		} finally {
			if (lock.isLocked()) {
				lock.unlock();
			}
		}
	}

	private boolean isPossible(final IMap<Long, Long> occupiedSeats, final List<Long> seatIds) {
		return seatIds.stream()
			.noneMatch(seatId -> occupiedSeats.containsKey(seatId));
	}
}
