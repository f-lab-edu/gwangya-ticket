package com.gwangya.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.NoExistEntityException;
import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.domain.User;
import com.gwangya.user.dto.UserCreateCommand;
import com.gwangya.user.dto.UserDto;
import com.gwangya.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;

	@Transactional
	public UserDto createUser(final UserCreateCommand userCreateCommand) {
		User savedUser = userRepository.save(User.of(userCreateCommand, passwordEncoder, userRepository));
		return ConvertUtil.convert(savedUser, UserDto.class);
	}

	@Transactional(readOnly = true)
	public UserDto searchUserById(final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoExistEntityException("존재하지 않는 유저입니다."));
		return ConvertUtil.convert(user, UserDto.class);
	}
}