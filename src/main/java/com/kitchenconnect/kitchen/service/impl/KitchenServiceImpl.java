package com.kitchenconnect.kitchen.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.DTO.KitchenRequest;
import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.KitchenStatus;
import com.kitchenconnect.kitchen.enums.UserRole;
import com.kitchenconnect.kitchen.repository.ChefRepository;
import com.kitchenconnect.kitchen.repository.KitchenRepository;
import com.kitchenconnect.kitchen.repository.UserRepository;
import com.kitchenconnect.kitchen.service.KitchenService;

import jakarta.transaction.Transactional;


@Service
public class KitchenServiceImpl implements KitchenService {
    @Autowired
    private KitchenRepository kitchenRepository;

    @Autowired
    private ChefRepository chefRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Kitchen> getAllKitchens() {
        return kitchenRepository.findAll();
    }

    public List<Kitchen> getKitchenUnderVerification() {
        return kitchenRepository.findByStatus(KitchenStatus.UNDER_VERIFICATION);
    }

    public List<Kitchen> getActiveKitchens() {
        return kitchenRepository.findByStatus(KitchenStatus.ACTIVE);
    }

    public List<Kitchen> getFeaturedKitchens() {
        return kitchenRepository.findTop3ByOrderByOverallRatingDesc();
    }

    public Kitchen getKitchenById(Long id) {
        return kitchenRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveKitchenRequest(KitchenRequest kitchenRequest) {
        System.out.println("\n----------------Value of user id is ------------------\n" + kitchenRequest.getUserId());

        // Fetch the user by ID
        User user = userRepository.findById(kitchenRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if a kitchen already exists for the user
        Kitchen existingKitchen = findKitchenByUser(user);

        Kitchen kitchen;
        if (existingKitchen != null) {
            // If the kitchen exists, update it
            System.out.println("Updating existing kitchen ID: " + existingKitchen.getKitchenId());
            kitchen = existingKitchen;

            if (existingKitchen.getStatus() == KitchenStatus.REJECTED) {
                System.out.println("Resetting kitchen status: ");
                kitchen.setStatus(KitchenStatus.UNDER_VERIFICATION);  // Reset status if rejected
            }
        } else {
            // If no kitchen exists, create a new one
            System.out.println("Creating a new kitchen");
            kitchen = new Kitchen();
            kitchen.setUser(user);
            kitchen.setOverallRating(BigDecimal.ZERO);  // Default rating
            kitchen.setTotalRatingsCount(0);
            kitchen.setMinDeliveryTime(20);
            kitchen.setMaxDeliveryTime(40);
            kitchen.setOpenTime("12:00");
            kitchen.setCloseTime("14:00");
            kitchen.setStatus(KitchenStatus.UNDER_VERIFICATION);
        }

        // Update or set fields
        kitchen.setKitchenName(kitchenRequest.getKitchenName());
        kitchen.setKitchenDescription(kitchenRequest.getKitchenDescription());

        if (kitchenRequest.getKitchenImagePath() != null) {
            kitchen.setKitchenImagePath(kitchenRequest.getKitchenImagePath());
        }

        kitchen.setDeliveryFees(kitchenRequest.getDeliveryFees());
        kitchen.setShopName(kitchenRequest.getShopName());
        kitchen.setFloor(kitchenRequest.getFloor());
        kitchen.setArea(kitchenRequest.getArea());
        kitchen.setCity(kitchenRequest.getCity());
        kitchen.setPhoneNumber(kitchenRequest.getPhoneNumber());

        if (!kitchenRequest.getMenuImagePaths().isEmpty()) {
            kitchen.setMenuImagePaths(kitchenRequest.getMenuImagePaths());
        }

        if (!kitchenRequest.getSelectedCuisines().isEmpty()) {
            kitchen.setSelectedCuisines(kitchenRequest.getSelectedCuisines());
        }

        if (!kitchenRequest.getOpenDays().isEmpty()) {
            kitchen.setOpenDays(kitchenRequest.getOpenDays());
        }

        kitchen.setFssaiNumber(kitchenRequest.getFssaiNumber());
        kitchen.setFssaiExpiryDate(kitchenRequest.getFssaiExpiryDate());

        if (kitchenRequest.getFssaiDocumentPath() != null) {
            kitchen.setFssaiDocumentPath(kitchenRequest.getFssaiDocumentPath());
        }

        kitchen.setPanNumber(kitchenRequest.getPanNumber());

        if (kitchenRequest.getPanDocumentPath() != null) {
            kitchen.setPanDocumentPath(kitchenRequest.getPanDocumentPath());
        }

        kitchen.setAcceptTerms(kitchenRequest.isAcceptTerms());

        // Save the kitchen (insert or update)
        try {
            Kitchen savedKitchen = kitchenRepository.save(kitchen);
            System.out.println("Saved/Updated Kitchen ID: " + savedKitchen.getKitchenId());
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // Re-throw the exception to see it in the logs
        }
    }


    public Kitchen findKitchenByUser(User user) {
        return kitchenRepository.findByUser(user);
    }

    public boolean updateKitchenStatus(Long kitchenId, boolean isApproved) {
        Optional<Kitchen> retrievedKitchen = kitchenRepository.findById(kitchenId);
        if (retrievedKitchen.isPresent()) {
            Kitchen kitchen = retrievedKitchen.get();
            kitchen.setStatus(isApproved ? KitchenStatus.APPROVED : KitchenStatus.REJECTED);
            kitchen.getUser().setFirstLogin(true);
            if(isApproved){
                kitchen.getUser().setRole(UserRole.CHEF);
                Chef chef = new Chef();
                chef.setBiography("Write you journey!");
                chef.setFavouriteDishes(String.join(",", kitchen.getSelectedCuisines()));
                chef.setKitchen(kitchen);
                chef.setUser(kitchen.getUser());
                chefRepository.save(chef);
            }
            userRepository.save(kitchen.getUser());
            kitchenRepository.save(kitchen);
            return true;
        }
        return false;
    }

}

