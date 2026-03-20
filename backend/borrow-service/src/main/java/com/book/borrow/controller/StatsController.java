package com.book.borrow.controller;

import com.book.common.result.Result;
import com.book.borrow.dto.BorrowTrend;
import com.book.borrow.dto.HotBook;
import com.book.borrow.dto.StatsOverview;
import com.book.borrow.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Statistics Controller
 */
@Tag(name = "Statistics", description = "Statistics and analytics APIs")
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @Operation(summary = "Get system overview statistics")
    @GetMapping("/overview")
    public Result<StatsOverview> getOverview() {
        return Result.success(statsService.getOverview());
    }

    @Operation(summary = "Get borrow trend")
    @GetMapping("/borrow/trend")
    public Result<BorrowTrend> getBorrowTrend(
            @Parameter(description = "Type: day/week/month") @RequestParam(required = false, defaultValue = "week") String type,
            @Parameter(description = "Start date (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "End date (yyyy-MM-dd)") @RequestParam(required = false) String endDate) {
        return Result.success(statsService.getBorrowTrend(type, startDate, endDate));
    }

    @Operation(summary = "Get hot books ranking")
    @GetMapping("/book/hot")
    public Result<List<HotBook>> getHotBooks(
            @Parameter(description = "Number of results") @RequestParam(defaultValue = "10") Integer limit,
            @Parameter(description = "Period: week/month/year") @RequestParam(defaultValue = "month") String period) {
        return Result.success(statsService.getHotBooks(limit, period));
    }

    @Operation(summary = "Get category distribution")
    @GetMapping("/category/distribution")
    public Result<List<Map<String, Object>>> getCategoryDistribution() {
        return Result.success(statsService.getCategoryDistribution());
    }

    @Operation(summary = "Get active users ranking")
    @GetMapping("/user/active")
    public Result<List<Map<String, Object>>> getActiveUsers(
            @Parameter(description = "Number of results") @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(statsService.getActiveUsers(limit));
    }
}
