package com.book.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Role Entity
 */
@Data
@TableName("sys_role")
@Schema(description = "Role Entity")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "Role display name")
    private String roleName;

    @Schema(description = "Role key for permission check")
    private String roleKey;

    @Schema(description = "Role description")
    private String description;

    @Schema(description = "Status: 0-Disabled, 1-Enabled")
    private Integer status;

    @Schema(description = "Sort order")
    private Integer sort;

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
