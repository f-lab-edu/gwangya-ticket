package com.gwangya.global.advice;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.gwangya.global.base.ExceptionResponse;
import com.gwangya.global.util.ConvertUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityExceptionHandleEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setCharacterEncoding("UTF-8");
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(authException.getMessage())
			.build();
		response.getWriter().write(ConvertUtil.convert(exceptionResponse));
	}
}
