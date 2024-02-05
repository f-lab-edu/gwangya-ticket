package com.gwangya.global.util;

import org.springframework.beans.factory.annotation.Qualifier;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.map.listener.EntryRemovedListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LockReleaseEventListener implements EntryRemovedListener<Long, Long> {

	@Qualifier("hazelcastInstance")
	private final HazelcastInstance hazelcastInstance;

	@Override
	public void entryRemoved(EntryEvent<Long, Long> event) {
		Long key = event.getKey();
		FencedLock lock = hazelcastInstance.getCPSubsystem().getLock(String.valueOf(key));
		lock.destroy();
	}
}
