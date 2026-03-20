package com.book.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * User-Role Relation Entity
 */
@Data
@TableName("sys_user_role")
@Schema(description = "User-Role Relation Entity")
public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Role ID")
    private Long roleId;

    @Schema(description = "Create time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
