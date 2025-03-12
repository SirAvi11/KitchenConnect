package com.kitchenconnect.kitchen.DTO;

import java.util.List;

public class OrderRequest {
    private double totalAmount;
    private List<OrderItemRequest> items;

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public List<OrderItemRequest> getItems() {
        return items;
    }
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }
    private Long userId;

}