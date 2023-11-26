package com.gwangya.global.authentication;

import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private Long userId;

	private String principal; // email

	private String credentials; // password

	private String accessToken;

	private String refreshToken;

	private List<Long> accessibleConcerts;

	@Builder
	public JwtAuthenticationToken(boolean authenticated, Collection<? extends GrantedAuthority> authorities,
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

	public Long getUserId() {
		return userId;
	}

	@Override
	public Object getCredentials() {
		return this.credentials;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public List<Long> getAccessibleConcerts() {
		return accessibleConcerts;
	}
}