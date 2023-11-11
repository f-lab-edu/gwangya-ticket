package com.gwangya.user.domain.vo;

import com.gwangya.user.domain.User;
import com.gwangya.user.repository.InMemoryUserRepository;
import com.gwangya.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmailTest {

    UserRepository userRepository;

    final String existedEmail = "user@email.com";

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        User user = new User(new Email(existedEmail), new Password("Password123!"), LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user);
    }

    @DisplayName("회원 가입 시 이메일은 필수이다.")
    @ParameterizedTest
    @NullAndEmptySource
    void user_email_is_required(String nullOrEmptyEmail) {
        assertThatThrownBy(() -> Email.of(nullOrEmptyEmail, userRepository))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일은 필수입니다.");

    }

    @DisplayName("이메일 형식이 맞지 않을 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"emailtest.com", "email@testcom", "email//@test.com"})
    void user_email_should_be_formatted_correctly(String invalidEmail) {
        assertThatThrownBy(() -> Email.of(invalidEmail, userRepository))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이메일 형식이 올바르지 않습니다.");

    }
    
    @DisplayName("중복된 이메일은 사용할 수 없다.")
    @Test
    void duplicate_email_is_not_available() {
        assertThatThrownBy(() -> Email.of(existedEmail, userRepository))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("중복된 이메일 입니다.");
    }

}