package com.kitchenconnect.kitchen.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.repository.KitchenRepository;
import com.kitchenconnect.kitchen.service.KitchenService;


@Service
public class KitchenServiceImpl implements KitchenService {
    @Autowired
    private KitchenRepository kitchenRepository;

    public List<Kitchen> getAllKitchens() {
        return kitchenRepository.findAll();
    }
    
    public List<Kitchen> getFeaturedKitchens() {
        return kitchenRepository.findTop3ByOrderByOverallRatingDesc();
    }

    public Kitchen getKitchenById(Long id) {
        return kitchenRepository.findById(id).orElse(null);
    }

}

