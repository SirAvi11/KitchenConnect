package com.kitchenconnect.kitchen.repository;

import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    // Find all order details for a specific order
    List<OrderDetails> findByOrder(Order order);
    List<OrderDetails> findByOrderId(Long id);
}