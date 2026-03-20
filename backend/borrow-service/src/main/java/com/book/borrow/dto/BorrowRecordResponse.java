package com.book.borrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Borrow Record Response DTO
 */
@Data
@Schema(description = "Borrow Record Response")
public class BorrowRecordResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Record ID")
    private Long id;

    @Schema(description = "User ID")
    private Long userId;

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Book ID")
    private Long bookId;

    @Schema(description = "Book title")
    private String bookTitle;

    @Schema(description = "Book cover")
    private String bookCover;

    @Schema(description = "Borrow date")
    private LocalDateTime borrowDate;

    @Schema(description = "Due date")
    private LocalDateTime dueDate;

    @Schema(description = "Return date")
    private LocalDateTime returnDate;

    @Schema(description = "Renew count")
    private Integer renewCount;

    @Schema(description = "Status")
    private Integer status;

    @Schema(description = "Overdue days")
    private Integer overdueDays;

    @Schema(description = "Fine amount")
    private BigDecimal fine;

    @Schema(description = "Remark")
    private String remark;
}
