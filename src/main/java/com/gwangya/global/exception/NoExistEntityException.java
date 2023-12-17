package com.gwangya.global.exception;

public class NoExistEntityException extends RuntimeException {

	private final String message;

	public NoExistEntityException(String message) {
		super(message);
		this.message = message;
	}
}
