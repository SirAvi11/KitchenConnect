package com.kitchenconnect.kitchen.DTO;

public class OrderItemRequest {
    private Long foodItemId;
    private Integer quantity;
    public Long getFoodItemId() {
        return foodItemId;
    }
    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
