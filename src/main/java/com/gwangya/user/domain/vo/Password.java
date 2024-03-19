package com.gwangya.user.domain.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Password {

	private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()\\-=_+{}\\:;'\"|\\\\<>,./?]).{8,20}$";
	private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);

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
		if (ObjectUtils.isEmpty(password)) {
			throw new IllegalArgumentException("비밀번호는 필수입니다.");
		}
		Matcher matcher = pattern.matcher(password);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("비밀번호는 알파벳 대소문자 및 숫자, 특수문자를 포함하여 8~20자리여야 합니다.");
		}
	}
}
