package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "kitchens")
public class Kitchen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kitchen_id")
    private Long kitchenId;

    @OneToOne
    @JoinColumn(name = "chef_id", nullable = false, unique = true)
    private Chef chef; // Assuming a Chef entity exists

    @Column(name = "kitchen_name", length = 35, nullable = false)
    private String kitchenName;

    @Column(name = "kitchen_description", columnDefinition = "TEXT")
    private String kitchenDescription;

    @Column(name = "kitchen_image", columnDefinition = "TEXT")
    private String kitchenImage;

    @Column(name = "speciality", nullable = false)
    private String speciality;

    @Column(name = "overall_rating", nullable = false, precision = 3, scale = 1)
    private BigDecimal overallRating;

    @Column(name = "total_ratings_count", nullable = false)
    private int totalRatingsCount;

    @Column(name = "min_delivery_time", nullable = false)
    private int minDeliveryTime;

    @Column(name = "max_delivery_time", nullable = false)
    private int maxDeliveryTime;

    @Column(name = "available_days", length = 100, nullable = false)
    private String availableDays;

    @Column(name = "delivery_fees", nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFees;

    // Getters and Setters
    public Long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public String getKitchenName() {
        return kitchenName;
    }

    public void setKitchenName(String kitchenName) {
        this.kitchenName = kitchenName;
    }

    public String getKitchenDescription() {
        return kitchenDescription;
    }

    public void setKitchenDescription(String kitchenDescription) {
        this.kitchenDescription = kitchenDescription;
    }

    public String getKitchenImage() {
        return kitchenImage;
    }

    public void setKitchenImage(String kitchenImage) {
        this.kitchenImage = kitchenImage;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public BigDecimal getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(BigDecimal overallRating) {
        this.overallRating = overallRating;
    }

    public int getTotalRatingsCount() {
        return totalRatingsCount;
    }

    public void setTotalRatingsCount(int totalRatingsCount) {
        this.totalRatingsCount = totalRatingsCount;
    }

    public int getMinDeliveryTime() {
        return minDeliveryTime;
    }

    public void setMinDeliveryTime(int minDeliveryTime) {
        this.minDeliveryTime = minDeliveryTime;
    }

    public int getMaxDeliveryTime() {
        return maxDeliveryTime;
    }

    public void setMaxDeliveryTime(int maxDeliveryTime) {
        this.maxDeliveryTime = maxDeliveryTime;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public BigDecimal getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(BigDecimal deliveryFees) {
        this.deliveryFees = deliveryFees;
    }
}
