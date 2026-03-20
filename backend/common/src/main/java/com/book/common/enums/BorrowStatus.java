package com.book.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Borrow Status Enum
 */
@Getter
@AllArgsConstructor
public enum BorrowStatus {

    BORROWED(0, "Borrowed"),
    RETURNED(1, "Returned"),
    OVERDUE(2, "Overdue"),
    LOST(3, "Lost");

    private final Integer code;
    private final String description;

    public static BorrowStatus getByCode(Integer code) {
        for (BorrowStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
