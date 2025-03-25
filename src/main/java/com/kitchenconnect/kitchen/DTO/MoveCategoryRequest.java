package com.kitchenconnect.kitchen.DTO;

public class MoveCategoryRequest {
    private Long categoryId;
    private String direction;
    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }
}
