package com.gwangya.global.filter;

import static com.gwangya.global.util.JwtUtil.*;
import static org.springframework.http.HttpHeaders.*;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gwangya.global.authentication.JwtAuthenticationToken;
import com.gwangya.global.exception.InvalidTokenException;
import com.gwangya.global.util.JwtUtil;
import com.gwangya.user.domain.User;
import com.gwangya.user.service.AuthService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_PREFIX = "Bearer ";

	private final AuthService authService;

	private final AccessDeniedHandler accessDeniedHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		try {

			String token = request.getHeader(AUTHORIZATION);
			validateAccessToken(token);

			JwtAuthenticationToken authToken = JwtUtil.parse(token);

			User user = authService.searchUserByUserId(authToken.getUserId());

			request.setAttribute(ACCESSIBLE_CONCERT_LIST, authToken.getAccessibleConcerts());
			request.setAttribute(USER_ID, user.getId());

			SecurityContextHolder.getContext().setAuthentication(authToken);

			filterChain.doFilter(request, response);

		} catch (InvalidTokenException exception) {
			accessDeniedHandler.handle(request, response, exception);
		}
	}

	private static void validateAccessToken(final String token) {
		if (ObjectUtils.isEmpty(token)) {
			throw new InvalidTokenException("토큰은 필수입니다.");
		}
		if (!token.startsWith(AUTHORIZATION_PREFIX)) {
			throw new InvalidTokenException("토큰이 유효하지 않습니다.");
		}
	}
}
