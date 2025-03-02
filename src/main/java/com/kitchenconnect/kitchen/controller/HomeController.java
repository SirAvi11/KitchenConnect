package com.kitchenconnect.kitchen.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.service.FoodItemService;
import com.kitchenconnect.kitchen.service.KitchenService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private FoodItemService foodItemService;

    @GetMapping
    public String showHomePage(Model model, HttpSession session) {
        List<Kitchen> featuredKitchens = kitchenService.getFeaturedKitchens();
        List<FoodItem> featuredFoodItems = foodItemService.getFeaturedFoodItems();
         // Retrieve cart from session
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }

        model.addAttribute("kitchens", featuredKitchens);
        model.addAttribute("foodItems", featuredFoodItems);
        model.addAttribute("cartItems", cart);

        return "index";
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";
    }

    
    
    @GetMapping("/dashboard")
    public String showChefDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("user", loggedInUser);
    
        return "dashboard"; //Return the chef's dashboard view
    }

    @GetMapping("/kitchen-registration")
    public String showKitchenRegistration() {
        return "kitchenRegistration";
    }
}
