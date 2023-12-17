package com.gwangya.user.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.gwangya.user.domain.User;
import com.gwangya.user.domain.vo.Email;

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
	public Optional<User> findByEmail(final Email email) {
		return users.values()
			.stream()
			.filter(user -> user.getEmail().equals(email.getValue()))
			.findFirst();
	}

	@Override
	public boolean existsById(final Long userId) {
		return users.entrySet()
			.stream()
			.anyMatch(user -> user.getKey().equals(userId));
	}

	@Override
	public Optional<User> findById(Long userId) {
		Optional<Map.Entry<Long, User>> result = users.entrySet()
			.stream()
			.filter(user -> user.getKey().equals(userId))
			.findFirst();

		return Optional.ofNullable(result.get().getValue());
	}

}
