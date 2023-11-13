package com.gwangya.global.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
@PropertySource(value = "classpath:application.yml")
public class JwtUtil {

	@Value("${jwt.key}")
	private String secret;

	@Value("${jwt.expiration}")
	private long expirationTime;

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secret)
			.build()
			.parseClaimsJwt(token)
			.getBody();
	}

	private String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}

	private String createToken(Map<String, Object> claims, String subject) {
		LocalDateTime now = LocalDateTime.now();

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(Timestamp.valueOf(now))
			.setExpiration(Timestamp.valueOf(now.plusSeconds(expirationTime)))
			.signWith(SignatureAlgorithm.HS256, secret)
			.compact();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token)
			.before(Timestamp.valueOf(LocalDateTime.now()));
	}

	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
}
