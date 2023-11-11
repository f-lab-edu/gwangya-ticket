package com.gwangya.user.domain.vo;

import com.gwangya.global.config.InMemoryPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new InMemoryPasswordEncoder();
    }

    @DisplayName("회원 가입 시 비밀번호는 필수이다.")
    @ParameterizedTest
    @NullAndEmptySource
    void user_password_is_required(String nullOrEmptyPassword) {
        assertThatThrownBy(() -> Password.of(nullOrEmptyPassword, passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 필수입니다.");

    }

    @DisplayName("비밀번호는 알파벳 대소문자, 숫자, 특수문자를 포함해야 한다.")
    @ParameterizedTest
    @ValueSource(strings = {"test123!", "TEST123!", "Test!!!!!!", "Test1234"})
    void password_should_be_contain_alphabet_upper_case_and_lower_case_and_number_and_special_characters(String invalidPassword) {
        assertThatThrownBy(() -> Password.of(invalidPassword, passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("비밀번호는 알파벳 대소문자 및 숫자, 특수문자를 포함하여 8~20자리여야 합니다.");
    }

}