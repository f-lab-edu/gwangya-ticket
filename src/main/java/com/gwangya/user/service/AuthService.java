package com.gwangya.user.service;

import java.util.Collections;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gwangya.user.domain.Authority;
import com.gwangya.user.domain.User;
import com.gwangya.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
			Collections.singleton(Authority.USER));
	}
}
