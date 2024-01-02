package com.gwangya.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginDto {

	private final String accessToken;
	private final String refreshToken;
}
