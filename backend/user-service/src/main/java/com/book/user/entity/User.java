package com.book.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User Entity
 */
@Data
@TableName("sys_user")
@Schema(description = "User Entity")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "Username for login")
    private String username;

    @Schema(description = "Password (encrypted)")
    private String password;

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

    @Schema(description = "Soft delete flag")
    @TableLogic
    private Integer deleted;

    @Schema(description = "Create time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "Update time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
