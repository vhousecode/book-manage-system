package com.book.borrow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.borrow.entity.BorrowRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Borrow Record Mapper
 */
@Mapper
public interface BorrowRecordMapper extends BaseMapper<BorrowRecord> {

    /**
     * Count user's current borrowed books
     */
    @Select("SELECT COUNT(*) FROM borrow_record WHERE user_id = #{userId} AND status = 0")
    int countUserBorrowing(@Param("userId") Long userId);

    /**
     * Count today's borrows
     */
    @Select("SELECT COUNT(*) FROM borrow_record WHERE DATE(borrow_date) = CURDATE()")
    long countTodayBorrow();

    /**
     * Count today's returns
     */
    @Select("SELECT COUNT(*) FROM borrow_record WHERE DATE(return_date) = CURDATE()")
    long countTodayReturn();

    /**
     * Get hot books (by borrow count)
     */
    @Select("SELECT book_id, COUNT(*) as count FROM borrow_record " +
            "WHERE borrow_date >= #{startDate} " +
            "GROUP BY book_id ORDER BY count DESC LIMIT #{limit}")
    List<Map<String, Object>> getHotBooks(@Param("startDate") LocalDateTime startDate, 
                                          @Param("limit") int limit);

    /**
     * Get borrow trend by date range
     */
    @Select("SELECT DATE(borrow_date) as date, COUNT(*) as count FROM borrow_record " +
            "WHERE borrow_date BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE(borrow_date) ORDER BY date")
    List<Map<String, Object>> getBorrowTrend(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    /**
     * Get return trend by date range
     */
    @Select("SELECT DATE(return_date) as date, COUNT(*) as count FROM borrow_record " +
            "WHERE return_date BETWEEN #{startDate} AND #{endDate} " +
            "GROUP BY DATE(return_date) ORDER BY date")
    List<Map<String, Object>> getReturnTrend(@Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);
}
