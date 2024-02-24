package com.gwangya.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gwangya.user.domain.User;
import com.gwangya.user.domain.vo.Email;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	boolean existsUserByEmail(Email email);

	Optional<User> findByEmail(Email email);
}