package com.book.bookservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.bookservice.entity.BookCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * Book Category Mapper
 */
@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategory> {
}
