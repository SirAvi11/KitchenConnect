package com.kitchenconnect.kitchen.service; // Correct package


import java.util.List;

import com.kitchenconnect.kitchen.entity.MenuItem;

public interface MenuItemService {
    List<MenuItem> getFeaturedMenuItems();
    List<MenuItem> getMenuItemsByCategory(Long categoryId);
    MenuItem createMenuItem(MenuItem menuItem);
    MenuItem updateMenuItem(Long id, MenuItem updatedMenuItem);
    boolean deleteMenuItem(Long id);
    MenuItem getMenuItemById(Long id);
    List<MenuItem> getMenuItemsByIds(List<Long> ids);
}
