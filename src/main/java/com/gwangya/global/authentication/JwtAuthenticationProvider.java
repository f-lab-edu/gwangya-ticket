package com.gwangya.global.authentication;

import static com.gwangya.user.domain.Authority.*;

import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gwangya.global.util.JwtUtil;
import com.gwangya.user.dto.AuthDto;
import com.gwangya.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final AuthService authService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public JwtAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
		AuthDto user = authService.searchUserByEmail((String)authentication.getPrincipal());
		validatePassword((String)authentication.getCredentials(), user.getPassword());
		List<Long> accessibleConcerts = authService.searchAccessibleConcertByUserId(user.getId());

		return JwtAuthenticationToken.authenticated(Collections.singleton(USER), user.getId(), user.getEmail(),
			accessibleConcerts, user.getPassword(),
			JwtUtil.generateAccessToken(user.getEmail(), user.getId(), accessibleConcerts),
			JwtUtil.generateRefreshToken(user.getEmail(), user.getId()));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

	private void validatePassword(final String rawPassword, final String encodedPassword) {
		if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
			throw new BadCredentialsException("비밀번호를 확인해주세요.");
		}
	}
}
