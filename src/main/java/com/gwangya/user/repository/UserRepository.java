package com.gwangya.user.repository;

import com.gwangya.user.domain.User;

public interface UserRepository {

	User save(User user);

	boolean existsUserByEmail(String email);
}