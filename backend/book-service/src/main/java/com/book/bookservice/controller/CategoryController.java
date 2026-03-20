package com.book.bookservice.controller;

import com.book.common.result.Result;
import com.book.bookservice.dto.CategoryTreeResponse;
import com.book.bookservice.entity.BookCategory;
import com.book.bookservice.service.BookCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Book Category Controller
 */
@Tag(name = "Category Management", description = "Book category management APIs")
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final BookCategoryService categoryService;

    @Operation(summary = "Get category tree")
    @GetMapping("/tree")
    public Result<List<CategoryTreeResponse>> getCategoryTree() {
        return Result.success(categoryService.getCategoryTree());
    }

    @Operation(summary = "Get all categories (flat list)")
    @GetMapping("/list")
    public Result<List<BookCategory>> getAllCategories() {
        return Result.success(categoryService.getAllCategories());
    }

    @Operation(summary = "Create category")
    @PostMapping
    public Result<Long> createCategory(@RequestBody BookCategory category) {
        return Result.success(categoryService.createCategory(category));
    }

    @Operation(summary = "Update category")
    @PutMapping
    public Result<Void> updateCategory(@RequestBody BookCategory category) {
        categoryService.updateCategory(category);
        return Result.success();
    }

    @Operation(summary = "Delete category")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
}
