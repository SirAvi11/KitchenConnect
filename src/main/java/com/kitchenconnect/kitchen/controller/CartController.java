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

        // Fetch food details 
        List<FoodItem> foodItems = getFoodItems(cart);

        // Calculate the total number of items in the cart
        int totalItemsInCart = cart.size();

        // Add food items and cart quantities to model
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("cartQuantities", cart);      
        model.addAttribute("totalItems", totalItemsInCart);
        return "cart"; 
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCart(@RequestBody CartRequest cartRequest, HttpSession session) {
        // Retrieve or initialize the cart
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        Map<String, Object> response = new HashMap<>();

        if (cart == null) {
            session.setAttribute("cart", cart = new HashMap<>());
        }

        // Check if cart is not empty before performing kitchen validation
        if (!cart.isEmpty()) {
            Long firstFoodId = cart.keySet().iterator().next();
            FoodItem existingFoodItem = foodItemService.getFoodItemById(firstFoodId);
            FoodItem newFoodItem = foodItemService.getFoodItemById(cartRequest.getFoodItemId());

            if (existingFoodItem != null && newFoodItem != null) {
                Long existingKitchenId = existingFoodItem.getKitchen().getKitchenId();
                Long newKitchenId = newFoodItem.getKitchen().getKitchenId();

                if (!existingKitchenId.equals(newKitchenId)) {
                    response.put("status", "error");
                    response.put("message", "Cart contains items from another kitchen.");
                    return response;
                }
            }
        }

        // Update the cart
        if (cartRequest.getQuantity() > 0) {
            cart.put(cartRequest.getFoodItemId(), cartRequest.getQuantity());
        } else {
            cart.remove(cartRequest.getFoodItemId()); // Remove item if quantity is 0
        }

        session.setAttribute("cart", cart); // Update session cart
        response.put("status", "success");
        response.put("message", "Cart updated successfully.");
        return response;
    }

    @PostMapping("/clear-cart")
    @ResponseBody
    public Map<String, String> clearCart(HttpSession session) {
        Map<String, String> response = new HashMap<>();

        session.setAttribute("cart", new HashMap<Long, Integer>());

        response.put("status", "success");
        response.put("message", "Cart cleared successfully.");
        return response;
        
    }

    private List<FoodItem> getFoodItems(Map<Long, Integer> cart){
        // Extract food item IDs
        List<Long> foodItemIds = new ArrayList<>(cart.keySet());
        // Fetch food details from service
        List<FoodItem> foodItems = foodItemIds.isEmpty() ? new ArrayList<>() : foodItemService.getFoodItemsByIds(foodItemIds);

        return foodItems;
    }

}
