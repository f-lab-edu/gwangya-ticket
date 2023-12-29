package com.gwangya.global.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

	private String message;

	private Class entityType;

	private Long requestId;

	private String request;

	private Long requestUserId;

	public EntityNotFoundException(String message, Class entityType, Long requestId, Long requestUserId) {
		super(message);
		this.message = message;
		this.entityType = entityType;
		this.requestId = requestId;
		this.requestUserId = requestUserId;
	}

	public EntityNotFoundException(String message, Class entityType, Long requestId) {
		super(message);
		this.message = message;
		this.entityType = entityType;
		this.requestId = requestId;
	}

	public EntityNotFoundException(String message, Class entityType, String request) {
		super(message);
		this.message = message;
		this.entityType = entityType;
		this.request = request;
	}
}
