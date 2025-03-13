package com.kitchenconnect.kitchen.service;
import java.util.List;

import com.kitchenconnect.kitchen.DTO.OrderRequest;
import com.kitchenconnect.kitchen.entity.Order;
import com.kitchenconnect.kitchen.enums.OrderStatus;

public interface OrderService {
    Order placeOrder(OrderRequest orderRequest);
    List<Order> getAllOrders();
    Order getOrderById(Long id);
    Order updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}
