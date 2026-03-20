package com.book.borrow.client;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Book Info DTO (for Feign response)
 */
@Data
public class BookInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String cover;
    private Integer stock;
    private Integer availableStock;
    private Integer status;
}
