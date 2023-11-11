package com.gwangya.user.service;

import com.gwangya.global.util.ConvertUtil;
import com.gwangya.user.domain.User;
import com.gwangya.user.repository.UserRepository;
import com.gwangya.user.dto.UserCreateCommand;
import com.gwangya.user.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserDto createUser(final UserCreateCommand userCreateCommand) {
        User savedUser = userRepository.save(User.of(userCreateCommand, passwordEncoder, userRepository));
        return ConvertUtil.convert(savedUser, UserDto.class);
    }
}