package com.gwangya.user.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gwangya.user.domain.User;
import com.gwangya.user.domain.vo.Email;
import com.gwangya.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	public User searchUserByEmail(final String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(Email.of(email))
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
		return user;
	}

	public List<Long> searchAccessibleConcertByUserId(final Long userId) {
		// 접근 가능한 공연 예매 목록 조회
		return List.of(1L, 2L, 3L);
	}
}
