package com.kitchenconnect.kitchen.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.KitchenRequest;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.KitchenStatus;
import com.kitchenconnect.kitchen.enums.UserRole;
import com.kitchenconnect.kitchen.repository.ChefRepository;
import com.kitchenconnect.kitchen.repository.KitchenRepository;
import com.kitchenconnect.kitchen.repository.UserRepository;
import com.kitchenconnect.kitchen.service.KitchenService;


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

    public void saveKitchenRequest(KitchenRequest kitchenRequest) {
        User user = userRepository.findById(kitchenRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Kitchen kitchen = new Kitchen();
        kitchen.setUser(user);
        kitchen.setStatus(KitchenStatus.UNDER_VERIFICATION);
        kitchen.setKitchenName(kitchenRequest.getKitchenName());
        kitchen.setKitchenDescription(kitchenRequest.getKitchenDescription());
        kitchen.setKitchenImage(kitchenRequest.getKitchenImagePath());
        kitchen.setOverallRating(BigDecimal.ZERO);  // Default rating
        kitchen.setTotalRatingsCount(0);
        kitchen.setMinDeliveryTime(20);
        kitchen.setMaxDeliveryTime(40);
        kitchen.setDeliveryFees(kitchenRequest.getDeliveryFees());
        kitchen.setShopName(kitchenRequest.getShopName());
        kitchen.setFloor(kitchenRequest.getFloor());
        kitchen.setArea(kitchenRequest.getArea());
        kitchen.setCity(kitchenRequest.getCity());
        kitchen.setPhoneNumber(kitchenRequest.getPhoneNumber());
        kitchen.setMenuImagePaths(kitchenRequest.getMenuImagePaths());
        kitchen.setSelectedCuisines(kitchenRequest.getSelectedCuisines());
        kitchen.setOpenDays(kitchenRequest.getOpenDays());
        kitchen.setOpenTime("12:00");
        kitchen.setCloseTime("14:00");
        kitchen.setFssaiNumber(kitchenRequest.getFssaiNumber());
        kitchen.setFssaiExpiryDate(kitchenRequest.getFssaiExpiryDate());
        kitchen.setFssaiDocumentPath(kitchenRequest.getFssaiDocumentPath());
        kitchen.setPanNumber(kitchenRequest.getPanNumber());
        kitchen.setPanDocumentPath(kitchenRequest.getPanDocumentPath());
        kitchen.setAcceptTerms(kitchenRequest.isAcceptTerms());

        kitchenRepository.save(kitchen);
    }

    public Kitchen findKitchenByUser(User user) {
        return kitchenRepository.findByUser(user);
    }

    public boolean updateKitchenStatus(Long kitchenId, boolean isApproved) {
        Optional<Kitchen> retrievedKitchen = kitchenRepository.findById(kitchenId);
        if (retrievedKitchen.isPresent()) {
            Kitchen kitchen = retrievedKitchen.get();
            kitchen.setStatus(isApproved ? KitchenStatus.APPROVED : KitchenStatus.REJECTED);
            if(isApproved){
                kitchen.getUser().setRole(UserRole.CHEF);
                Chef chef = new Chef();
                chef.setBiography("Write you journey!");
                chef.setFavouriteDishes(String.join(",", kitchen.getSelectedCuisines()));
                chef.setKitchen(kitchen);
                chef.setUser(kitchen.getUser());
                chefRepository.save(chef);
                userRepository.save(kitchen.getUser());
            }
            kitchenRepository.save(kitchen);
            return true;
        }
        return false;
    }

}

