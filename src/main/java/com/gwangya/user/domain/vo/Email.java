package com.gwangya.user.domain.vo;

import com.gwangya.global.util.RegexUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Email {

    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Column(name = "email", nullable = false)
    private String email;

    protected Email() {
    }

    public Email(final String email) {
        this.email = email;
    }

    public static Email of(final String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!RegexUtil.checkRegex(email, EMAIL_REGEX)) {
            throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
        }
        return new Email(email);
    }

    public String getEmail() {
        return email;
    }
}
