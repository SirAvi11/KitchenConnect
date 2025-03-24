package com.kitchenconnect.kitchen.repository;

import org.springframework.stereotype.Repository;
import com.kitchenconnect.kitchen.entity.ItemRating;
import com.kitchenconnect.kitchen.entity.Rating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ItemRatingRepository extends JpaRepository<ItemRating, Long> {
    ItemRating findByParentRatingAndItemName(Rating parentRating, String itemName);
    List<ItemRating> findByParentRating(Rating parentRating);
}