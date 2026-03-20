package com.book.bookservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.book.bookservice.dto.BookRequest;
import com.book.bookservice.dto.BookResponse;
import com.book.bookservice.entity.Book;

/**
 * Book Service Interface
 */
public interface BookService extends IService<Book> {

    /**
     * Get book page list
     */
    Page<BookResponse> getBookPage(String title, String author, String isbn, 
                                   Long categoryId, Integer status, 
                                   int pageNum, int pageSize);

    /**
     * Get book by ID
     */
    BookResponse getBookById(Long id);

    /**
     * Create book
     */
    Long createBook(BookRequest request);

    /**
     * Update book
     */
    void updateBook(BookRequest request);

    /**
     * Delete book
     */
    void deleteBook(Long id);

    /**
     * Update book status
     */
    void updateStatus(Long id, Integer status);

    /**
     * Update book stock
     */
    void updateStock(Long id, Integer stock, Integer availableStock);

    /**
     * Search books by keyword
     */
    Page<BookResponse> searchBooks(String keyword, int pageNum, int pageSize);

    /**
     * Decrease available stock
     */
    boolean decreaseAvailableStock(Long bookId);

    /**
     * Increase available stock
     */
    boolean increaseAvailableStock(Long bookId);
}
