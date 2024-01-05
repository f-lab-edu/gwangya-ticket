package com.gwangya.performance.exception;

public class UnavailablePurchaseException extends RuntimeException {

	private final String message;

	public UnavailablePurchaseException(String message) {
		super(message);
		this.message = message;
	}
}
