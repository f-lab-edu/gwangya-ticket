package com.gwangya.global.auth.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.dto.LoginDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmailPasswordAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws IOException {
		//
		// Cookie session = Arrays.stream(request.getCookies())
		// 	.filter(cookie -> cookie.getName().equals("JSESSIONID"))
		// 	.findFirst()
		// 	.orElseGet(() -> new Cookie("JSESSIONID", null));
		LoginDto loginResponse = new LoginDto("로그인 성공");

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.getWriter().write(ConvertUtil.convert(loginResponse));
	}
}
