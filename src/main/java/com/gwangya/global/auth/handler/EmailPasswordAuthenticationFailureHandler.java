package com.gwangya.global.auth.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.gwangya.global.base.ExceptionResponse;
import com.gwangya.global.util.ConvertUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmailPasswordAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private static final String FAIL_MESSAGE = "로그인에 실패했습니다.";

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException exception) throws IOException {

		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(FAIL_MESSAGE)
			.build();

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.getWriter().write(ConvertUtil.convert(exceptionResponse));
	}
}
