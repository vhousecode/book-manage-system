package com.book.borrow.service.impl;

import com.book.borrow.dto.BorrowTrend;
import com.book.borrow.dto.HotBook;
import com.book.borrow.dto.StatsOverview;
import com.book.borrow.mapper.BorrowRecordMapper;
import com.book.borrow.service.StatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Statistics Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final BorrowRecordMapper borrowRecordMapper;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public StatsOverview getOverview() {
        StatsOverview overview = new StatsOverview();

        // Total books (from book service, use local query for demo)
        Long totalBooks = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM book_info WHERE deleted = 0", Long.class);
        overview.setTotalBooks(totalBooks != null ? totalBooks : 0L);

        // Total users
        Long totalUsers = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM sys_user WHERE deleted = 0", Long.class);
        overview.setTotalUsers(totalUsers != null ? totalUsers : 0L);

        // Currently borrowed
        Long totalBorrowed = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM borrow_record WHERE status = 0", Long.class);
        overview.setTotalBorrowed(totalBorrowed != null ? totalBorrowed : 0L);

        // Overdue
        Long totalOverdue = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM borrow_record WHERE status = 0 AND due_date < NOW()", Long.class);
        overview.setTotalOverdue(totalOverdue != null ? totalOverdue : 0L);

        // Today's stats
        overview.setTodayBorrow(borrowRecordMapper.countTodayBorrow());
        overview.setTodayReturn(borrowRecordMapper.countTodayReturn());

        // Available books
        Long availableBooks = jdbcTemplate.queryForObject(
                "SELECT SUM(available_stock) FROM book_info WHERE deleted = 0 AND status = 1", Long.class);
        overview.setAvailableBooks(availableBooks != null ? availableBooks : 0L);

        return overview;
    }

    @Override
    public BorrowTrend getBorrowTrend(String type, String startDate, String endDate) {
        BorrowTrend trend = new BorrowTrend();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        // Calculate date range based on type
        if ("week".equalsIgnoreCase(type)) {
            start = end.minusDays(7);
        } else if ("month".equalsIgnoreCase(type)) {
            start = end.minusDays(30);
        } else {
            start = end.minusDays(7);  // Default to week
        }

        // Override with custom dates if provided
        if (startDate != null && !startDate.isEmpty()) {
            start = LocalDate.parse(startDate, formatter).atStartOfDay();
        }
        if (endDate != null && !endDate.isEmpty()) {
            end = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);
        }

        // Get borrow trend
        List<Map<String, Object>> borrowData = borrowRecordMapper.getBorrowTrend(start, end);
        List<Map<String, Object>> returnData = borrowRecordMapper.getReturnTrend(start, end);

        // Build date list
        List<String> dates = new ArrayList<>();
        Map<String, Long> borrowMap = new LinkedHashMap<>();
        Map<String, Long> returnMap = new LinkedHashMap<>();

        // Fill dates
        LocalDate current = start.toLocalDate();
        while (!current.isAfter(end.toLocalDate())) {
            String dateStr = current.format(formatter);
            dates.add(dateStr);
            borrowMap.put(dateStr, 0L);
            returnMap.put(dateStr, 0L);
            current = current.plusDays(1);
        }

        // Fill borrow data
        for (Map<String, Object> row : borrowData) {
            Object dateObj = row.get("date");
            Object countObj = row.get("count");
            if (dateObj != null && countObj != null) {
                String dateStr = dateObj.toString();
                borrowMap.put(dateStr, ((Number) countObj).longValue());
            }
        }

        // Fill return data
        for (Map<String, Object> row : returnData) {
            Object dateObj = row.get("date");
            Object countObj = row.get("count");
            if (dateObj != null && countObj != null) {
                String dateStr = dateObj.toString();
                returnMap.put(dateStr, ((Number) countObj).longValue());
            }
        }

        trend.setDates(dates);
        trend.setBorrowCount(new ArrayList<>(borrowMap.values()));
        trend.setReturnCount(new ArrayList<>(returnMap.values()));

        return trend;
    }

    @Override
    public List<HotBook> getHotBooks(int limit, String period) {
        LocalDateTime startDate;

        if ("week".equalsIgnoreCase(period)) {
            startDate = LocalDateTime.now().minusDays(7);
        } else if ("month".equalsIgnoreCase(period)) {
            startDate = LocalDateTime.now().minusDays(30);
        } else if ("year".equalsIgnoreCase(period)) {
            startDate = LocalDateTime.now().minusDays(365);
        } else {
            startDate = LocalDateTime.now().minusDays(30);  // Default to month
        }

        List<Map<String, Object>> hotBookData = borrowRecordMapper.getHotBooks(startDate, limit);
        List<HotBook> hotBooks = new ArrayList<>();

        int rank = 1;
        for (Map<String, Object> row : hotBookData) {
            HotBook hotBook = new HotBook();
            hotBook.setRank(rank++);

            Object bookIdObj = row.get("book_id");
            Object countObj = row.get("count");
            if (bookIdObj != null) {
                hotBook.setBookId(((Number) bookIdObj).longValue());
                // Fetch book info from database
                Map<String, Object> bookInfo = jdbcTemplate.queryForMap(
                        "SELECT title, author, cover FROM book_info WHERE id = ?",
                        hotBook.getBookId());
                if (bookInfo != null) {
                    hotBook.setTitle((String) bookInfo.get("title"));
                    hotBook.setAuthor((String) bookInfo.get("author"));
                    hotBook.setCover((String) bookInfo.get("cover"));
                }
            }
            if (countObj != null) {
                hotBook.setBorrowCount(((Number) countObj).longValue());
            }

            hotBooks.add(hotBook);
        }

        return hotBooks;
    }

    @Override
    public List<Map<String, Object>> getCategoryDistribution() {
        String sql = "SELECT c.name as category, COUNT(b.id) as count " +
                     "FROM book_category c " +
                     "LEFT JOIN book_info b ON c.id = b.category_id AND b.deleted = 0 " +
                     "WHERE c.deleted = 0 AND c.parent_id != 0 " +
                     "GROUP BY c.id, c.name " +
                     "ORDER BY count DESC " +
                     "LIMIT 10";

        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getActiveUsers(int limit) {
        String sql = "SELECT u.id, u.username, u.nickname, COUNT(br.id) as borrow_count " +
                     "FROM sys_user u " +
                     "JOIN borrow_record br ON u.id = br.user_id " +
                     "WHERE u.deleted = 0 " +
                     "GROUP BY u.id, u.username, u.nickname " +
                     "ORDER BY borrow_count DESC " +
                     "LIMIT ?";

        return jdbcTemplate.queryForList(sql, limit);
    }
}
