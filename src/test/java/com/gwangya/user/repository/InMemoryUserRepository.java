package com.gwangya.user.repository;

import java.util.HashMap;
import java.util.Map;

import com.gwangya.user.domain.User;

public class InMemoryUserRepository implements UserRepository {

	private final Map<Long, User> users = new HashMap<>();

	@Override
	public User save(User user) {
		Long id = Long.valueOf(users.size() + 1);
		users.put(id, user);
		return user;
	}

	@Override
	public boolean existsUserByEmail(String email) {
		return users.values()
			.stream()
			.anyMatch(user -> user.getEmail().equals(email));
	}
}
