package com.gwangya.global.exception;

public class InvalidLoginException extends RuntimeException {

	private final String message;

	public InvalidLoginException(String message) {
		super(message);
		this.message = message;
	}
}
