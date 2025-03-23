package com.kitchenconnect.kitchen.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.DTO.ItemRatingResponse;
import com.kitchenconnect.kitchen.DTO.RatingRequest;
import com.kitchenconnect.kitchen.DTO.RatingResponse;
import com.kitchenconnect.kitchen.entity.ItemRating;
import com.kitchenconnect.kitchen.entity.Rating;
import com.kitchenconnect.kitchen.repository.RatingRepository;
import com.kitchenconnect.kitchen.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public RatingResponse getRatingsForOrder(Long orderId) {
        // Fetch ratings from the database
        Rating rating = ratingRepository.findByOrderId(orderId);
        if (rating == null) {
            return null;
        }

        RatingResponse response = new RatingResponse();
        response.setKitchenRating(rating.getKitchenRating());
        response.setUserNote(rating.getUserNote());

        List<ItemRatingResponse> itemRatings = rating.getItemRatings().stream()
            .map(item -> {
                ItemRatingResponse itemResponse = new ItemRatingResponse();
                itemResponse.setItemName(item.getItemName());
                itemResponse.setRating(item.getRatingValue());
                return itemResponse;
            })
            .collect(Collectors.toList());

        response.setItemRatings(itemRatings);
        return response;
    }

    public void saveOrUpdateRatings(Long orderId, RatingRequest request) {
        // Save or update ratings in the database
        Rating rating = ratingRepository.findByOrderId(orderId);
        if (rating == null) {
            rating = new Rating();
            rating.setOrderId(orderId);
        }

        rating.setKitchenRating(request.getKitchenRating());
        rating.setUserNote(request.getUserNote());

        List<ItemRating> itemRatings = request.getItemRatings().stream()
            .map(itemRequest -> {
                ItemRating itemRating = new ItemRating();
                itemRating.setItemName(itemRequest.getItemName());
                itemRating.setRatingValue(itemRequest.getRating());
                return itemRating;
            })
            .collect(Collectors.toList());

        rating.setItemRatings(itemRatings);
        ratingRepository.save(rating);
    }

}
