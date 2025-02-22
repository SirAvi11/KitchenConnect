package com.kitchenconnect.kitchen.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.service.KitchenService;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private KitchenService kitchenService;

    // @Autowired
    // private FoodItemService foodItemService;

    @GetMapping
    public String showHomePage(Model model) {
        List<Kitchen> featuredKitchens = kitchenService.getFeaturedKitchens();
        // List<FoodItem> featuredFoodItems = foodItemService.getFeaturedFoodItems();

        model.addAttribute("kitchens", featuredKitchens);
        // model.addAttribute("foodItems", featuredFoodItems);
        return "index";
    }

    @GetMapping("/kitchens")
    public String kitchens() {
        return "kitchens";
    }

    @GetMapping("/chef")
    public String chef() {
        return "chef";
    }

    @GetMapping("/kitchenpage")
    public String kitchenpage() {
        return "kitchenpage";
    }
    

    // Show the registration and login page
    @GetMapping("/accounts")
    public String showAccountsPage(Model model) {
        model.addAttribute("user", new User()); // Ensure User class has the necessary fields
        return "accounts"; // This should match the name of your HTML file without the .html extension
    }
}
