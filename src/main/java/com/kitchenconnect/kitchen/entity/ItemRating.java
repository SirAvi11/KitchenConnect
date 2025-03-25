package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;

@Entity
public class ItemRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;


    private int ratingValue;

    @ManyToOne
    @JoinColumn(name = "rating_id", nullable = false)
    private Rating parentRating;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public Rating getParentRating() {
        return parentRating;
    }

    public void setParentRating(Rating parentRating) {
        this.parentRating = parentRating;
    }
    
}
