package com.kitchenconnect.kitchen.service;

import java.util.List;

import com.kitchenconnect.kitchen.entity.Category;

public interface CategoryService {
    List<Category> getCategoriesByKitchen(Long kitchenId);
    Category createCategory(Category category);
    boolean updateCategory(Long id, String updatedCategoryName);
    boolean deleteCategory(Long id);
    Category getCategoryById(Long id);
}

