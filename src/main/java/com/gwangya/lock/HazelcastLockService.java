package com.gwangya.lock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gwangya.lock.exception.LockOccupationException;
import com.gwangya.purchase.domain.LockInfo;
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
	private void initializeListeners() {
		hazelcastInstance.getMap(SEAT_SESSION_MAP_NAME)
			.addEntryListener(
				new LockReleaseEventListener(this),
				true
			);
	}

	public IMap<Long, LockInfo> getLockMap(String key) {
		return hazelcastInstance.getMap(key);
	}

	@Override
	public FencedLock tryLock(long time, TimeUnit unit, String key, ConcurrentMap<Long, LockInfo> occupiedLocks) throws
		LockOccupationException {
		final FencedLock fencedLock = hazelcastInstance.getCPSubsystem().getLock(key);

		checkLockStatus(occupiedLocks, fencedLock);
		if (!fencedLock.tryLock(time, unit)) {
			throw new LockOccupationException("Lock 획득에 실패했습니다.", key);
		}
		return fencedLock;
	}

	@Override
	public List<FencedLock> tryLockAll(long time, TimeUnit unit, List<String> keys,
		ConcurrentMap<Long, LockInfo> occupiedLocks) throws LockOccupationException {
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

	private void checkLockStatus(final ConcurrentMap<Long, LockInfo> occupiedLocks, final FencedLock lock) {
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
	public void unlock(final String key) {
		final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(key);
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








