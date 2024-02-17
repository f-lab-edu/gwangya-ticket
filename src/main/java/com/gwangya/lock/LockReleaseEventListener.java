package com.gwangya.lock;

import org.springframework.beans.factory.annotation.Qualifier;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.listener.EntryExpiredListener;
import com.hazelcast.map.listener.EntryRemovedListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LockReleaseEventListener implements EntryRemovedListener<Long, Long>, EntryExpiredListener<Long, Long> {

	@Qualifier("hazelcastInstance")
	private final HazelcastInstance hazelcastInstance;

	@Override
	public void entryRemoved(EntryEvent<Long, Long> event) {
		lockRelease(event.getKey());
	}

	@Override
	public void entryExpired(EntryEvent<Long, Long> event) {
		lockRelease(event.getKey());
	}

	private void lockRelease(final Long key) {
		final FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(String.valueOf(key));
		lock.destroy();
	}
}
