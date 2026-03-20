package com.book.bookservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.book.bookservice.dto.CategoryTreeResponse;
import com.book.bookservice.entity.BookCategory;

import java.util.List;

/**
 * Book Category Service Interface
 */
public interface BookCategoryService extends IService<BookCategory> {

    /**
     * Get category tree
     */
    List<CategoryTreeResponse> getCategoryTree();

    /**
     * Get all categories (flat list)
     */
    List<BookCategory> getAllCategories();

    /**
     * Create category
     */
    Long createCategory(BookCategory category);

    /**
     * Update category
     */
    void updateCategory(BookCategory category);

    /**
     * Delete category
     */
    void deleteCategory(Long id);
}
