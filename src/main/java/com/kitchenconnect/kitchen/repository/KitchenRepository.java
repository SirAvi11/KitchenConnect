package com.kitchenconnect.kitchen.repository;

import com.kitchenconnect.kitchen.entity.Kitchen;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KitchenRepository extends JpaRepository<Kitchen, Long> {
    List<Kitchen> findTop3ByOrderByOverallRatingDesc();
}
