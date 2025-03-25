package com.kitchenconnect.kitchen.DTO;

import java.util.List;

public class RatingRequest {
    private int kitchenRating;
    private String userNote;
    private List<ItemRatingRequest> itemRatings;
    
    public int getKitchenRating() {
        return kitchenRating;
    }
    public void setKitchenRating(int kitchenRating) {
        this.kitchenRating = kitchenRating;
    }
    public String getUserNote() {
        return userNote;
    }
    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }
    public List<ItemRatingRequest> getItemRatings() {
        return itemRatings;
    }
    public void setItemRatings(List<ItemRatingRequest> itemRatings) {
        this.itemRatings = itemRatings;
    }

    // Getters and Setters
}
