package com.book.gateway.filter;

import com.book.common.constant.SystemConstants;
import com.book.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Authentication Filter
 */
@Slf4j
@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Value("${jwt.secret:book-manage-system-jwt-secret-key-must-be-at-least-256-bits}")
    private String jwtSecret;

    /**
     * Paths that don't require authentication
     */
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/api/user/auth/login",
            "/api/user/auth/register",
            "/doc.html",
            "/webjars/**",
            "/swagger-resources/**",
            "/v3/api-docs/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // Skip authentication for excluded paths
        if (isExcludedPath(path)) {
            return chain.filter(exchange);
        }

        // Get token from header
        String token = request.getHeaders().getFirst(SystemConstants.TOKEN_HEADER);
        if (token == null || token.isEmpty()) {
            log.warn("Missing token for path: {}", path);
            return unauthorized(exchange);
        }

        // Remove "Bearer " prefix
        if (token.startsWith(SystemConstants.TOKEN_PREFIX)) {
            token = token.substring(SystemConstants.TOKEN_PREFIX.length());
        }

        // Validate token
        if (!JwtUtils.validateToken(token, jwtSecret)) {
            log.warn("Invalid token for path: {}", path);
            return unauthorized(exchange);
        }

        // Get user info from token and add to headers
        Long userId = JwtUtils.getUserId(token);
        String username = JwtUtils.getUsername(token);

        if (userId != null && username != null) {
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header(SystemConstants.USER_ID_HEADER, String.valueOf(userId))
                    .header(SystemConstants.USERNAME_HEADER, username)
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        }

        return chain.filter(exchange);
    }

    /**
     * Check if path is excluded from authentication
     */
    private boolean isExcludedPath(String path) {
        for (String excludePath : EXCLUDE_PATHS) {
            if (excludePath.endsWith("/**")) {
                String prefix = excludePath.substring(0, excludePath.length() - 3);
                if (path.startsWith(prefix)) {
                    return true;
                }
            } else if (path.equals(excludePath)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Return unauthorized response
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json;charset=UTF-8");

        String body = "{\"code\":401,\"message\":\"Unauthorized\",\"timestamp\":" + System.currentTimeMillis() + "}";
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
