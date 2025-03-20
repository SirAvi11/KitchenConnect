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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

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

    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderDetails>> getOrderItems(@PathVariable Long orderId) {
        List<OrderDetails> orderDetails = orderService.getOrderDetailsById(orderId);
        return ResponseEntity.ok(orderDetails);
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

    @PostMapping("/update-order-status")
    public ResponseEntity<String> updateOrderStatus(@RequestBody Map<String, String> request) {
        try {
            Long orderId = Long.parseLong(request.get("orderId"));
            String newStatus = request.get("newStatus");
            System.out.println("id" + orderId + "status" + newStatus);

            // Fetch the order by ID
            Order order = orderService.getOrderById(orderId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order not found");
            }

            // Update the order status
            order.setStatus(OrderStatus.valueOf(newStatus));
            orderService.saveOrder(order);

            return ResponseEntity.ok("Order status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update order status");
        }
    }
}