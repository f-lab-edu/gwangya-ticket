package com.gwangya.global.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidLoginException extends AuthenticationException {

	private final String message;

	public InvalidLoginException(String message) {
		super(message);
		this.message = message;
	}
}
