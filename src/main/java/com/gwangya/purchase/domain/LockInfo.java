package com.gwangya.purchase.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LockInfo {

	private final long userId;

	private final long threadId;
}
