package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "fooditem")
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kitchen_id", nullable = false)
    private Kitchen kitchen;

    @Column(nullable = false)
    private String name;

    private String picture; 

    @Column(nullable = false)
    private double price;

    @Column(name = "overall_rating", columnDefinition = "DOUBLE DEFAULT 0.0")
    private double overallRating;

    @Column(name = "rating_count", columnDefinition = "INT DEFAULT 0")
    private int ratingCount;

    @Column(nullable = false)
    private boolean availability;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Kitchen getKitchen() { return kitchen; }
    public void setKitchen(Kitchen kitchen) { this.kitchen = kitchen; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public double getOverallRating() { return overallRating; }
    public void setOverallRating(double overallRating) { this.overallRating = overallRating; }

    public int getRatingCount() { return ratingCount; }
    public void setRatingCount(int ratingCount) { this.ratingCount = ratingCount; }

    public boolean isAvailability() { return availability; }
    public void setAvailability(boolean availability) { this.availability = availability; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}

