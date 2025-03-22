package com.kitchenconnect.kitchen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kitchenconnect.kitchen.entity.MenuItem;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByCategoryId(Long categoryId);
    List<MenuItem> findTop4ByOrderByOverallRatingDesc();
}