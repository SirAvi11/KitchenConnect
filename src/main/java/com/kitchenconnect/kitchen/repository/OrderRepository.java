package com.kitchenconnect.kitchen.repository;

import com.kitchenconnect.kitchen.entity.Kitchen;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Find all orders by a specific user
    List<Order> findByUser(User user);

    // Find all orders for a specific kitchen
    List<Order> findByKitchen(Kitchen kitchen);

    // Find orders by status
    List<Order> findByStatus(OrderStatus status);
    
}