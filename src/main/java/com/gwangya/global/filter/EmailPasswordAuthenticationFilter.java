package com.gwangya.global.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;

import com.gwangya.global.authentication.JwtAuthenticationToken;
import com.gwangya.global.exception.InvalidLoginException;
import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.dto.LoginCreateCommand;
import com.gwangya.user.dto.LoginDto;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EmailPasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

	private SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
		.getContextHolderStrategy();

	private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();

	private AuthenticationFailureHandler authenticationFailureHandler;
	private static final AntPathRequestMatcher PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/auth", "POST");
	private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_VALUE;

	public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
		AuthenticationEntryPoint authenticationEntryPoint) {
		super(PATH_REQUEST_MATCHER.getPattern(), authenticationManager);
		this.authenticationFailureHandler = new AuthenticationEntryPointFailureHandler(authenticationEntryPoint);
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

			JwtAuthenticationToken authRequest = JwtAuthenticationToken.builder()
				.authenticated(false)
				.email(email)
				.password(password)
				.build();

			return super.getAuthenticationManager().authenticate(authRequest);

		} catch (IOException e) {
			throw new InvalidLoginException("로그인에 실패했습니다");
		}
	}

	private void validateLoginRequest(final String email, final String password) {
		if (ObjectUtils.isEmpty(email) || ObjectUtils.isEmpty(password)) {
			throw new IllegalArgumentException("이메일과 비밀번호를 입력해주세요.");
		}
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException {
		SecurityContext context = this.securityContextHolderStrategy.createEmptyContext();
		context.setAuthentication(authResult);
		this.securityContextHolderStrategy.setContext(context);
		this.securityContextRepository.saveContext(context, request, response);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("UTF-8");

		JwtAuthenticationToken jwtAuthResult = (JwtAuthenticationToken)authResult;

		LoginDto loginDto = new LoginDto(jwtAuthResult.getAccessToken(), jwtAuthResult.getRefreshToken());
		response.getWriter().write(ConvertUtil.convert(loginDto));
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		this.authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
	}
}
