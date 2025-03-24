package com.kitchenconnect.kitchen.repository;

import org.springframework.stereotype.Repository;

import com.kitchenconnect.kitchen.entity.Rating;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long>  {

    Rating findByOrderId(Long orderId);
    List<Rating> findByKitchenId(Long kitchenId);

}
