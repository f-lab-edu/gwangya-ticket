package com.gwangya.user.repository;

import org.springframework.stereotype.Repository;

import com.gwangya.user.domain.User;
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
	public Boolean existsUserByEmail(final String email) {
		return jpaRepository.existsUserByEmail(email);
	}
}