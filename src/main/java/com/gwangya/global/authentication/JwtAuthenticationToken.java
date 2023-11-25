package com.gwangya.global.authentication;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Builder;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private String principal; // email

	private String credentials; // password

	private String accessToken;

	private String refreshToken;

	@Builder
	public JwtAuthenticationToken(boolean authenticated, Collection<? extends GrantedAuthority> authorities,
		String email, String password, String accessToken, String refreshToken) {
		super(authorities);
		super.setAuthenticated(authenticated);
		this.principal = email;
		this.credentials = password;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
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
}