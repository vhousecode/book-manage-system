package com.book.borrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Hot Book DTO
 */
@Data
@Schema(description = "Hot Book")
public class HotBook implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Rank")
    private Integer rank;

    @Schema(description = "Book ID")
    private Long bookId;

    @Schema(description = "Book title")
    private String title;

    @Schema(description = "Author")
    private String author;

    @Schema(description = "Cover")
    private String cover;

    @Schema(description = "Borrow count")
    private Long borrowCount;
}
