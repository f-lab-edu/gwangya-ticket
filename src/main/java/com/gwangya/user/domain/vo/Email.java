package com.gwangya.user.domain.vo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.ObjectUtils;

import com.gwangya.user.repository.UserRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Email {

	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

	@Column(name = "email", nullable = false)
	private String value;

	protected Email() {
	}

	public Email(final String email) {
		this.value = email;
	}

	public static Email of(final String email, final UserRepository userRepository) {
		validateEmail(email);
		checkDuplicateEmail(email, userRepository);
		return new Email(email);
	}

	private static void validateEmail(final String email) {
		if (ObjectUtils.isEmpty(email)) {
			throw new IllegalArgumentException("이메일은 필수입니다.");
		}
		Matcher matcher = pattern.matcher(email);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
		}
	}

	private static void checkDuplicateEmail(final String email, final UserRepository userRepository) {
		if (userRepository.existsUserByEmail(email)) {
			throw new IllegalArgumentException("중복된 이메일 입니다.");
		}
	}
}
