package com.book.borrow.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Borrow Record Entity
 */
@Data
@TableName("borrow_record")
@Schema(description = "Borrow Record Entity")
public class BorrowRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Primary key")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "Borrower user ID")
    private Long userId;

    @Schema(description = "Book ID")
    private Long bookId;

    @Schema(description = "Borrow date")
    private LocalDateTime borrowDate;

    @Schema(description = "Due date for return")
    private LocalDateTime dueDate;

    @Schema(description = "Actual return date")
    private LocalDateTime returnDate;

    @Schema(description = "Renew times count")
    private Integer renewCount;

    @Schema(description = "Status: 0-Borrowed, 1-Returned, 2-Overdue, 3-Lost")
    private Integer status;

    @Schema(description = "Remark")
    private String remark;

    @Schema(description = "Create time")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "Update time")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
