package com.kitchenconnect.kitchen.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.UserRole;
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
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");

        model.addAttribute("user", loggedInUser);
        if (loggedInUser.getRole() == UserRole.ADMIN) {
            Map<String, Object> kitchenData = getKitchenData();
            model.addAttribute("kitchenData", kitchenData);

        }
        
        return "dashboard"; //Return the dashboard view
    }

    private Map<String, Object> getKitchenData(){
        Map<String, Object> kitchenData = new HashMap<>();

        List<Kitchen> underVerificationKitchens = kitchenService.getKitchenUnderVerification();
        List<Kitchen> activeKitchens = kitchenService.getActiveKitchens();
        List<Kitchen> allKitchens = kitchenService.getAllKitchens();

        kitchenData.put("underVerificationKitchens", underVerificationKitchens);
        kitchenData.put("underVerificationCount", underVerificationKitchens.size());

        kitchenData.put("activeKitchens", activeKitchens);
        kitchenData.put("activeCount", activeKitchens.size());

        kitchenData.put("allKitchens", allKitchens);
        kitchenData.put("allCount", allKitchens.size());

        return kitchenData;
    }
}
