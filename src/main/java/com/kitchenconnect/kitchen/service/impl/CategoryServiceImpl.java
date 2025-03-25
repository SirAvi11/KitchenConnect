package com.kitchenconnect.kitchen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kitchenconnect.kitchen.DTO.MoveCategoryRequest;
import com.kitchenconnect.kitchen.entity.Category;
import com.kitchenconnect.kitchen.repository.CategoryRepository;
import com.kitchenconnect.kitchen.service.CategoryService;

import jakarta.transaction.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategoriesByKitchen(Long kitchenId) {
        return categoryRepository.findAllByKitchen_KitchenIdOrderByPositionAsc(kitchenId);
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

    public Category getCategoryById(Long categoryId){
        return categoryRepository.findById(categoryId).orElse(null);
    }

    @Transactional
    public void moveCategory(Long categoryId, String direction) {
        // Fetch the category to be moved
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Fetch all categories ordered by their current position
        List<Category> categories = categoryRepository.findAllByKitchen_KitchenIdOrderByPositionAsc(category.getKitchen().getKitchenId());

        // Find the index of the current category
        int currentIndex = categories.indexOf(category);

        // Determine the new index based on the direction
        int newIndex = direction.equals("up") ? currentIndex - 1 : currentIndex + 1;

        // Ensure the new index is within bounds
        if (newIndex >= 0 && newIndex < categories.size()) {
            // Swap positions
            Category adjacentCategory = categories.get(newIndex);
            int tempPosition = category.getPosition();
            category.setPosition(adjacentCategory.getPosition());
            adjacentCategory.setPosition(tempPosition);

            // Save the updated categories
            categoryRepository.save(category);
            categoryRepository.save(adjacentCategory);
        }
    }

}
