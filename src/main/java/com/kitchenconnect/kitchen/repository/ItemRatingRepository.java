package com.kitchenconnect.kitchen.repository;

import org.springframework.stereotype.Repository;
import com.kitchenconnect.kitchen.entity.ItemRating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemRatingRepository extends JpaRepository<ItemRating, Long> {
    List<ItemRating> findByMenuItemId(Long menuItemId);
}