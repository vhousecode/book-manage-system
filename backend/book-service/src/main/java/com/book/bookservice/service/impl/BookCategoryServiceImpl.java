package com.book.bookservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.book.bookservice.dto.CategoryTreeResponse;
import com.book.bookservice.entity.BookCategory;
import com.book.bookservice.mapper.BookCategoryMapper;
import com.book.bookservice.service.BookCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Book Category Service Implementation
 */
@Service
@RequiredArgsConstructor
public class BookCategoryServiceImpl extends ServiceImpl<BookCategoryMapper, BookCategory> implements BookCategoryService {

    @Override
    public List<CategoryTreeResponse> getCategoryTree() {
        List<BookCategory> categories = list(new LambdaQueryWrapper<BookCategory>()
                .eq(BookCategory::getDeleted, 0)
                .orderByAsc(BookCategory::getSort));

        // Build tree
        Map<Long, CategoryTreeResponse> categoryMap = categories.stream()
                .map(this::convertToTreeResponse)
                .collect(Collectors.toMap(CategoryTreeResponse::getId, c -> c));

        List<CategoryTreeResponse> tree = new ArrayList<>();
        for (CategoryTreeResponse category : categoryMap.values()) {
            if (category.getParentId() == null || category.getParentId() == 0) {
                tree.add(category);
            } else {
                CategoryTreeResponse parent = categoryMap.get(category.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(category);
                }
            }
        }

        return tree;
    }

    @Override
    public List<BookCategory> getAllCategories() {
        return list(new LambdaQueryWrapper<BookCategory>()
                .eq(BookCategory::getDeleted, 0)
                .orderByAsc(BookCategory::getSort));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createCategory(BookCategory category) {
        category.setDeleted(0);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        save(category);
        return category.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCategory(BookCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        updateById(category);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCategory(Long id) {
        BookCategory category = getById(id);
        if (category != null) {
            category.setDeleted(1);
            category.setUpdateTime(LocalDateTime.now());
            updateById(category);
        }
    }

    private CategoryTreeResponse convertToTreeResponse(BookCategory category) {
        CategoryTreeResponse response = new CategoryTreeResponse();
        BeanUtil.copyProperties(category, response);
        return response;
    }
}
