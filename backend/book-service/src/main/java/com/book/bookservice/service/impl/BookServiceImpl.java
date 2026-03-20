package com.book.bookservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookservice.dto.BookRequest;
import com.book.bookservice.dto.BookResponse;
import com.book.bookservice.entity.Book;
import com.book.bookservice.entity.BookCategory;
import com.book.bookservice.mapper.BookCategoryMapper;
import com.book.bookservice.mapper.BookMapper;
import com.book.bookservice.service.BookService;
import com.book.common.exception.BusinessException;
import com.book.common.result.ResultCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Book Service Implementation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    private final BookCategoryMapper categoryMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Page<BookResponse> getBookPage(String title, String author, String isbn,
                                          Long categoryId, Integer status,
                                          int pageNum, int pageSize) {
        Page<Book> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getDeleted, 0);

        if (StrUtil.isNotBlank(title)) {
            wrapper.like(Book::getTitle, title);
        }
        if (StrUtil.isNotBlank(author)) {
            wrapper.like(Book::getAuthor, author);
        }
        if (StrUtil.isNotBlank(isbn)) {
            wrapper.like(Book::getIsbn, isbn);
        }
        if (categoryId != null) {
            wrapper.eq(Book::getCategoryId, categoryId);
        }
        if (status != null) {
            wrapper.eq(Book::getStatus, status);
        }
        wrapper.orderByDesc(Book::getCreateTime);

        Page<Book> bookPage = page(page, wrapper);

        // Get category names
        Map<Long, String> categoryMap = getCategoryMap();

        Page<BookResponse> responsePage = new Page<>();
        responsePage.setCurrent(bookPage.getCurrent());
        responsePage.setSize(bookPage.getSize());
        responsePage.setTotal(bookPage.getTotal());
        responsePage.setPages(bookPage.getPages());
        responsePage.setRecords(bookPage.getRecords().stream()
                .map(book -> convertToResponse(book, categoryMap))
                .collect(Collectors.toList()));

        return responsePage;
    }

    @Override
    public BookResponse getBookById(Long id) {
        Book book = getById(id);
        if (book == null || book.getDeleted() == 1) {
            throw new BusinessException(ResultCode.BOOK_NOT_FOUND);
        }

        Map<Long, String> categoryMap = getCategoryMap();
        return convertToResponse(book, categoryMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBook(BookRequest request) {
        // Check ISBN
        if (StrUtil.isNotBlank(request.getIsbn())) {
            Book existingBook = getOne(new LambdaQueryWrapper<Book>()
                    .eq(Book::getIsbn, request.getIsbn())
                    .eq(Book::getDeleted, 0));
            if (existingBook != null) {
                throw new BusinessException(ResultCode.CONFLICT, "ISBN already exists");
            }
        }

        Book book = new Book();
        BeanUtil.copyProperties(request, book);
        book.setStatus(1);
        book.setDeleted(0);
        book.setCreateTime(LocalDateTime.now());
        book.setUpdateTime(LocalDateTime.now());

        // Set available stock same as stock if not provided
        if (book.getAvailableStock() == null && book.getStock() != null) {
            book.setAvailableStock(book.getStock());
        }

        save(book);
        return book.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(BookRequest request) {
        Book existingBook = getById(request.getId());
        if (existingBook == null) {
            throw new BusinessException(ResultCode.BOOK_NOT_FOUND);
        }

        Book book = new Book();
        BeanUtil.copyProperties(request, book);
        book.setUpdateTime(LocalDateTime.now());
        updateById(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBook(Long id) {
        Book book = getById(id);
        if (book == null) {
            throw new BusinessException(ResultCode.BOOK_NOT_FOUND);
        }

        book.setDeleted(1);
        book.setUpdateTime(LocalDateTime.now());
        updateById(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        Book book = getById(id);
        if (book == null) {
            throw new BusinessException(ResultCode.BOOK_NOT_FOUND);
        }

        book.setStatus(status);
        book.setUpdateTime(LocalDateTime.now());
        updateById(book);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStock(Long id, Integer stock, Integer availableStock) {
        Book book = getById(id);
        if (book == null) {
            throw new BusinessException(ResultCode.BOOK_NOT_FOUND);
        }

        book.setStock(stock);
        if (availableStock != null) {
            book.setAvailableStock(availableStock);
        }
        book.setUpdateTime(LocalDateTime.now());
        updateById(book);
    }

    @Override
    public Page<BookResponse> searchBooks(String keyword, int pageNum, int pageSize) {
        Page<Book> page = new Page<>(pageNum, pageSize);

        LambdaQueryWrapper<Book> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Book::getDeleted, 0);
        wrapper.eq(Book::getStatus, 1);
        
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.and(w -> w
                    .like(Book::getTitle, keyword)
                    .or()
                    .like(Book::getAuthor, keyword)
                    .or()
                    .like(Book::getIsbn, keyword));
        }
        wrapper.orderByDesc(Book::getCreateTime);

        Page<Book> bookPage = page(page, wrapper);

        Map<Long, String> categoryMap = getCategoryMap();

        Page<BookResponse> responsePage = new Page<>();
        responsePage.setCurrent(bookPage.getCurrent());
        responsePage.setSize(bookPage.getSize());
        responsePage.setTotal(bookPage.getTotal());
        responsePage.setPages(bookPage.getPages());
        responsePage.setRecords(bookPage.getRecords().stream()
                .map(book -> convertToResponse(book, categoryMap))
                .collect(Collectors.toList()));

        return responsePage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean decreaseAvailableStock(Long bookId) {
        Book book = getById(bookId);
        if (book == null || book.getAvailableStock() <= 0) {
            return false;
        }

        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Book::getId, bookId)
                .gt(Book::getAvailableStock, 0)
                .setSql("available_stock = available_stock - 1")
                .set(Book::getUpdateTime, LocalDateTime.now());

        return update(updateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean increaseAvailableStock(Long bookId) {
        Book book = getById(bookId);
        if (book == null || book.getAvailableStock() >= book.getStock()) {
            return false;
        }

        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Book::getId, bookId)
                .apply("available_stock < stock")
                .setSql("available_stock = available_stock + 1")
                .set(Book::getUpdateTime, LocalDateTime.now());

        return update(updateWrapper);
    }

    /**
     * Get category ID-name mapping
     */
    private Map<Long, String> getCategoryMap() {
        return categoryMapper.selectList(
                new LambdaQueryWrapper<BookCategory>().eq(BookCategory::getDeleted, 0))
                .stream()
                .collect(Collectors.toMap(BookCategory::getId, BookCategory::getName));
    }

    /**
     * Convert Book entity to BookResponse DTO
     */
    private BookResponse convertToResponse(Book book, Map<Long, String> categoryMap) {
        BookResponse response = new BookResponse();
        BeanUtil.copyProperties(book, response);

        if (book.getCategoryId() != null && categoryMap != null) {
            response.setCategoryName(categoryMap.get(book.getCategoryId()));
        }

        if (book.getCreateTime() != null) {
            response.setCreateTime(book.getCreateTime().format(DATE_FORMATTER));
        }

        return response;
    }
}
