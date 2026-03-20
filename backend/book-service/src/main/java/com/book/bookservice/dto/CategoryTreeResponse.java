package com.book.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Category Tree Response DTO
 */
@Data
@Schema(description = "Category Tree Response")
public class CategoryTreeResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Category ID")
    private Long id;

    @Schema(description = "Category name")
    private String name;

    @Schema(description = "Parent category ID")
    private Long parentId;

    @Schema(description = "Sort order")
    private Integer sort;

    @Schema(description = "Category icon")
    private String icon;

    @Schema(description = "Status")
    private Integer status;

    @Schema(description = "Children categories")
    private List<CategoryTreeResponse> children;
}
