package com.gwangya.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Email {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Column(name = "email", nullable = false)
    private String value;

    protected Email() {
    }

    public Email(final String email) {
        this.value = email;
    }

    public static Email of(final String email) {
        validateEmail(email);
        return new Email(email);
    }

    private static void validateEmail(final String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
