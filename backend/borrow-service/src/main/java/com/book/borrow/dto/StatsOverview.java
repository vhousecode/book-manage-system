package com.book.borrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * Statistics Overview DTO
 */
@Data
@Schema(description = "Statistics Overview")
public class StatsOverview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Total books")
    private Long totalBooks;

    @Schema(description = "Total users")
    private Long totalUsers;

    @Schema(description = "Total borrowed books")
    private Long totalBorrowed;

    @Schema(description = "Total overdue books")
    private Long totalOverdue;

    @Schema(description = "Today borrow count")
    private Long todayBorrow;

    @Schema(description = "Today return count")
    private Long todayReturn;

    @Schema(description = "Available books")
    private Long availableBooks;
}
