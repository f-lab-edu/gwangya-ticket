package com.gwangya.global.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.gwangya.global.auth.filter.EmailPasswordAuthenticationFilter;
import com.gwangya.global.auth.handler.EmailPasswordAuthenticationFailureHandler;
import com.gwangya.global.auth.handler.EmailPasswordAuthenticationSuccessHandler;
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
			.csrf(CsrfConfigurer::disable)
			.formLogin(formLoginConfigurer -> new FormLoginConfigurer<>())
			.securityContext(context -> {
				context.securityContextRepository(securityContextRepository());
			})
			.addFilter(authenticationFilter())
			.authorizeHttpRequests(
				requestMatcher -> requestMatcher.requestMatchers(HttpMethod.POST, "/api/v1/auth/**",
						"/api/v1/user")
					.permitAll()
					.anyRequest().authenticated())
			.build();
	}

	@Bean
	public EmailPasswordAuthenticationFilter authenticationFilter() {
		EmailPasswordAuthenticationFilter filter = new EmailPasswordAuthenticationFilter(
			authenticationManager(),
			new EmailPasswordAuthenticationSuccessHandler(), new EmailPasswordAuthenticationFailureHandler());
		return filter;
	}

	@Bean
	public ProviderManager authenticationManager() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(authService);
		authenticationProvider.setPasswordEncoder(encoder());

		return new ProviderManager(authenticationProvider);
	}

	@Bean
	public SecurityContextRepository securityContextRepository() {
		return new HttpSessionSecurityContextRepository();
	}
}

