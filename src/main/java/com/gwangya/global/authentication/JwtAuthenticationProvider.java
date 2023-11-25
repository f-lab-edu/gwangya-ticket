package com.gwangya.global.authentication;

import static com.gwangya.user.domain.Authority.*;

import java.util.Collections;
import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gwangya.user.domain.User;
import com.gwangya.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final AuthService authService;

	private final PasswordEncoder passwordEncoder;

	@Override
	public JwtAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
		User user = authService.searchUserByEmail((String)authentication.getPrincipal());
		validatePassword((String)authentication.getCredentials(), user.getPassword());
		List<Long> accessibleConcert = authService.searchAccessibleConcertByUserId(user.getId());

		return JwtAuthenticationToken.builder()
			.authenticated(true)
			.authorities(Collections.singleton(USER))
			.email(user.getEmail())
			.password(user.getPassword())
			.accessToken(createAccessToken(user.getId(), accessibleConcert))
			.refreshToken(createRefreshToken(user.getId()))
			.build();
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

	public String createAccessToken(final Long userId, final List<Long> accessibleConcerts) {
		// 엑세스 토큰 발급
		return "access token";
	}

	public String createRefreshToken(final Long userId) {
		// 리프레시 토큰 발급
		return "refresh token";
	}
}
