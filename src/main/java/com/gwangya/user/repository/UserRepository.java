package com.gwangya.user.repository;

import java.util.Optional;

import com.gwangya.user.domain.User;
import com.gwangya.user.domain.vo.Email;

public interface UserRepository {

	User save(User user);

	boolean existsUserByEmail(Email email);

	Optional<User> findByEmail(Email email);

	boolean existsById(long userId);

	Optional<User> findById(long userId);
}