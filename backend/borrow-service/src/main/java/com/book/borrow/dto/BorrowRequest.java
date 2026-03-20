package com.book.borrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Borrow Request DTO
 */
@Data
@Schema(description = "Borrow Request")
public class BorrowRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Book ID")
    private Long bookId;

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Borrow days", example = "30")
    private Integer days = 30;
}
