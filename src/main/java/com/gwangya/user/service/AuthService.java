package com.gwangya.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.domain.User;
import com.gwangya.user.domain.vo.Email;
import com.gwangya.user.dto.AuthDto;
import com.gwangya.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public AuthDto searchUserByEmail(final String email) {
		User user = userRepository.findByEmail(Email.of(email))
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다.", User.class, email));
		return ConvertUtil.convert(user, AuthDto.class);
	}

	public List<Long> searchAccessibleConcertByUserId(final Long userId) {
		// 접근 가능한 공연 예매 목록 조회
		return List.of(1L, 2L, 3L);
	}

	@Transactional(readOnly = true)
	public boolean existsByUserId(Long userId) {
		return userRepository.existsById(userId);
	}
}
