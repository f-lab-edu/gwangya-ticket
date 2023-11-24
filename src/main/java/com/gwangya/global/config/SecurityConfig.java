package com.gwangya.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.gwangya.user.domain.Authority;
import com.gwangya.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

	private final AuthService authService;

	@Bean
	public PasswordEncoder encoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(Customizer.withDefaults())
			.formLogin(formLoginConfigurer -> formLoginConfigurer.usernameParameter("email"))
			.authenticationManager(authenticationManager())
			.authorizeHttpRequests(
				requestMatcher -> requestMatcher.requestMatchers(HttpMethod.POST, "/api/v1/user")
					.permitAll()
					.requestMatchers(HttpMethod.GET, "/api/v1/user/authority")
					.hasAuthority(Authority.USER.name())
					.anyRequest().authenticated())
			.build();
	}

	@Bean
	public ProviderManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(authService);
		authenticationProvider.setPasswordEncoder(encoder());

		return new ProviderManager(authenticationProvider);
	}
}

