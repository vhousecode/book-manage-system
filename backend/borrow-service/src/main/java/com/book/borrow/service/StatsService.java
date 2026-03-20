package com.book.borrow.service;

import com.book.borrow.dto.BorrowTrend;
import com.book.borrow.dto.HotBook;
import com.book.borrow.dto.StatsOverview;

import java.util.List;
import java.util.Map;

/**
 * Statistics Service Interface
 */
public interface StatsService {

    /**
     * Get statistics overview
     */
    StatsOverview getOverview();

    /**
     * Get borrow trend
     */
    BorrowTrend getBorrowTrend(String type, String startDate, String endDate);

    /**
     * Get hot books
     */
    List<HotBook> getHotBooks(int limit, String period);

    /**
     * Get category distribution
     */
    List<Map<String, Object>> getCategoryDistribution();

    /**
     * Get active users
     */
    List<Map<String, Object>> getActiveUsers(int limit);
}
