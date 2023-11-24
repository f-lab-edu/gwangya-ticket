package com.gwangya.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class UserAuthController {

	@GetMapping("/")
	public ResponseEntity<String> loginSuccessResult() {
		return ResponseEntity.ok("로그인 성공");
	}

	@GetMapping("/api/v1/user/authority")
	public ResponseEntity<String> searchLoginUserAuthority(HttpServletRequest request) {
		return ResponseEntity.ok(request.getUserPrincipal().getName());
	}
}
