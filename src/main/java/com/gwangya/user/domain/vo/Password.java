package com.gwangya.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Password {

    private static final String PASSWORD_REGEX = "^(?=.*[a-z][A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-=_+{}\\:;'\"|\\\\<>,./?]).{8,20}$";

    @Column(name = "password", nullable = false)
    private String value;

    protected Password() {
    }

    public Password(final String password) {
        this.value = password;
    }

    public static Password of(final String password, final PasswordEncoder encoder) {
        validatePassword(password);
        return new Password(encoder.encode(password));
    }

    private static void validatePassword(final String password) {
        if (Objects.isNull(password) || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("비밀번호는 알파벳 대소문자 및 숫자, 특수문자를 포함하여 8~20자리여야 합니다.");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(getValue(), password.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
