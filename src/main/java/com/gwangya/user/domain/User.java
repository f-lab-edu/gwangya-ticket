package com.gwangya.user.domain;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.user.domain.vo.Email;
import com.gwangya.user.domain.vo.Password;
import com.gwangya.user.dto.UserCreateCommand;
import com.gwangya.user.repository.UserRepository;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USER")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Embedded
	private Email email;

	@Embedded
	private Password password;

	protected User() {
	}

	public User(Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.email = email;
		this.password = password;
	}

	public static User of(final UserCreateCommand userCreateCommand, final PasswordEncoder passwordEncoder,
		final UserRepository userRepository) {
		return new User(
			Email.of(userCreateCommand.getEmail(), userRepository),
			Password.of(userCreateCommand.getPassword(), passwordEncoder),
			LocalDateTime.now(),
			LocalDateTime.now()
		);
	}

	protected User(Long id, Email email, Password password, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email.getValue();
	}

	public String getPassword() {
		return password.getValue();
	}
}
