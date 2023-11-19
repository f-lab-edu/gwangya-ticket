package com.gwangya.user.repository;

import java.util.Optional;

import com.gwangya.user.domain.User;

public interface UserRepository {

	User save(User user);

	boolean existsUserByEmail(String email);

	Optional<User> findByEmail(String email);
}