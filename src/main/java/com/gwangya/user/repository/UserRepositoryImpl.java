package com.gwangya.user.repository;

import com.gwangya.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private final UserJpaRepository jpaRepository;

    public UserRepositoryImpl(JPAQueryFactory jpaQueryFactory, UserJpaRepository jpaRepository) {
        this.jpaQueryFactory = jpaQueryFactory;
        this.jpaRepository = jpaRepository;
    }


    @Override
    public User save(final User user) {
        return jpaRepository.save(user);
    }

    @Override
    public Boolean existsUserByEmail(final String email) {
        return jpaRepository.existsUserByEmail(email);
    }
}