package com.gwangya.user.repository;

import com.gwangya.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    Boolean existsUserByEmail(String email);
}