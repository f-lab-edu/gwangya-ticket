package com.gwangya.lock;

import com.gwangya.purchase.domain.LockInfo;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.internal.util.ThreadUtil;
import com.hazelcast.map.listener.EntryExpiredListener;
import com.hazelcast.map.listener.EntryRemovedListener;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LockReleaseEventListener
	implements EntryRemovedListener<Long, LockInfo>, EntryExpiredListener<Long, LockInfo> {

	private final LockService lockService;

	@Override
	public void entryRemoved(EntryEvent<Long, LockInfo> event) {
		lockRelease(event);
	}

	@Override
	public void entryExpired(EntryEvent<Long, LockInfo> event) {
		lockRelease(event);
	}

	private void lockRelease(final EntryEvent<Long, LockInfo> event) {
		ThreadUtil.setThreadId(event.getOldValue().getThreadId());
		lockService.unlock(String.valueOf(event.getKey()));
	}
}
