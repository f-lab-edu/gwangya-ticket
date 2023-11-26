package com.gwangya.global.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
@PropertySource(value = "classpath:application.yml")
public final class JwtUtil {

	private static final SecretKey SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	private static long TOKEN_DURATION;

	@Value("${jwt.duration}")
	public static void setExpirationTime(long duration) {
		JwtUtil.TOKEN_DURATION = duration;
	}

	public static String generateToken(final String email, final Long userId, final List<Long> accessibleConcerts) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("userId", userId);
		claims.put("accessible", accessibleConcerts);
		return createAccessToken(claims, email);
	}

	private static String createAccessToken(final Map<String, Object> claims, final String subject) {
		LocalDateTime now = LocalDateTime.now();

		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(Timestamp.valueOf(now))
			.setExpiration(Timestamp.valueOf(now.plusSeconds(TOKEN_DURATION)))
			.signWith(SECRET)
			.compact();
	}
}
