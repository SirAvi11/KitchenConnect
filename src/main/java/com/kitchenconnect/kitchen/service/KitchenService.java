package com.kitchenconnect.kitchen.service;

import java.util.List;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.KitchenRequest;
import com.kitchenconnect.kitchen.entity.User;

public interface KitchenService {
    List<Kitchen> getAllKitchens();
    List<Kitchen> getFeaturedKitchens();
    Kitchen getKitchenById(Long id);
    void saveKitchenRequest(KitchenRequest kitchenRequest);
    Kitchen findKitchenByUser(User user);
    List<Kitchen> getKitchenUnderVerification();
    List<Kitchen> getActiveKitchens();
    boolean updateKitchenStatus(Long kitchenId, boolean isApproved);
}
