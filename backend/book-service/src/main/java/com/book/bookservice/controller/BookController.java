package com.book.bookservice.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.book.common.result.PageResult;
import com.book.common.result.Result;
import com.book.bookservice.dto.BookRequest;
import com.book.bookservice.dto.BookResponse;
import com.book.bookservice.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Book Controller
 */
@Tag(name = "Book Management", description = "Book management APIs")
@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Get book list with pagination")
    @GetMapping("/list")
    public Result<PageResult<BookResponse>> getBookList(
            @Parameter(description = "Title") @RequestParam(required = false) String title,
            @Parameter(description = "Author") @RequestParam(required = false) String author,
            @Parameter(description = "ISBN") @RequestParam(required = false) String isbn,
            @Parameter(description = "Category ID") @RequestParam(required = false) Long categoryId,
            @Parameter(description = "Status") @RequestParam(required = false) Integer status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BookResponse> page = bookService.getBookPage(title, author, isbn, categoryId, status, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }

    @Operation(summary = "Get book by ID")
    @GetMapping("/{id}")
    public Result<BookResponse> getBookById(@PathVariable Long id) {
        return Result.success(bookService.getBookById(id));
    }

    @Operation(summary = "Create book")
    @PostMapping
    public Result<Long> createBook(@RequestBody BookRequest request) {
        return Result.success(bookService.createBook(request));
    }

    @Operation(summary = "Update book")
    @PutMapping
    public Result<Void> updateBook(@RequestBody BookRequest request) {
        bookService.updateBook(request);
        return Result.success();
    }

    @Operation(summary = "Delete book")
    @DeleteMapping("/{id}")
    public Result<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return Result.success();
    }

    @Operation(summary = "Update book status")
    @PutMapping("/status")
    public Result<Void> updateStatus(@RequestParam Long id, @RequestParam Integer status) {
        bookService.updateStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "Update book stock")
    @PutMapping("/stock")
    public Result<Void> updateStock(@RequestParam Long id, 
                                    @RequestParam Integer stock,
                                    @RequestParam(required = false) Integer availableStock) {
        bookService.updateStock(id, stock, availableStock);
        return Result.success();
    }

    @Operation(summary = "Search books by keyword")
    @GetMapping("/search")
    public Result<PageResult<BookResponse>> searchBooks(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") Integer pageSize) {

        Page<BookResponse> page = bookService.searchBooks(keyword, pageNum, pageSize);
        return Result.success(PageResult.of(page));
    }
}
