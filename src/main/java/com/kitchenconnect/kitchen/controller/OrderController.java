package com.kitchenconnect.kitchen.controller;

import com.kitchenconnect.kitchen.DTO.OrderRequest;
import com.kitchenconnect.kitchen.DTO.RatingRequest;
import com.kitchenconnect.kitchen.DTO.RatingResponse;
import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.OrderDetails;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.OrderStatus;
import com.kitchenconnect.kitchen.service.KitchenService;
import com.kitchenconnect.kitchen.service.OrderService;
import com.kitchenconnect.kitchen.service.RatingService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RatingService ratingService;

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

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, HttpServletRequest request) {
         // Update the order status to CANCELLED
         orderService.updateOrderStatus(id, OrderStatus.CANCELLED);
         return "redirect:/dashboard?tab=my-orders";
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

    @GetMapping("/{orderId}/get-ratings")
    public ResponseEntity<RatingResponse> getOrderRatings(@PathVariable Long orderId) {
        RatingResponse ratingResponse = ratingService.getRatingsForOrder(orderId);
        if (ratingResponse == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ratingResponse);
    }

    @PostMapping("/{orderId}/ratings")
    public ResponseEntity<Map<String, String>> saveOrderRatings(
        @PathVariable Long orderId,
        @RequestBody RatingRequest ratingRequest
    ) {
        ratingService.saveOrUpdateRatings(orderId, ratingRequest);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Ratings saved successfully.");
        return ResponseEntity.ok(response);
    }
}