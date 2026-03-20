package com.book.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Role Response DTO
 */
@Data
@Schema(description = "Role Response")
public class RoleResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Role ID")
    private Long id;

    @Schema(description = "Role display name")
    private String roleName;

    @Schema(description = "Role key")
    private String roleKey;

    @Schema(description = "Role description")
    private String description;

    @Schema(description = "Status")
    private Integer status;

    @Schema(description = "Sort order")
    private Integer sort;

    @Schema(description = "Create time")
    private String createTime;
}
