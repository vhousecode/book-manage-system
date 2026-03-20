package com.book.common.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Page Request Parameters
 */
@Data
@Schema(description = "Page Request Parameters")
public class PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Page number", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "Page size", example = "10")
    private Integer pageSize = 10;

    @Schema(description = "Sort field")
    private String sortField;

    @Schema(description = "Sort order: asc/desc", example = "desc")
    private String sortOrder = "desc";

    /**
     * Get offset for MySQL LIMIT
     */
    public long getOffset() {
        return (long) (pageNum - 1) * pageSize;
    }

    /**
     * Convert to MyBatis-Plus Page object
     */
    public <T> com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> toPage() {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<T> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        if (sortField != null && !sortField.isEmpty()) {
            if ("asc".equalsIgnoreCase(sortOrder)) {
                page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.asc(sortField));
            } else {
                page.addOrder(com.baomidou.mybatisplus.core.metadata.OrderItem.desc(sortField));
            }
        }
        return page;
    }
}
