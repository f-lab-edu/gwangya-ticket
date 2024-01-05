package com.gwangya.user.domain;

import java.time.LocalDateTime;

import com.gwangya.user.domain.vo.Email;
import com.gwangya.user.domain.vo.Password;

public class UserFixture {

	public static User createUser(Long id) {
		return new User(id, new Email("test@email.com"), new Password("password"), LocalDateTime.now(),
			LocalDateTime.now());
	}
}
