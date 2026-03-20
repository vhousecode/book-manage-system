package com.book.borrow.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.borrow.dto.*;
import com.book.borrow.entity.BorrowRecord;

import java.time.LocalDateTime;

/**
 * Borrow Service Interface
 */
public interface BorrowService extends IService<BorrowRecord> {

    /**
     * Borrow a book
     */
    BorrowRecordResponse borrowBook(BorrowRequest request);

    /**
     * Return a book
     */
    BorrowRecordResponse returnBook(Long recordId);

    /**
     * Renew a book
     */
    BorrowRecordResponse renewBook(Long recordId, Integer days);

    /**
     * Get borrow record page
     */
    Page<BorrowRecordResponse> getBorrowPage(Long userId, Long bookId, Integer status,
                                              LocalDateTime startDate, LocalDateTime endDate,
                                              int pageNum, int pageSize);

    /**
     * Get user's borrow records
     */
    Page<BorrowRecordResponse> getUserBorrowRecords(Long userId, Integer status, int pageNum, int pageSize);

    /**
     * Get overdue records
     */
    Page<BorrowRecordResponse> getOverdueRecords(int pageNum, int pageSize);

    /**
     * Get record by ID
     */
    BorrowRecordResponse getRecordById(Long id);
}
