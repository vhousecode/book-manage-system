package com.book.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Result Code Enum
 */
@Getter
@AllArgsConstructor
public enum ResultCode {

    // Success
    SUCCESS(200, "Success"),

    // Client Errors 4xx
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    PARAM_VALID_ERROR(422, "Parameter Validation Error"),

    // Server Errors 5xx
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),

    // Business Errors 1xxx
    USER_NOT_FOUND(1001, "User Not Found"),
    USER_ALREADY_EXISTS(1002, "User Already Exists"),
    USER_DISABLED(1003, "User Disabled"),
    PASSWORD_ERROR(1004, "Password Error"),
    TOKEN_EXPIRED(1005, "Token Expired"),
    TOKEN_INVALID(1006, "Token Invalid"),

    BOOK_NOT_FOUND(2001, "Book Not Found"),
    BOOK_NOT_AVAILABLE(2002, "Book Not Available"),
    BOOK_STOCK_NOT_ENOUGH(2003, "Book Stock Not Enough"),

    BORROW_LIMIT_EXCEEDED(3001, "Borrow Limit Exceeded"),
    BORROW_RECORD_NOT_FOUND(3002, "Borrow Record Not Found"),
    BORROW_ALREADY_RETURNED(3003, "Book Already Returned"),
    BORROW_OVERDUE(3004, "Book Overdue"),
    RENEW_LIMIT_EXCEEDED(3005, "Renew Limit Exceeded");

    private final Integer code;
    private final String message;
}
