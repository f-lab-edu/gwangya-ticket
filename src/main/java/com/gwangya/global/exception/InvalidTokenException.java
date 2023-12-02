package com.gwangya.global.exception;

import org.springframework.security.access.AccessDeniedException;

public class InvalidTokenException extends AccessDeniedException {

	private final String message;

	public InvalidTokenException(String message) {
		super(message);
		this.message = message;
	}
}
