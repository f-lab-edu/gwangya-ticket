package com.gwangya.user.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.gwangya.user.domain.User;

public class InMemoryUserRepository implements UserRepository {

	private final Map<Long, User> users = new HashMap<>();

	@Override
	public User save(final User user) {
		Long id = Long.valueOf(users.size() + 1);
		users.put(id, user);
		return user;
	}

	@Override
	public boolean existsUserByEmail(final String email) {
		return users.values()
			.stream()
			.anyMatch(user -> user.getEmail().equals(email));
	}

	@Override
	public Optional<User> findByEmail(final String email) {
		return users.values()
			.stream()
			.filter(user -> user.getEmail().equals(email))
			.findFirst();
	}

}
