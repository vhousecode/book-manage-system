package com.book.borrow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Borrow Trend DTO
 */
@Data
@Schema(description = "Borrow Trend Data")
public class BorrowTrend implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Date list")
    private List<String> dates;

    @Schema(description = "Borrow count list")
    private List<Long> borrowCount;

    @Schema(description = "Return count list")
    private List<Long> returnCount;
}
