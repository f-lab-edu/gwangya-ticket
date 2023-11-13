package com.gwangya.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gwangya.user.domain.User;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	boolean existsUserByEmail(String email);
}