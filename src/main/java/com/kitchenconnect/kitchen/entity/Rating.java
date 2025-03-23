package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Rating {
    private Long orderId;
    private int kitchenRating;
    private String userNote;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemRating> itemRatings = new ArrayList<>();
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

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

    public List<ItemRating> getItemRatings() {
        return itemRatings;
    }

    public void setItemRatings(List<ItemRating> itemRatings) {
        this.itemRatings = itemRatings;
    }

    
}
