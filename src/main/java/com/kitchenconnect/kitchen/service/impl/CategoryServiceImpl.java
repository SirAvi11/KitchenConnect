package com.kitchenconnect.kitchen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.Category;
import com.kitchenconnect.kitchen.repository.CategoryRepository;
import com.kitchenconnect.kitchen.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesByKitchen(Long kitchenId) {
        return categoryRepository.findByKitchen_KitchenId(kitchenId);
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    public boolean updateCategory(Long id, String updatedCategoryName) {
        if(categoryRepository.existsById(id)){
            Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
            category.setName(updatedCategoryName);
            categoryRepository.save(category);
            return true;
        }
        else{
            return false;
        }
        
    }

    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true; // Deletion successful
        }
        return false; // Entity does not exist    
    }
}
