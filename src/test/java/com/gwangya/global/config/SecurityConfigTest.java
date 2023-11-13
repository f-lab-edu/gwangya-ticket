package com.gwangya.global.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @DisplayName("비밀번호가 정상적으로 암호화 된다.")
    @Test
    void password_is_encrypt_successfully() {
        // given
        String password = "sohee408!";

        // when
        String result = passwordEncoder.encode(password);

        // then
        assertThat(result).contains("bcrypt");
        assertThat(passwordEncoder.matches("sohee408!", result)).isTrue();
    }
}