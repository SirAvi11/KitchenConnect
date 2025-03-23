package com.kitchenconnect.kitchen.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kitchenconnect.kitchen.entity.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByKitchen_KitchenIdOrderByPositionAsc(Long kitchenId);

    List<Category> findAllByOrderByPositionAsc();
}
