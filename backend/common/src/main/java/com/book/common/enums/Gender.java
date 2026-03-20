package com.book.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Gender Enum
 */
@Getter
@AllArgsConstructor
public enum Gender {

    UNKNOWN(0, "Unknown"),
    MALE(1, "Male"),
    FEMALE(2, "Female");

    private final Integer code;
    private final String description;

    public static Gender getByCode(Integer code) {
        for (Gender gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return UNKNOWN;
    }
}
