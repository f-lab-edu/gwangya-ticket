package com.gwangya.global.config;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gwangya.user.service.AuthService;

@SpringBootTest(classes = {SecurityConfig.class, AuthService.class})
class SecurityConfigTest {

	@Autowired
	PasswordEncoder passwordEncoder;

	@MockBean
	AuthService authService;

	@DisplayName("비밀번호가 정상적으로 암호화 된다.")
	@Test
	void password_is_encrypt_successfully() {
		// given
		String password = "sohee408!";

		// when
		String result = passwordEncoder.encode(password);

		// then
		assertThat(result).contains("$2a$");
		assertThat(passwordEncoder.matches("sohee408!", result)).isTrue();
	}
}