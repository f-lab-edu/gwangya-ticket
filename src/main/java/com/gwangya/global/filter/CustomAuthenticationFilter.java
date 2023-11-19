package com.gwangya.global.filter;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gwangya.global.auth.CustomAuthenticationSuccessHandler;
import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.dto.UserCreateCommand;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(
		"/api/v1/auth",
		"POST");

	private AuthenticationSuccessHandler successHandler = new CustomAuthenticationSuccessHandler();

	public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		StringBuffer stringBuffer = new StringBuffer();

		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}

			String requestBody = stringBuffer.toString();
			UserCreateCommand loginRequest = ConvertUtil.convert(requestBody, UserCreateCommand.class);
			UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
				loginRequest.getEmail(), loginRequest.getPassword());

			setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
