package com.book.bookservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.book.bookservice.entity.Book;
import org.apache.ibatis.annotations.Mapper;

/**
 * Book Mapper
 */
@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
