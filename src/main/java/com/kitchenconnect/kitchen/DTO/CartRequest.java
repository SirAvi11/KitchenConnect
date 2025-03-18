package com.kitchenconnect.kitchen.DTO;

public class CartRequest {
    private Long foodItemId;
    private Integer quantity;

    // Constructors
    public CartRequest() {}

    public CartRequest(Long foodItemId, Integer quantity) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
    }

    // Getters and Setters
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

