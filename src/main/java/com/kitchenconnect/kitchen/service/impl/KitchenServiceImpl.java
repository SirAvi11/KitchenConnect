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

import jakarta.persistence.EntityNotFoundException;
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
        return kitchenRepository.findByStatus(KitchenStatus.APPROVED);
    }

    public List<Kitchen> getFeaturedKitchens() {
        return kitchenRepository.findTop3ByOrderByOverallRatingDesc();
    }

    public Kitchen getKitchenById(Long id) {
        return kitchenRepository.findById(id).orElse(null);
    }

    @Transactional
    public void saveKitchenRequest(KitchenRequest kitchenRequest) {
        // Fetch the user by ID
        User user = userRepository.findById(kitchenRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if a kitchen already exists for the user
        Kitchen existingKitchen = findKitchenByUser(user);

        Kitchen kitchen;
        if (existingKitchen != null) {
            // If the kitchen exists, update it
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

    @Transactional
public boolean updateKitchenStatus(Long kitchenId, KitchenStatus status) {
    Kitchen kitchen = kitchenRepository.findById(kitchenId)
            .orElseThrow(() -> new EntityNotFoundException("Kitchen not found"));

    // First save any changes to the kitchen
    kitchen.setStatus(status);
    kitchen.getUser().setFirstLogin(true);

    if (status == KitchenStatus.APPROVED) {
        kitchen.getUser().setRole(UserRole.CHEF);
        
        if (kitchen.getChef() == null) {
            Chef chef = new Chef();
            chef.setBiography("Write your journey!");
            chef.setFavouriteDishes(String.join(",", kitchen.getSelectedCuisines()));
            chef.setUser(kitchen.getUser());
            
            // This is the critical part - save the chef FIRST
            chef = chefRepository.save(chef);
            
            // Then establish the relationship
            chef.setKitchen(kitchen);
            kitchen.setChef(chef);
        } else {
            // Update existing chef
            Chef chef = kitchen.getChef();
            chef.setFavouriteDishes(String.join(",", kitchen.getSelectedCuisines()));
            chefRepository.save(chef);
        }
    } 
    else if (status == KitchenStatus.REJECTED) {
        kitchen.getUser().setRole(UserRole.FOOD_LOVER);
        if (kitchen.getChef() != null) {
            chefRepository.delete(kitchen.getChef());
            kitchen.setChef(null);
        }
    }
    else if (status == KitchenStatus.UNDER_VERIFICATION) {
        kitchen.getUser().setRole(UserRole.FOOD_LOVER);
    }

    // Finally save the kitchen
    kitchenRepository.save(kitchen);
    return true;
}
    
    public Kitchen updateKitchen(Long kitchenId, Kitchen updatedKitchen) {
        Kitchen existingKitchen = kitchenRepository.findById(kitchenId)
                .orElseThrow(() -> new RuntimeException("Kitchen not found with ID: " + kitchenId));

        // Update the fields
        existingKitchen.setKitchenName(updatedKitchen.getKitchenName());
        existingKitchen.setKitchenDescription(updatedKitchen.getKitchenDescription());
        existingKitchen.setKitchenImagePath(updatedKitchen.getKitchenImagePath());
        existingKitchen.setSelectedCuisines(updatedKitchen.getSelectedCuisines());
        existingKitchen.setKitchenDescription(updatedKitchen.getKitchenDescription());
        existingKitchen.setOpenDays(updatedKitchen.getOpenDays());
        existingKitchen.setOpenTime(updatedKitchen.getOpenTime());
        existingKitchen.setCloseTime(updatedKitchen.getCloseTime());
        existingKitchen.setPhoneNumber(updatedKitchen.getPhoneNumber());
        existingKitchen.setDeliveryFees(updatedKitchen.getDeliveryFees());
        existingKitchen.setMinDeliveryTime(updatedKitchen.getMinDeliveryTime());
        existingKitchen.setMaxDeliveryTime(updatedKitchen.getMaxDeliveryTime());
        existingKitchen.setShopName(updatedKitchen.getShopName());
        existingKitchen.setFloor(updatedKitchen.getFloor());
        existingKitchen.setArea(updatedKitchen.getArea());
        existingKitchen.setCity(updatedKitchen.getCity());

        return kitchenRepository.save(existingKitchen);
    }

}

