package com.gwangya.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AuthDto {

	private final Long id;

	private final String email;

	private final String password;
}
