package com.{{company}}.{{project}}.common.result;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * Response Code Enumeration
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    
    SUCCESS(200, "Success"),
    PARAM_ERROR(400, "Parameter error"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    BUSINESS_ERROR(500, "Business error"),
    
    // User related errors (1000-1099)
    USER_NOT_FOUND(1001, "User not found"),
    USER_DISABLED(1002, "User is disabled"),
    USERNAME_EXISTS(1003, "Username already exists"),
    PASSWORD_ERROR(1004, "Password error"),
    
    // Book related errors (2000-2099)
    BOOK_NOT_FOUND(2001, "Book not found"),
    BOOK_OUT_OF_STOCK(2002, "Book out of stock"),
    
    // Borrow related errors (3000-3099)
    BORROW_LIMIT_EXCEEDED(3001, "Borrow limit exceeded"),
    BOOK_ALREADY_BORROWED(3002, "Book already borrowed"),
    BORROW_NOT_FOUND(3003, "Borrow record not found"),
    RENEW_LIMIT_EXCEEDED(3004, "Renew limit exceeded");

    private final Integer code;
    private final String message;
}
