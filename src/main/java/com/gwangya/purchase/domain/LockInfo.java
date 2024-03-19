package com.gwangya.purchase.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LockInfo {

	private final long userId;

	private final long threadId;
}
