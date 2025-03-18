package com.kitchenconnect.kitchen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.kitchenconnect.kitchen.entity.Category;
import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.service.CategoryService;
import com.kitchenconnect.kitchen.service.MenuItemService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private CategoryService categoryService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/menuImages/";

    @GetMapping("/category/{categoryId}")
    public List<MenuItem> getMenuItemsByCategory(@PathVariable Long categoryId) {
        return menuItemService.getMenuItemsByCategory(categoryId);
    }

    @PostMapping(value = "/save-item", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> saveFoodItem(
            @RequestParam("dishName") String dishName,
            @RequestParam("dishDescription") String dishDescription,
            @RequestParam("price") double price,
            @RequestParam("foodType") String foodType,
            @RequestParam("categoryId") String cId,
            @RequestParam(value = "menuId", required = false) String mId, // Allow menuId to be null
            @RequestParam(value = "image", required = false) MultipartFile imageFile,
            HttpSession session) {

        try {
            
            User sessionUser = (User) session.getAttribute("loggedInUser");
            Long categoryId = (Long) Long.parseLong(cId);
            MenuItem existingMenuItem = null;
            if(mId != null){
                Long menuId = (Long) Long.parseLong(mId);
                existingMenuItem = menuItemService.getMenuItemById(menuId);
            }

            // Validate the payload
            if (dishName == null || dishName.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Dish name cannot be empty."));
            }

            // Save the image file (if provided)
            String imagePath = null;
            if (imageFile != null && !imageFile.isEmpty()) {
                imagePath = saveToDisk(imageFile,sessionUser.getId(),categoryId);
            }

            Category category = categoryService.getCategoryById(categoryId);
            
            if(category != null){
                // Save the food item
                MenuItem menuItem = existingMenuItem != null ? existingMenuItem : new MenuItem();
                menuItem.setCategory(category);
                menuItem.setName(dishName);
                menuItem.setDescription(dishDescription);
                menuItem.setPrice(price);
                if (imageFile != null && !imageFile.isEmpty()) {
                    menuItem.setImageUrl(imagePath);
                }
                menuItem.setIsVeg(foodType == "veg" ? true : false);
                MenuItem savedMenuItem = menuItemService.createMenuItem(menuItem);

                return ResponseEntity.ok(Map.of("status", "success", "message", "Food item saved successfully.", "menuId", savedMenuItem.getId(), "imageUrl", savedMenuItem.getImageUrl()));

            }

            return ResponseEntity.ok(Map.of("status", "error", "message", "Failed to get category"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", "Failed to save food item."));
        }
    }

    @PostMapping
    public MenuItem createMenuItem(@RequestBody MenuItem menuItem) {
        return menuItemService.createMenuItem(menuItem);
    }

    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id, @RequestBody MenuItem updatedMenuItem) {
        return menuItemService.updateMenuItem(id, updatedMenuItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteMenuItem(@PathVariable Long id) {
        boolean isDeleted =  menuItemService.deleteMenuItem(id);
        if (isDeleted) {
            return ResponseEntity.ok(true); // Return true if deletion is successful
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false); // Return false if category is not found
        }
    }

    private String saveToDisk(MultipartFile file, Long userId, Long categoryId ) throws IOException {

        // Ensure userId is valid
        if (file.isEmpty() || userId == null) {
            throw new IllegalArgumentException("Invalid file or userId");
        }

        String menuFolder = UPLOAD_DIR + userId + "/" + categoryId + "/";

        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        Path path = Paths.get(menuFolder + fileName);
        
        // Ensure the directory exists, otherwise create it
        Files.createDirectories(path.getParent());
        
        Files.write(path, file.getBytes());
        String returnPath = "/uploads/menuImages/" + userId + "/" + categoryId + "/" + fileName;
        return returnPath; // Return the file path
    }
}
