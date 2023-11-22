package com.gwangya.global.auth.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import com.gwangya.global.auth.exception.InvalidLoginException;
import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.dto.LoginCreateCommand;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private static final String LOGIN_USERNAME_KEY = "email";
	private static final AntPathRequestMatcher PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/auth", "POST");
	private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

	public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
		AuthenticationSuccessHandler successHandler,
		AuthenticationFailureHandler failureHandler) {
		super.setFilterProcessesUrl(PATH_REQUEST_MATCHER.getPattern());
		super.setUsernameParameter(LOGIN_USERNAME_KEY);
		super.setAuthenticationManager(authenticationManager);
		setAuthenticationSuccessHandler(successHandler);
		setAuthenticationFailureHandler(failureHandler);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {

		if (ObjectUtils.isEmpty(request.getContentType()) || !request.getContentType().equals(CONTENT_TYPE)) {
			throw new AuthenticationServiceException("잘못된 로그인 요청 형식입니다.");
		}

		try {
			LoginCreateCommand loginRequest = ConvertUtil.convert(
				StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), LoginCreateCommand.class);

			String email = loginRequest.getEmail();
			String password = loginRequest.getPassword();
			validateLoginRequest(email, password);

			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email,
				password);

			setDetails(request, authRequest);
			return super.getAuthenticationManager().authenticate(authRequest);

		} catch (IOException e) {
			throw new InvalidLoginException("로그인에 실패했습니다");
		}
	}

	private void validateLoginRequest(final String email, final String password) {
		if (ObjectUtils.isEmpty(email) || ObjectUtils.isEmpty(password)) {
			throw new IllegalArgumentException("이메일과 비밀번호를 입력해주세요.");
		}
	}
}
