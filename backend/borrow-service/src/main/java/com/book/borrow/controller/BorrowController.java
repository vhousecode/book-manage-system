package com.book.borrow.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.common.constant.SystemConstants;
import com.book.common.result.PageResult;
import com.book.common.result.Result;
import com.book.borrow.dto.*;
import com.book.borrow.service.BorrowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Borrow Controller
 */
@Tag(name = "Borrow Management", description = "Borrow management APIs")
@RestController
@RequestMapping("/api/borrow")
@RequiredArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;

    @Operation(summary = "Borrow a book")
    @PostMapping
    public Result<BorrowRecordResponse> borrowBook(@RequestBody BorrowRequest request) {
        return Result.success(borrowService.borrowBook(request));
    }

    @Operation(summary = "Return a book")
    @PostMapping("/return")
    public Result<BorrowRecordResponse> returnBook(@RequestParam Long recordId) {
        return Result.success(borrowService.returnBook(recordId));
    }

    @Operation(summary = "Renew a book")
    @PostMapping("/renew")
    public Result<BorrowRecordResponse> renewBook(@RequestParam Long recordId,
                                                   @RequestParam(required = false) Integer days) {
        return Result.success(borrowService.renewBook(recordId, days));
    }

    @Operation(summary = "Get borrow records with pagination")
    @GetMapping("/list")
    public Result<PageResult<BorrowRecordResponse>> getBorrowList(
            @Parameter(description = "User ID") @RequestParam(required = false) Long userId,
            @Parameter(description = "Book ID") @RequestParam(required = false) Long bookId,
            @Parameter(description = "Status") @RequestParam(required = false) Integer status,
            @Parameter(description = "Start date") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam(required = false) 
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BorrowRecordResponse> page = borrowService.getBorrowPage(
                userId, bookId, status, startDate, endDate, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "Get user's borrow records")
    @GetMapping("/user/{userId}")
    public Result<PageResult<BorrowRecordResponse>> getUserBorrowRecords(
            @PathVariable Long userId,
            @Parameter(description = "Status") @RequestParam(required = false) Integer status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BorrowRecordResponse> page = borrowService.getUserBorrowRecords(userId, status, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "Get overdue records")
    @GetMapping("/overdue")
    public Result<PageResult<BorrowRecordResponse>> getOverdueRecords(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BorrowRecordResponse> page = borrowService.getOverdueRecords(pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "Get borrow record by ID")
    @GetMapping("/{id}")
    public Result<BorrowRecordResponse> getRecordById(@PathVariable Long id) {
        return Result.success(borrowService.getRecordById(id));
    }

    @Operation(summary = "Get current user's borrow records")
    @GetMapping("/my")
    public Result<PageResult<BorrowRecordResponse>> getMyBorrowRecords(
            @RequestHeader(SystemConstants.USER_ID_HEADER) Long userId,
            @Parameter(description = "Status") @RequestParam(required = false) Integer status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BorrowRecordResponse> page = borrowService.getUserBorrowRecords(userId, status, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }
}
