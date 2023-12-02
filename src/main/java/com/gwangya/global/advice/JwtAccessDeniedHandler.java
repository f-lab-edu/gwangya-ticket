package com.gwangya.global.advice;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.gwangya.global.base.ExceptionResponse;
import com.gwangya.global.util.ConvertUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException {

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding("UTF-8");
		ExceptionResponse exceptionResponse = ExceptionResponse.builder()
			.message(accessDeniedException.getMessage())
			.build();
		response.getWriter().write(ConvertUtil.convert(exceptionResponse));
	}
}
