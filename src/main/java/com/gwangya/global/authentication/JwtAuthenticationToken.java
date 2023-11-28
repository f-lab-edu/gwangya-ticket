package com.gwangya.global.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private Long userId;

	private String principal; // email

	private String credentials; // password

	private String accessToken;

	private String refreshToken;

	private List<Long> accessibleConcerts;

	protected JwtAuthenticationToken() {
		super(null);
	}

	private JwtAuthenticationToken(boolean authenticated, Collection<? extends GrantedAuthority> authorities,
		Long userId, String email, List<Long> accessibleConcerts, String password, String accessToken,
		String refreshToken) {
		super(authorities);
		super.setAuthenticated(authenticated);
		this.userId = userId;
		this.principal = email;
		this.credentials = password;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessibleConcerts = accessibleConcerts;
	}

	public static JwtAuthenticationToken authenticated(
		final Collection<? extends GrantedAuthority> authorities,
		final Long userId, final String email, final List<Long> accessibleConcerts, final String password,
		final String accessToken,
		final String refreshToken) {

		return new JwtAuthenticationToken(true, authorities, userId, email, accessibleConcerts, password, accessToken,
			refreshToken);
	}

	public static JwtAuthenticationToken unauthenticated(final String email, final String password) {
		return new JwtAuthenticationToken(false, null, null, email, null, password, null, null);
	}

	public static JwtAuthenticationToken of(final Collection<? extends GrantedAuthority> authorities,
		final Long userId, final String email, final List<Long> accessibleConcerts) {
		return new JwtAuthenticationToken(true, authorities, userId, email, accessibleConcerts, null, null, null);
	}
}