package com.kitchenconnect.kitchen.DTO;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private Long kitchenId;
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getKitchenId() {
        return kitchenId;
    }
    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }
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

}