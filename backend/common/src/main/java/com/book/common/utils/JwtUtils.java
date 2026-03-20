package com.book.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Utility Class
 */
@Slf4j
public class JwtUtils {

    private JwtUtils() {
    }

    /**
     * Default secret key (should be configured in application.yml)
     */
    private static final String DEFAULT_SECRET = "book-manage-system-jwt-secret-key-must-be-at-least-256-bits";

    /**
     * Default expiration time: 24 hours
     */
    private static final long DEFAULT_EXPIRATION = 24 * 60 * 60 * 1000L;

    /**
     * Get secret key
     */
    private static SecretKey getSecretKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Generate token
     *
     * @param userId   user ID
     * @param username username
     * @return token string
     */
    public static String generateToken(Long userId, String username) {
        return generateToken(userId, username, DEFAULT_SECRET, DEFAULT_EXPIRATION);
    }

    /**
     * Generate token with custom secret and expiration
     *
     * @param userId     user ID
     * @param username   username
     * @param secret     secret key
     * @param expiration expiration time in milliseconds
     * @return token string
     */
    public static String generateToken(Long userId, String username, String secret, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSecretKey(secret))
                .compact();
    }

    /**
     * Parse token to get claims
     *
     * @param token token string
     * @return claims
     */
    public static Claims parseToken(String token) {
        return parseToken(token, DEFAULT_SECRET);
    }

    /**
     * Parse token with custom secret
     *
     * @param token  token string
     * @param secret secret key
     * @return claims
     */
    public static Claims parseToken(String token, String secret) {
        try {
            return Jwts.parser()
                    .verifyWith(getSecretKey(secret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Failed to parse token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get user ID from token
     *
     * @param token token string
     * @return user ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            Object userId = claims.get("userId");
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            } else if (userId instanceof Long) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * Get username from token
     *
     * @param token token string
     * @return username
     */
    public static String getUsername(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * Validate token
     *
     * @param token token string
     * @return true if valid
     */
    public static boolean validateToken(String token) {
        return validateToken(token, DEFAULT_SECRET);
    }

    /**
     * Validate token with custom secret
     *
     * @param token  token string
     * @param secret secret key
     * @return true if valid
     */
    public static boolean validateToken(String token, String secret) {
        try {
            Claims claims = parseToken(token, secret);
            return claims != null && !isTokenExpired(claims);
        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Check if token is expired
     *
     * @param claims claims
     * @return true if expired
     */
    private static boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * Check if token is about to expire (within 30 minutes)
     *
     * @param token token string
     * @return true if about to expire
     */
    public static boolean isTokenAboutToExpire(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return true;
        }
        Date expiration = claims.getExpiration();
        long diff = expiration.getTime() - System.currentTimeMillis();
        return diff < 30 * 60 * 1000L;
    }
}
