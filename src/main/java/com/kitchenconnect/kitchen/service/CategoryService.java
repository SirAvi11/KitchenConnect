package com.kitchenconnect.kitchen.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.kitchenconnect.kitchen.DTO.MoveCategoryRequest;
import com.kitchenconnect.kitchen.entity.Category;

public interface CategoryService {
    List<Category> getCategoriesByKitchen(Long kitchenId);
    Category createCategory(Category category);
    boolean updateCategory(Long id, String updatedCategoryName);
    boolean deleteCategory(Long id);
    Category getCategoryById(Long id);
    void moveCategory(Long categoryId, String direction);
}

