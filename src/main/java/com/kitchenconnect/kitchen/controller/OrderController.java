package com.kitchenconnect.kitchen.controller;

import com.kitchenconnect.kitchen.DTO.OrderRequest;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.OrderDetails;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.OrderStatus;
import com.kitchenconnect.kitchen.service.KitchenService;
import com.kitchenconnect.kitchen.service.OrderService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private KitchenService kitchenService;

    // Create a new order
    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderRequest orderRequest, HttpSession session) {
        // Get the logged-in user from session
        User sessionUser = (User) session.getAttribute("loggedInUser");
        Kitchen existingKitchen = kitchenService.findKitchenByUser(sessionUser);
        Order placedOrder = orderService.placeOrder(orderRequest, sessionUser.getId(), existingKitchen.getKitchenId());
        return ResponseEntity.ok(placedOrder);
    }

    // Get all orders
    // @GetMapping
    // public ResponseEntity<List<Order>> getAllOrders() {
    //     List<Order> orders = orderService.getAllOrders();
    //     return ResponseEntity.ok(orders);
    // }

    @GetMapping
    public String showOrderPage(){
        return "Orders";
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Delete an order
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}