package com.gwangya.user.repository;

import com.gwangya.user.domain.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserRepository implements UserRepository {

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        Long id = Long.valueOf(users.size() + 1);
        users.put(id, user);
        return user;
    }

    @Override
    public Boolean existsUserByEmail(String email) {
        return users.values()
                .stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}
