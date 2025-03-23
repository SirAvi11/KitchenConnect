package com.kitchenconnect.kitchen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kitchenconnect.kitchen.DTO.CategoryRequest;
import com.kitchenconnect.kitchen.DTO.MoveCategoryRequest;
import com.kitchenconnect.kitchen.DTO.UpdateCategoryRequest;
import com.kitchenconnect.kitchen.entity.Category;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.service.CategoryService;
import com.kitchenconnect.kitchen.service.KitchenService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private KitchenService kitchenService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategory(@RequestBody CategoryRequest categoryRequest, HttpSession session) {
        // Get the logged-in user from session
        User sessionUser = (User) session.getAttribute("loggedInUser");
        Kitchen existingKitchen = kitchenService.findKitchenByUser(sessionUser);

        if (existingKitchen == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("status", "error", "message", "kitchen not found"));
        }

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setKitchen(existingKitchen);

        Category result = categoryService.createCategory(category);

        if (result != null) {
            return ResponseEntity.ok(Map.of("status", "success", "message", "saved successfully", "id", result.getId()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "failed to save"));
        }
    }

    @GetMapping("/kitchen/{kitchenId}")
    public List<Category> getCategoriesByKitchen(@PathVariable Long kitchenId) {
        return categoryService.getCategoriesByKitchen(kitchenId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Boolean> updateCategory(@PathVariable Long id, @RequestBody UpdateCategoryRequest updateCategoryRequest) {
        boolean isUpdated = categoryService.updateCategory(updateCategoryRequest.getId(), updateCategoryRequest.getName());
        if (isUpdated) {
            return ResponseEntity.ok(true); // Return true if updated is successful
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Return false if updated is not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable Long id) {
        boolean isDeleted = categoryService.deleteCategory(id);
        if (isDeleted) {
            return ResponseEntity.ok(true); // Return true if deletion is successful
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Return false if category is not found
        }
    }

    @PostMapping("/move")
    public ResponseEntity<Map<String, String>> moveCategory(@RequestBody MoveCategoryRequest request) {
        try {
            categoryService.moveCategory(request.getCategoryId(), request.getDirection());
            Map<String, String> response = new HashMap<>();
            response.put("redirectUrl", "/dashboard?tab=manage-menu");
            return ResponseEntity.ok(response); // Return JSON with redirect URL
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error moving category"));
        }
    }
}
