package com.kitchenconnect.kitchen.service;

import com.kitchenconnect.kitchen.DTO.RatingRequest;
import com.kitchenconnect.kitchen.DTO.RatingResponse;

public interface RatingService {
    RatingResponse getRatingsForOrder(Long orderId);
    void saveOrUpdateRatings(Long orderId, RatingRequest request);
    void updateMenuItemRating(Long menuItemId);
    void updateKitchenRating(Long kitchenId);

}
