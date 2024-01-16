package com.gwangya.global.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gwangya.global.base.ExceptionResponse;
import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.performance.exception.UnavailablePurchaseException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleEntityException(EntityNotFoundException exception) {
		log.info("Message : {}, Entity : {}, RequestId : {}, Request : {}, RequestUserId : {}",
			exception.getMessage(),
			exception.getEntityType(),
			exception.getRequestId(),
			exception.getRequest(),
			exception.getRequestUserId()
		);
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UnavailablePurchaseException.class)
	public ResponseEntity<ExceptionResponse> handlePurchaseException(UnavailablePurchaseException exception) {
		log.info("Type : {}, Performance Detail PK : {}",
			exception.getType(),
			exception.getPerformanceDetailId()
		);
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.badRequest().body(exceptionResponse);
	}

	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<ExceptionResponse> handleClientException(RuntimeException exception) {
		log.info(exception.getMessage());
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.badRequest().body(exceptionResponse);
	}

	@ExceptionHandler({RuntimeException.class})
	public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exception) {
		log.info(exception.getMessage());
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.internalServerError().body(exceptionResponse);
	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<ExceptionResponse> handleServerException(Exception exception) {
		log.info(exception.getMessage());
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(exception.getMessage())
			.build();
		return ResponseEntity.internalServerError().body(exceptionResponse);
	}
}
