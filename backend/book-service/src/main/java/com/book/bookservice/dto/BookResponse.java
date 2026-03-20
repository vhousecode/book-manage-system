package com.book.bookservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Book Response DTO
 */
@Data
@Schema(description = "Book Response")
public class BookResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
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

    @Schema(description = "Category name")
    private String categoryName;

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

    @Schema(description = "Create time")
    private String createTime;
}
