package com.kitchenconnect.kitchen.controller;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kitchenconnect.kitchen.entity.Category;
import com.kitchenconnect.kitchen.entity.FoodItem;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.KitchenStatus;
import com.kitchenconnect.kitchen.enums.OrderStatus;
import com.kitchenconnect.kitchen.enums.UserRole;
import com.kitchenconnect.kitchen.service.CategoryService;
import com.kitchenconnect.kitchen.service.FoodItemService;
import com.kitchenconnect.kitchen.service.KitchenService;
import com.kitchenconnect.kitchen.service.OrderService;
import com.kitchenconnect.kitchen.service.UserService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private KitchenService kitchenService;

    @Autowired
    private FoodItemService foodItemService;

    @Autowired 
    private UserService userService;

    @Autowired 
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

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
    
    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/accounts/login"; // Redirect if no user is logged in
        }
    
        model.addAttribute("user", loggedInUser);
    
        // If the user is an ADMIN, add kitchen data
        if (loggedInUser.getRole() == UserRole.ADMIN ) {
            model.addAttribute("kitchenData", getKitchenData());
        }

        Kitchen userKitchen = kitchenService.findKitchenByUser(loggedInUser);
    
        // Show success or error message on first login after approval/rejection
        if (loggedInUser.getFirstLogin() && loggedInUser.getRole() != UserRole.ADMIN) {
            if (userKitchen != null) {
                switch (userKitchen.getStatus()) {
                    case APPROVED -> model.addAttribute("successMessage", "🎉 Congratulations! Your kitchen application was approved.");
                    case REJECTED -> model.addAttribute("errorMessage", "❌ Sorry, your kitchen application was rejected.");
                    default -> {}
                }
    
                // Mark first login as false to avoid showing the message again
                loggedInUser.setFirstLogin(false);
                userService.saveUser(loggedInUser);
            }
        }

        if(loggedInUser.getRole() == UserRole.CHEF && userKitchen.getStatus() == KitchenStatus.APPROVED){
            // Add kitchen data to the model in case of approval
            model.addAttribute("kitchen", userKitchen);
            List<Category> categories = categoryService.getCategoriesByKitchen(userKitchen.getKitchenId());
            
            session.setAttribute("categories", categories);
        }

        //If User is chef/food lover

        if(loggedInUser.getRole() == UserRole.CHEF || loggedInUser.getRole() == UserRole.FOOD_LOVER){
               // Initialize lists for current and past orders
        List<Order> currentOrders = new ArrayList<>();
        List<Order> pastOrders = new ArrayList<>();

        // Fetch orders based on the user's role
        List<Order> personalOrders = null;
        List<Order> kitchenOrders = null ;
        if (loggedInUser.getRole() == UserRole.CHEF) {
                // Fetch orders for the chef's kitchen
                Kitchen kitchen = kitchenService.findKitchenByUser(loggedInUser);
                kitchenOrders = orderService.getAllOrdersByKitchen(kitchen);
                personalOrders = orderService.getOrdersByUser(loggedInUser);
            } else {
                // Fetch orders for the food lover
                personalOrders = orderService.getOrdersByUser(loggedInUser);
            }

            if(personalOrders != null){
                // Split orders into currentOrders and pastOrders based on status
                for (Order order : personalOrders) {
                    if (order.getStatus() == OrderStatus.PENDING || order.getStatus() == OrderStatus.PROCESSING) {
                        currentOrders.add(order); // Add to currentOrders if status is PENDING or PROCESSING
                    } else {
                        pastOrders.add(order); // Add to pastOrders for all other statuses
                    }
                }
                // Add both lists to the model
                model.addAttribute("currentOrders", currentOrders);
                model.addAttribute("pastOrders", pastOrders);
            }

            if(kitchenOrders != null){
                model.addAttribute("kitchenOrders", kitchenOrders);
            }
        }
            
        return "dashboard";
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
