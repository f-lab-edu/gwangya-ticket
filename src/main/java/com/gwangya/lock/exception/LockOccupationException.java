package com.gwangya.lock.exception;

import lombok.Getter;

@Getter
public class LockOccupationException extends RuntimeException {

	private final String message;

	private final String name;

	public LockOccupationException(String message, String name) {
		super(message);
		this.message = message;
		this.name = name;
	}
}
