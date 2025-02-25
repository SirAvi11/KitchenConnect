package com.kitchenconnect.kitchen.controller;

import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.service.FoodItemService;
import com.kitchenconnect.kitchen.service.KitchenService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kitchens")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private FoodItemService foodItemService;

    @GetMapping
    public String showAllKitchens(Model model) {
        List<Kitchen> allKitchens = kitchenService.getAllKitchens();

        model.addAttribute("kitchens", allKitchens);
        return "kitchens";
    }

    @GetMapping("/{id}")
    public String showKitchenDetails(@PathVariable Long id, Model model, HttpSession session) {
        Kitchen kitchen = kitchenService.getKitchenById(id);

        if (kitchen != null) {
            // Fetch the chef's name from the associated Chef entity
            String chefName = kitchen.getChef().getUser().getFirstname() + " " + kitchen.getChef().getUser().getLastname();
            Long chefId = kitchen.getChef().getChefId();

            //Fetch Food items (menu)
            List<FoodItem> menuItems = foodItemService.findByKitchenId(id);

            // Retrieve cart from session
            Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            model.addAttribute("kitchen", kitchen);
            model.addAttribute("chefName", chefName); // Add chef's name to the model
            model.addAttribute("chefId", chefId); // Add chef's Id to the model
            model.addAttribute("menuItems", menuItems); // Add menu items to model
            model.addAttribute("cartItems", cart);

            return "kitchenpage";
        }
        
        return "redirect:/"; // Redirect to homepage if kitchen not found
    }
}


