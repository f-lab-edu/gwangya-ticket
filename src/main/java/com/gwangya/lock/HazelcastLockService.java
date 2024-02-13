package com.gwangya.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gwangya.global.util.LockReleaseEventListener;
import com.gwangya.lock.exception.LockOccupationException;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.IMap;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class HazelcastLockService implements LockService {

	public static final String SEAT_SESSION_MAP_NAME = "seatSession";

	@Qualifier("hazelcastInstance")
	private final HazelcastInstance hazelcastInstance;

	@PostConstruct
	private void initializeListeners(HazelcastInstance hazelcastInstance) {
		hazelcastInstance.getMap(SEAT_SESSION_MAP_NAME)
			.addEntryListener(
				new LockReleaseEventListener(hazelcastInstance),
				true
			);
	}

	public IMap<Long, Long> getLockMap(String key) {
		return hazelcastInstance.getMap(key);
	}

	@Override
	public FencedLock tryLock(long time, TimeUnit unit, String key, ConcurrentMap<Long, Long> occupiedLocks) throws
		LockOccupationException {
		final FencedLock fencedLock = hazelcastInstance.getCPSubsystem().getLock(key);

		checkLockStatus(occupiedLocks, fencedLock);
		boolean isSuccessfullyOccupied = fencedLock.tryLock(time, unit);
		if (!isSuccessfullyOccupied) {
			throw new LockOccupationException("Lock 획득에 실패했습니다.", key);
		}
		return fencedLock;
	}

	@Override
	public List<FencedLock> tryLockAll(long time, TimeUnit unit, List<String> keys,
		ConcurrentMap<Long, Long> occupiedLocks) throws LockOccupationException {
		final List<FencedLock> fencedLocks = new ArrayList<>();
		for (String key : keys) {
			final FencedLock fencedLock = hazelcastInstance.getCPSubsystem().getLock(String.valueOf(key));
			checkLockStatus(occupiedLocks, fencedLock);
			fencedLocks.add(fencedLock);
		}
		return fencedLocks.stream()
			.filter(lock -> lock.tryLock(time, unit))
			.toList();
	}

	private void checkLockStatus(final ConcurrentMap<Long, Long> occupiedLocks, final FencedLock lock) {
		if (occupiedLocks.containsKey(Long.valueOf(lock.getName())) || lock.isLocked()) {
			throw new LockOccupationException("Lock 획득에 실패했습니다.", lock.getName());
		}
	}

	@Override
	public void unlock(FencedLock lock) {
		if (lock.isLockedByCurrentThread()) {
			lock.unlock();
		}
	}

	@Override
	public void unlockAll(List<FencedLock> locks) {
		for (FencedLock lock : locks) {
			if (lock.isLockedByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}








