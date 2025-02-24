package com.kitchenconnect.kitchen.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kitchenconnect.kitchen.entity.CartRequest;
import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.service.FoodItemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private FoodItemService foodItemService;

    @GetMapping
    public String showCartPage(HttpSession session, Model model) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        // Ensure cart is not null
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        // Extract food item IDs
        List<Long> foodItemIds = new ArrayList<>(cart.keySet());
        // Fetch food details from service
        List<FoodItem> foodItems = foodItemIds.isEmpty() ? new ArrayList<>() : foodItemService.getFoodItemsByIds(foodItemIds);

        // Calculate the total number of items in the cart
        int totalItemsInCart = cart.values().stream().mapToInt(Integer::intValue).sum();

        // Add food items and cart quantities to model
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("cartQuantities", cart);      
        model.addAttribute("totalItems", totalItemsInCart);
        return "cart"; 
    }

    @PostMapping("/update")
    @ResponseBody
    public String updateCart(@RequestBody CartRequest cartRequest, HttpSession session) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null) {
            cart = new HashMap<>();
        }

        if (cartRequest.getQuantity() > 0) {
            cart.put(cartRequest.getFoodItemId(), cartRequest.getQuantity());
        } else {
            cart.remove(cartRequest.getFoodItemId()); // Remove item if quantity is 0
        }

        session.setAttribute("cart", cart); // Store cart in session

        return "Cart updated in session";
    }
}
