package com.book.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User Response DTO
 */
@Data
@Schema(description = "User Response")
public class UserResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "User ID")
    private Long id;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Display name")
    private String nickname;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "Email address")
    private String email;

    @Schema(description = "Avatar URL")
    private String avatar;

    @Schema(description = "Gender: 0-Unknown, 1-Male, 2-Female")
    private Integer gender;

    @Schema(description = "Status: 0-Disabled, 1-Enabled")
    private Integer status;

    @Schema(description = "Create time")
    private String createTime;

    @Schema(description = "Role list")
    private List<RoleResponse> roles;

    @Schema(description = "Role ID list")
    private List<Long> roleIds;
}
