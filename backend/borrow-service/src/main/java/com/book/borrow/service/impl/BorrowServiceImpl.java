package com.book.borrow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.borrow.client.BookClient;
import com.book.borrow.client.BookInfo;
import com.book.borrow.client.UserClient;
import com.book.borrow.client.UserInfo;
import com.book.borrow.dto.*;
import com.book.borrow.entity.BorrowRecord;
import com.book.borrow.mapper.BorrowRecordMapper;
import com.book.borrow.service.BorrowService;
import com.book.common.exception.BusinessException;
import com.book.common.result.Result;
import com.book.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Borrow Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BorrowServiceImpl extends ServiceImpl<BorrowRecordMapper, BorrowRecord> implements BorrowService {

    private final UserClient userClient;
    private final BookClient bookClient;

    @Value("${borrow.max-borrow-count:5}")
    private int maxBorrowCount;

    @Value("${borrow.max-borrow-days:30}")
    private int maxBorrowDays;

    @Value("${borrow.max-renew-times:2}")
    private int maxRenewTimes;

    @Value("${borrow.renew-days:15}")
    private int renewDays;

    @Value("${borrow.overdue-fine-rate:0.50}")
    private double overdueFineRate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BorrowRecordResponse borrowBook(BorrowRequest request) {
        // Validate user
        Result<UserInfo> userResult = userClient.getUserById(request.getUserId());
        if (userResult == null || !userResult.isSuccess() || userResult.getData() == null) {
            throw new BusinessException(ResultCode.USER_NOT_FOUND);
        }

        // Validate book
        Result<BookInfo> bookResult = bookClient.getBookById(request.getBookId());
        if (bookResult == null || !bookResult.isSuccess() || bookResult.getData() == null) {
            throw new BusinessException(ResultCode.BOOK_NOT_FOUND);
        }

        BookInfo book = bookResult.getData();
        if (book.getStatus() != 1) {
            throw new BusinessException(ResultCode.BOOK_NOT_AVAILABLE);
        }
        if (book.getAvailableStock() <= 0) {
            throw new BusinessException(ResultCode.BOOK_STOCK_NOT_ENOUGH);
        }

        // Check borrow limit
        int currentBorrowing = baseMapper.countUserBorrowing(request.getUserId());
        if (currentBorrowing >= maxBorrowCount) {
            throw new BusinessException(ResultCode.BORROW_LIMIT_EXCEEDED, 
                    "You have reached the maximum borrow limit of " + maxBorrowCount + " books");
        }

        // Create borrow record
        int days = request.getDays() != null ? request.getDays() : maxBorrowDays;
        LocalDateTime now = LocalDateTime.now();
        
        BorrowRecord record = new BorrowRecord();
        record.setUserId(request.getUserId());
        record.setBookId(request.getBookId());
        record.setBorrowDate(now);
        record.setDueDate(now.plusDays(days));
        record.setRenewCount(0);
        record.setStatus(0);
        record.setCreateTime(now);
        record.setUpdateTime(now);

        save(record);

        // Decrease available stock (call book service)
        // Note: In production, use Feign to call book service
        log.info("Decrease available stock for book: {}", request.getBookId());

        return convertToResponse(record, userResult.getData(), book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BorrowRecordResponse returnBook(Long recordId) {
        BorrowRecord record = getById(recordId);
        if (record == null) {
            throw new BusinessException(ResultCode.BORROW_RECORD_NOT_FOUND);
        }

        if (record.getStatus() == 1) {
            throw new BusinessException(ResultCode.BORROW_ALREADY_RETURNED);
        }

        LocalDateTime now = LocalDateTime.now();
        record.setReturnDate(now);
        record.setUpdateTime(now);

        // Calculate overdue days and fine
        int overdueDays = 0;
        BigDecimal fine = BigDecimal.ZERO;
        if (now.isAfter(record.getDueDate())) {
            overdueDays = (int) ChronoUnit.DAYS.between(record.getDueDate(), now);
            fine = BigDecimal.valueOf(overdueDays * overdueFineRate).setScale(2, RoundingMode.HALF_UP);
            record.setStatus(2); // Overdue
        } else {
            record.setStatus(1); // Returned
        }

        updateById(record);

        // Increase available stock (call book service)
        log.info("Increase available stock for book: {}", record.getBookId());

        BorrowRecordResponse response = convertToResponse(record, null, null);
        response.setOverdueDays(overdueDays);
        response.setFine(fine);

        return response;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BorrowRecordResponse renewBook(Long recordId, Integer days) {
        BorrowRecord record = getById(recordId);
        if (record == null) {
            throw new BusinessException(ResultCode.BORROW_RECORD_NOT_FOUND);
        }

        if (record.getStatus() != 0) {
            throw new BusinessException(ResultCode.BORROW_ALREADY_RETURNED, "Cannot renew a returned book");
        }

        if (record.getRenewCount() >= maxRenewTimes) {
            throw new BusinessException(ResultCode.RENEW_LIMIT_EXCEEDED, 
                    "Maximum renew times (" + maxRenewTimes + ") reached");
        }

        // Check if overdue
        if (LocalDateTime.now().isAfter(record.getDueDate())) {
            throw new BusinessException(ResultCode.BORROW_OVERDUE, "Cannot renew an overdue book");
        }

        int renewDays = days != null ? days : this.renewDays;
        record.setDueDate(record.getDueDate().plusDays(renewDays));
        record.setRenewCount(record.getRenewCount() + 1);
        record.setUpdateTime(LocalDateTime.now());

        updateById(record);

        return convertToResponse(record, null, null);
    }

    @Override
    public Page<BorrowRecordResponse> getBorrowPage(Long userId, Long bookId, Integer status,
                                                     LocalDateTime startDate, LocalDateTime endDate,
                                                     int pageNum, int pageSize) {
        Page<BorrowRecord> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        if (userId != null) {
            wrapper.eq(BorrowRecord::getUserId, userId);
        }
        if (bookId != null) {
            wrapper.eq(BorrowRecord::getBookId, bookId);
        }
        if (status != null) {
            wrapper.eq(BorrowRecord::getStatus, status);
        }
        if (startDate != null) {
            wrapper.ge(BorrowRecord::getBorrowDate, startDate);
        }
        if (endDate != null) {
            wrapper.le(BorrowRecord::getBorrowDate, endDate);
        }
        wrapper.orderByDesc(BorrowRecord::getCreateTime);

        Page<BorrowRecord> recordPage = page(page, wrapper);

        Page<BorrowRecordResponse> responsePage = new Page<>();
        responsePage.setCurrent(recordPage.getCurrent());
        responsePage.setSize(recordPage.getSize());
        responsePage.setTotal(recordPage.getTotal());
        responsePage.setPages(recordPage.getPages());
        responsePage.setRecords(recordPage.getRecords().stream()
                .map(r -> convertToResponse(r, null, null))
                .toList());

        return responsePage;
    }

    @Override
    public Page<BorrowRecordResponse> getUserBorrowRecords(Long userId, Integer status, int pageNum, int pageSize) {
        return getBorrowPage(userId, null, status, null, null, pageNum, pageSize);
    }

    @Override
    public Page<BorrowRecordResponse> getOverdueRecords(int pageNum, int pageSize) {
        Page<BorrowRecord> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<BorrowRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BorrowRecord::getStatus, 0)  // Still borrowed
                .lt(BorrowRecord::getDueDate, LocalDateTime.now())  // Past due date
                .orderByAsc(BorrowRecord::getDueDate);

        Page<BorrowRecord> recordPage = page(page, wrapper);

        Page<BorrowRecordResponse> responsePage = new Page<>();
        responsePage.setCurrent(recordPage.getCurrent());
        responsePage.setSize(recordPage.getSize());
        responsePage.setTotal(recordPage.getTotal());
        responsePage.setPages(recordPage.getPages());
        responsePage.setRecords(recordPage.getRecords().stream()
                .map(r -> {
                    BorrowRecordResponse response = convertToResponse(r, null, null);
                    // Calculate overdue days
                    if (r.getDueDate() != null && LocalDateTime.now().isAfter(r.getDueDate())) {
                        response.setOverdueDays((int) ChronoUnit.DAYS.between(r.getDueDate(), LocalDateTime.now()));
                    }
                    return response;
                })
                .toList());

        return responsePage;
    }

    @Override
    public BorrowRecordResponse getRecordById(Long id) {
        BorrowRecord record = getById(id);
        if (record == null) {
            throw new BusinessException(ResultCode.BORROW_RECORD_NOT_FOUND);
        }
        return convertToResponse(record, null, null);
    }

    private BorrowRecordResponse convertToResponse(BorrowRecord record, UserInfo user, BookInfo book) {
        BorrowRecordResponse response = new BorrowRecordResponse();
        BeanUtil.copyProperties(record, response);

        // In production, fetch from Feign clients
        if (user != null) {
            response.setUsername(user.getUsername());
        }

        if (book != null) {
            response.setBookTitle(book.getTitle());
            response.setBookCover(book.getCover());
        }

        return response;
    }
}
