package com.gwangya.user.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    @DisplayName("회원 가입 시 이메일은 필수이다.")
    @ParameterizedTest
    @NullAndEmptySource
    void user_email_is_required(String nullOrEmptyEmail) {
        assertThatThrownBy(() -> Email.of(nullOrEmptyEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일은 필수입니다.");

    }

    @DisplayName("회원 가입 시 이메일은 필수이다.")
    @ParameterizedTest
    @ValueSource(strings = {"emailtest.com", "email@testcom", "email//@test.com"})
    void user_email_should_be_formatted_correctly(String invalidEmail) {
        assertThatThrownBy(() -> Email.of(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 형식이 올바르지 않습니다.");

    }

}