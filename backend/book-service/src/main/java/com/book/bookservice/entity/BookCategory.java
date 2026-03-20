package com.book.bookservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Book Category Entity
 */
@Data
@TableName("book_category")
@Schema(description = "Book Category Entity")
public class BookCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "Category name")
    private String name;

    @Schema(description = "Parent category ID, 0 for root")
    private Long parentId;

    @Schema(description = "Sort order")
    private Integer sort;

    @Schema(description = "Category icon")
    private String icon;

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
