package com.book.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * User Status Enum
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    DISABLED(0, "Disabled"),
    ENABLED(1, "Enabled");

    private final Integer code;
    private final String description;

    public static UserStatus getByCode(Integer code) {
        for (UserStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
}
