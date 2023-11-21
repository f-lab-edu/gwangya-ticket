package com.gwangya.user.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
	USER("ROLE_USER");

	String authority;

	Authority(String authority) {
		this.authority = authority;
	}

	@Override
	public String getAuthority() {
		return this.name();
	}
}
