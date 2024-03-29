package com.gwangya.lock;

import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.gwangya.lock.exception.LockOccupationException;
import com.gwangya.purchase.domain.LockInfo;
import com.hazelcast.cp.lock.FencedLock;

public interface LockService {

	ConcurrentMap<Long, LockInfo> getLockMap(String key);

	FencedLock tryLock(long time, TimeUnit unit, String key, ConcurrentMap<Long, LockInfo> occupiedLocks) throws
		LockOccupationException;

	List<FencedLock> tryLockAll(long time, TimeUnit unit, List<String> keys,
		ConcurrentMap<Long, LockInfo> occupiedLocks) throws
		LockOccupationException;

	void unlock(FencedLock lock);

	void unlock(String key);

	void unlockAll(List<FencedLock> locks);

}
