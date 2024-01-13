package com.gwangya.performance.exception;

import lombok.Getter;

@Getter
public class UnavailablePurchaseException extends RuntimeException {

	private final String message;

	private final UnavailablePurchaseType type;

	private final long performanceDetailId;

	public UnavailablePurchaseException(String message, UnavailablePurchaseType type, long performanceDetailId) {
		super(message);
		this.message = message;
		this.type = type;
		this.performanceDetailId = performanceDetailId;
	}
}
