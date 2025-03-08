package com.kitchenconnect.kitchen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.service.MenuItemService;

import java.util.List;

@Controller
@RequestMapping("/menu-items")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @GetMapping("/category/{categoryId}")
    public List<MenuItem> getMenuItemsByCategory(@PathVariable Long categoryId) {
        return menuItemService.getMenuItemsByCategory(categoryId);
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
    public void deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
    }
}
