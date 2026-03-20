package com.book.borrow.client;

import com.book.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Book Service Feign Client
 */
@FeignClient(name = "book-service", path = "/api/book")
public interface BookClient {

    @GetMapping("/{id}")
    Result<BookInfo> getBookById(@PathVariable("id") Long id);

    @PostMapping("/stock/decrease")
    Result<Boolean> decreaseStock(@PathVariable("bookId") Long bookId);

    @PostMapping("/stock/increase")
    Result<Boolean> increaseStock(@PathVariable("bookId") Long bookId);
}
