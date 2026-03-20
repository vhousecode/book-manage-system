package com.book.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Book Status Enum
 */
@Getter
@AllArgsConstructor
public enum BookStatus {

    OFF_SHELF(0, "Off Shelf"),
    ON_SHELF(1, "On Shelf");

    private final Integer code;
    private final String description;

    public static BookStatus getByCode(Integer code) {
        for (BookStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
