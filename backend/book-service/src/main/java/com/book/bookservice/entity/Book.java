package com.book.bookservice.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Book Entity
 */
@Data
@TableName("book_info")
@Schema(description = "Book Entity")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "Book title")
    private String title;

    @Schema(description = "Book author")
    private String author;

    @Schema(description = "ISBN number")
    private String isbn;

    @Schema(description = "Publisher")
    private String publisher;

    @Schema(description = "Publish date")
    private LocalDate publishDate;

    @Schema(description = "Category ID")
    private Long categoryId;

    @Schema(description = "Book price")
    private BigDecimal price;

    @Schema(description = "Stock quantity")
    private Integer stock;

    @Schema(description = "Available stock for borrowing")
    private Integer availableStock;

    @Schema(description = "Book shelf location")
    private String location;

    @Schema(description = "Cover image URL")
    private String cover;

    @Schema(description = "Book description")
    private String description;

    @Schema(description = "Status: 0-Off shelf, 1-On shelf")
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
