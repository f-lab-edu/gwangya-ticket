package com.gwangya.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.gwangya.user.dto.UserCreateCommand;
import com.gwangya.user.dto.UserDto;
import com.gwangya.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserCommandController {

	private final UserService userService;

	@PostMapping("/api/v1/user")
	public ResponseEntity<UserDto> createUser(@RequestBody UserCreateCommand userCreateCommand) {
		return ResponseEntity.ok(userService.createUser(userCreateCommand));
	}
}
