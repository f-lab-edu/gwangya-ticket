package com.gwangya.global.util;

import static com.gwangya.user.domain.Authority.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.gwangya.global.authentication.JwtAuthenticationToken;
import com.gwangya.global.exception.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
@PropertySource(value = "classpath:application.yml")
public final class JwtUtil {

	private static final SecretKey SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);

	public static final String USER_ID = "userId";
	public static final String ACCESSIBLE_CONCERT_LIST = "accessible";

	private static long ACCESS_TOKEN_DURATION;
	private static long REFRESH_TOKEN_DURATION;

	@Value("${jwt.duration.access}")
	public void setAccessTokenDuration(long accessTokenDuration) {
		ACCESS_TOKEN_DURATION = accessTokenDuration;
	}

	@Value("${jwt.duration.refresh}")
	public void setRefreshTokenDuration(long refreshTokenDuration) {
		REFRESH_TOKEN_DURATION = refreshTokenDuration;
	}

	public static String generateAccessToken(final String email, final Long userId,
		final List<Long> accessibleConcerts) {
		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> claims = new HashMap<>();
		claims.put(USER_ID, userId);
		claims.put(ACCESSIBLE_CONCERT_LIST, accessibleConcerts);

		return createAccessToken(claims, email, now, now.plusMinutes(ACCESS_TOKEN_DURATION));
	}

	public static String generateRefreshToken(final String email, final Long userId) {
		LocalDateTime now = LocalDateTime.now();
		Map<String, Object> claims = new HashMap<>();
		claims.put(USER_ID, userId);

		return createAccessToken(claims, email, now, now.plusDays(REFRESH_TOKEN_DURATION));
	}

	private static String createAccessToken(final Map<String, Object> claims, final String subject,
		final LocalDateTime issuedAt, final LocalDateTime expiration) {
		return Jwts.builder()
			.setClaims(claims)
			.setSubject(subject)
			.setIssuedAt(Timestamp.valueOf(issuedAt))
			.setExpiration(Timestamp.valueOf(expiration))
			.signWith(SECRET)
			.compact();
	}

	public static JwtAuthenticationToken parse(final String authorization) {
		String token = authorization.split(" ")[1];
		Claims claims = extractAllClaims(token);
		validateToken(claims);

		return JwtAuthenticationToken.builder()
			.authenticated(true)
			.authorities(Collections.singleton(USER))
			.userId(claims.get(USER_ID, Long.class))
			.email(claims.getSubject())
			.accessibleConcerts(claims.get(ACCESSIBLE_CONCERT_LIST, List.class))
			.build();
	}

	private static Claims extractAllClaims(final String token) {
		return Jwts.parserBuilder()
			.setSigningKey(SECRET.getEncoded())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	private static void validateToken(final Claims claims) {
		if (claims.getExpiration().before(Timestamp.valueOf(LocalDateTime.now()))) {
			throw new InvalidTokenException("토큰이 만료되었습니다.");
		}
	}
}
