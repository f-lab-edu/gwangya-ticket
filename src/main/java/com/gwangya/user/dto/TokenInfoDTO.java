package com.gwangya.user.dto;

import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class TokenInfoDTO {

	private Long userId;
	private List<Long> accessibleConcerts;

	public TokenInfoDTO(Long userId) {
		this.userId = userId;
	}

	public TokenInfoDTO(Long userId, List<Long> accessibleConcerts) {
		this.userId = userId;
		this.accessibleConcerts = accessibleConcerts;
	}
}
