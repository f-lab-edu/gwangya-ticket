package com.gwangya.user.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.gwangya.user.domain.User;
import com.gwangya.user.domain.vo.Email;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final UserJpaRepository jpaRepository;

	@Override
	public User save(final User user) {
		return jpaRepository.save(user);
	}

	@Override
	public boolean existsUserByEmail(final Email email) {
		return jpaRepository.existsUserByEmail(email);
	}

	@Override
	public Optional<User> findByEmail(final Email email) {
		return jpaRepository.findByEmail(email);
	}

	@Override
	public boolean existsById(long userId) {
		return jpaRepository.existsById(userId);
	}

	@Override
	public Optional<User> findById(long userId) {
		return jpaRepository.findById(userId);
	}
}