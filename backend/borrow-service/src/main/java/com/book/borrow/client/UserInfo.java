package com.book.borrow.client;

import lombok.Data;

import java.io.Serializable;

/**
 * User Info DTO (for Feign response)
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private String nickname;
}
