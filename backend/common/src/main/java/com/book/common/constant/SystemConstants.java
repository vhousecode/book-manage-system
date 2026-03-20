package com.book.common.constant;

/**
 * System Constants
 */
public interface SystemConstants {

    /**
     * Default page size
     */
    int DEFAULT_PAGE_SIZE = 10;

    /**
     * Max page size
     */
    int MAX_PAGE_SIZE = 100;

    /**
     * Token header name
     */
    String TOKEN_HEADER = "Authorization";

    /**
     * Token prefix
     */
    String TOKEN_PREFIX = "Bearer ";

    /**
     * User ID header (for internal service calls)
     */
    String USER_ID_HEADER = "X-User-Id";

    /**
     * Username header (for internal service calls)
     */
    String USERNAME_HEADER = "X-Username";
}
