package com.gwangya.global.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gwangya.global.base.ExceptionResponse;
import com.gwangya.global.exception.InvalidLoginException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({IllegalArgumentException.class, InvalidLoginException.class})
	public ResponseEntity<?> handleClientException(RuntimeException exception) {
		log.info(exception.getMessage());
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.badRequest().body(exceptionResponse);
	}

	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
		log.info(exception.getMessage());
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.internalServerError().body(exceptionResponse);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<?> handleServerException(Exception exception) {
		log.info(exception.getMessage());
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.internalServerError().body(exceptionResponse);
	}
}
