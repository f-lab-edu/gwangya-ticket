package com.gwangya.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.gwangya.global.advice.JwtAccessDeniedHandler;
import com.gwangya.global.advice.SecurityExceptionHandleEntryPoint;
import com.gwangya.global.authentication.JwtAuthenticationProvider;
import com.gwangya.global.filter.EmailPasswordAuthenticationFilter;
import com.gwangya.global.filter.JwtTokenFilter;
import com.gwangya.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthService authService;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(CsrfConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			.requestCache(RequestCacheConfigurer::disable)
			.authenticationManager(authenticationManager())
			.logout(LogoutConfigurer::disable)
			.sessionManagement(
				sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterAt(new EmailPasswordAuthenticationFilter(authenticationManager(), authenticationEntryPoint()),
				UsernamePasswordAuthenticationFilter.class)
			.addFilterAt(new JwtTokenFilter(authService, accessDeniedHandler()),
				UsernamePasswordAuthenticationFilter.class)
			.authorizeHttpRequests(
				requestMatcher -> requestMatcher.requestMatchers(HttpMethod.POST, "/api/v1/user", "/api/v1/auth")
					.permitAll()
					.anyRequest().authenticated())
			.build();
	}

	@Bean
	public ProviderManager authenticationManager() {
		JwtAuthenticationProvider authenticationProvider = new JwtAuthenticationProvider(authService, encoder());
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new SecurityExceptionHandleEntryPoint();
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return new JwtAccessDeniedHandler();
	}
}

