package com.kitchenconnect.kitchen.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kitchenconnect.kitchen.DTO.CartRequest;
import com.kitchenconnect.kitchen.DTO.OrderRequest;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.MenuItem;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.OrderDetails;
import com.kitchenconnect.kitchen.service.MenuItemService;
import com.kitchenconnect.kitchen.service.OrderService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCartPage(HttpSession session, Model model) {
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        // Ensure cart is not null
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }

        // Fetch food details 
        List<MenuItem> foodItems = getFoodItems(cart);

        //Fetch kitchen details
        Kitchen kitchenData = foodItems.size() != 0 ? foodItems.get(0).getCategory().getKitchen() : null;

        // Calculate the total number of items in the cart
        int totalItemsInCart = cart.size();

      

        if(foodItems.size() !=0 && kitchenData != null){

            // Calculate subtotal using simpler logic
            double subtotal = 0.0;
            for (MenuItem item : foodItems) {
                int quantity = cart.getOrDefault(item.getId(), 1);
                subtotal += item.getPrice() * quantity;
            }

            // Calculate tax (5%)
            double tax = subtotal * 0.05;

            // Calculate delivery fees (fixed or dynamic)
            double deliveryFees = kitchenData.getDeliveryFees().doubleValue();

            // Calculate platform fees (fixed or dynamic)
            double platformFees = 5.00;

            // Calculate total
            double total = subtotal + tax + deliveryFees + platformFees;

            model.addAttribute("tax", tax);
            model.addAttribute("deliveryFees", deliveryFees);
            model.addAttribute("platformFees", platformFees);
            model.addAttribute("total", total);
            model.addAttribute("subtotal", subtotal);
        }



        // Add food items and cart quantities to model
        model.addAttribute("foodItems", foodItems);
        model.addAttribute("cartQuantities", cart);      
        model.addAttribute("totalItems", totalItemsInCart);
        model.addAttribute("kitchen", kitchenData);
        
        return "cart"; 
    }

    @GetMapping("/redirect-to-dashboard-my-orders")
    public String redirectToDashboard() {
        // Redirect to the dashboard with the second tab activated
        return "redirect:/dashboard?tab=my-orders"; // Replace with the desired tab
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
            MenuItem existingFoodItem = menuItemService.getMenuItemById(firstFoodId);
            MenuItem newFoodItem = menuItemService.getMenuItemById(cartRequest.getFoodItemId());

            if (existingFoodItem != null && newFoodItem != null) {
                Long existingKitchenId = existingFoodItem.getCategory().getKitchen().getKitchenId();
                Long newKitchenId = newFoodItem.getCategory().getKitchen().getKitchenId();

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

    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody OrderRequest orderRequest, HttpSession session){
        Order placedOrder = orderService.placeOrder(orderRequest);
        if(placedOrder != null){
            clearCart(session);
        }
        // Return response
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("orderId", placedOrder.getId());
        response.put("transactionId", orderRequest.getUpiTransactionId());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/build")
    public ResponseEntity<Map<String, String>> buildCart(
        @RequestParam Long orderId,
        @RequestParam(required = false, defaultValue = "false") boolean clearCart,
        HttpSession session
    ) {
        Map<String, String> response = new HashMap<>();
        Map<Long, Integer> cart = (Map<Long, Integer>) session.getAttribute("cart");

        if (cart == null) {
            session.setAttribute("cart", cart = new HashMap<>());
        }

        // Clear the cart if requested
        if (clearCart) {
            clearCart(session);
        }

        // If the cart is not empty and clearCart is false, return an error
        if (!clearCart && !cart.isEmpty()) {
            Long firstFoodId = cart.keySet().iterator().next();
            MenuItem existingFoodItem = menuItemService.getMenuItemById(firstFoodId);
            Order expressOrder = orderService.getOrderById(orderId);
            MenuItem newFoodItem = expressOrder.getOrderDetails().get(0).getMenuItem();

            if (existingFoodItem != null && newFoodItem != null) {
                Long existingKitchenId = existingFoodItem.getCategory().getKitchen().getKitchenId();
                Long newKitchenId = newFoodItem.getCategory().getKitchen().getKitchenId();

                if (!existingKitchenId.equals(newKitchenId)) {
                    response.put("status", "error");
                    response.put("message", "Cart contains items from another kitchen.");
                    return ResponseEntity.ok(response);                
                }
            }
        }

        // Build the cart with the specified order items
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            response.put("status", "error");
            response.put("message", "Order not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        List<OrderDetails> orderDetails = orderService.getOrderById(orderId).getOrderDetails();

        for(OrderDetails od : orderDetails){
            MenuItem mI = od.getMenuItem();
            cart.put(mI.getId(), od.getQuantity());
        }

        response.put("status", "success");
        response.put("message", "Cart updated successfully.");
        return ResponseEntity.ok(response);
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

    private List<MenuItem> getFoodItems(Map<Long, Integer> cart){
        // Extract food item IDs
        List<Long> foodItemIds = new ArrayList<>(cart.keySet());
        // Fetch food details from service
        List<MenuItem> foodItems = foodItemIds.isEmpty() ? new ArrayList<>() : menuItemService.getMenuItemsByIds(foodItemIds);

        return foodItems;
    }

}
