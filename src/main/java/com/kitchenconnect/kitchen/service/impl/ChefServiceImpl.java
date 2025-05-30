package com.kitchenconnect.kitchen.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.repository.ChefRepository;
import com.kitchenconnect.kitchen.service.ChefService;


@Service
public class ChefServiceImpl implements ChefService {
    @Autowired
    private ChefRepository chefRepository;

    public Chef getChefById(Long id) {
        return chefRepository.findById(id).orElse(null);
    }

    public Chef getChefByKitchenId(Long id){
        return chefRepository.findByKitchen_KitchenId(id).orElse(null);
    }

    public Chef updateChef(Chef chef){
        return chefRepository.save(chef);
    }

    public Chef getChefByUserId(Long userID){
        return chefRepository.findByUser_Id(userID).orElse(null);
    }

}

