package com.kitchenconnect.kitchen.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.DTO.ItemRatingRequest;
import com.kitchenconnect.kitchen.DTO.ItemRatingResponse;
import com.kitchenconnect.kitchen.DTO.RatingRequest;
import com.kitchenconnect.kitchen.DTO.RatingResponse;
import com.kitchenconnect.kitchen.entity.ItemRating;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.Rating;
import com.kitchenconnect.kitchen.repository.ItemRatingRepository;
import com.kitchenconnect.kitchen.repository.KitchenRepository;
import com.kitchenconnect.kitchen.repository.OrderRepository;
import com.kitchenconnect.kitchen.repository.RatingRepository;
import com.kitchenconnect.kitchen.service.RatingService;

import jakarta.transaction.Transactional;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ItemRatingRepository itemRatingRepository;

    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private OrderRepository orderRepository;

    public RatingResponse getRatingsForOrder(Long orderId) {
        // Fetch ratings from the database
        Rating rating = ratingRepository.findByOrderId(orderId);
        if (rating == null) {
            return null;
        }

        RatingResponse response = new RatingResponse();

        response.setKitchenRating(rating.getKitchenRating());
        response.setUserNote(rating.getUserNote());

        if (rating.getItemRatings() == null || rating.getItemRatings().isEmpty()) {
            System.out.println("itemRatings is null or empty");
        } else {
            System.out.println("itemRatings size: " + rating.getItemRatings().size());
        }

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
    @Transactional
    public void saveOrUpdateRatings(Long orderId, RatingRequest request) {
        // Save or update the main rating
        System.out.println("orderId item raing" + orderId);


        Rating rating = ratingRepository.findByOrderId(orderId);
        if (rating == null) {
            rating = new Rating();
            rating.setOrderId(orderId);
        }

        rating.setKitchenRating(request.getKitchenRating());
        rating.setUserNote(request.getUserNote());
        ratingRepository.save(rating);

        // Process each item rating from the request
        for (ItemRatingRequest itemRequest : request.getItemRatings()) {
            // Try to find existing item rating
            ItemRating existingItemRating = itemRatingRepository
                .findByParentRatingAndItemName(rating, itemRequest.getItemName());

            System.out.println("Existing item raing" + existingItemRating.getParentRating().getId());

            
            if (existingItemRating != null) {
                // Update existing
                existingItemRating.setRatingValue(itemRequest.getRating());
                itemRatingRepository.save(existingItemRating);
            } else {
                // Create new
                ItemRating newItemRating = new ItemRating();
                newItemRating.setItemName(itemRequest.getItemName());
                newItemRating.setRatingValue(itemRequest.getRating());
                newItemRating.setParentRating(rating);
                itemRatingRepository.save(newItemRating);
            }
        }
    }

    @Transactional
    public void updateKitchenRating(Long kitchenId) {
        Kitchen kitchen = kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new RuntimeException("Kitchen not found"));

        //Fetch all orders for the kitchens

        List<Order> orders = orderRepository.findByKitchen(kitchen);

        //Get rating list for kitchens
        List<Rating> ratings = new ArrayList<Rating>();

        for(Order o : orders){
            ratings.add(ratingRepository.findByOrderId(o.getId()));
        }

        // Calculate overall rating and total ratings count
        double totalRating = ratings.stream().mapToInt(Rating::getKitchenRating).sum();
        int totalRatingsCount = ratings.size();
        double overallRating = totalRatingsCount > 0 ? totalRating / totalRatingsCount : 0.0;

        // Update the kitchen entity
        kitchen.setOverallRating(BigDecimal.valueOf(overallRating));
        kitchen.setTotalRatingsCount(totalRatingsCount);
        kitchenRepository.save(kitchen);
    }

    // @Transactional
    public void updateMenuItemRating(Long menuItemId) {
    //     MenuItem menuItem = menuItemRepository.findById(menuItemId)
    //             .orElseThrow(() -> new RuntimeException("Menu Item not found"));

    //     // Fetch all item ratings for the menu item
    //     List<ItemRating> itemRatings = itemRatingRepository.findByMenuItemId(menuItemId);

    //     // Calculate overall rating and rating count
    //     double totalRating = itemRatings.stream().mapToInt(ItemRating::getRatingValue).sum();
    //     int ratingCount = itemRatings.size();
    //     double overallRating = ratingCount > 0 ? totalRating / ratingCount : 0.0;

    //     // Update the menu item entity
    //     menuItem.setOverallRating(overallRating);
    //     menuItem.setRatingCount(ratingCount);
    //     menuItemRepository.save(menuItem);
    }

}
