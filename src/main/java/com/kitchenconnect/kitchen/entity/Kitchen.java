package com.kitchenconnect.kitchen.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kitchenconnect.kitchen.enums.KitchenStatus;

@Entity
@Table(name = "kitchens")
public class Kitchen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kitchen_id")
    private Long kitchenId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true) // Foreign key reference to User
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private KitchenStatus status = KitchenStatus.UNDER_VERIFICATION;

    @OneToMany(mappedBy = "kitchen", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonBackReference // Prevents circular reference
    private List<Category> categories;

    @OneToMany(mappedBy = "kitchen", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Order> orders;

    @Column(name = "kitchen_name", length = 35, nullable = false)
    private String kitchenName;

    @Column(columnDefinition = "TEXT")
    private String kitchenDescription;

    @Column(name = "kitchen_image_path")
    private String kitchenImagePath;

    @Column(nullable = false, precision = 3, scale = 1)
    private BigDecimal overallRating = BigDecimal.ZERO;

    @Column(nullable = false)
    private int totalRatingsCount;

    @Column(nullable = false)
    private int minDeliveryTime;

    @Column(nullable = false)
    private int maxDeliveryTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal deliveryFees;

    // Address fields
    @Column(nullable = false)
    private String shopName;

    @Column
    private String floor;

    @Column(nullable = false)
    private String area;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    // Kitchen Menu & Operation Details
    @Column(name = "menu_image_paths", columnDefinition = "TEXT")
    private String menuImagePaths;

    @ElementCollection
    @CollectionTable(name = "kitchen_cuisines", joinColumns = @JoinColumn(name = "kitchen_id"))
    @Column(name = "cuisine")
    private List<String> selectedCuisines = new ArrayList<>();

    @Column(name = "open_days", columnDefinition = "TEXT")
    private String openDays;

    @Column(nullable = false)
    private String openTime;

    @Column(nullable = false)
    private String closeTime;

    // Kitchen Documents
    @Column(unique = true, nullable = false)
    private String fssaiNumber;

    @Column(nullable = false)
    private String fssaiExpiryDate;

    @Column(columnDefinition = "TEXT")
    private String fssaiDocumentPath;

    @Column(unique = true, nullable = false)
    private String panNumber;

    @Column(columnDefinition = "TEXT")
    private String panDocumentPath;

    // Partner Contract
    @Column(nullable = false)
    private boolean acceptTerms;

    // Getters and Setters

    public Long getKitchenId() {
        return kitchenId;
    }

    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public KitchenStatus getStatus() {
        return status;
    }

    public void setStatus(KitchenStatus status) {
        this.status = status;
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

    public String getKitchenImagePath() {
        return kitchenImagePath;
    }

    public void setKitchenImagePath(String kitchenImagePath) {
        this.kitchenImagePath = kitchenImagePath;
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

    public BigDecimal getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(BigDecimal deliveryFees) {
        this.deliveryFees = deliveryFees;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<String> getMenuImagePaths() {
        return menuImagePaths != null ? new ArrayList<>(Arrays.asList(menuImagePaths.split(","))) : new ArrayList<>();
    }

    public void setMenuImagePaths(List<String> paths) {
        this.menuImagePaths = String.join(",", paths);
    }

    public List<String> getSelectedCuisines() {
        return new ArrayList<>(selectedCuisines); // Return a mutable copy
    }

    public void setSelectedCuisines(List<String> selectedCuisines) {
        this.selectedCuisines = new ArrayList<>(selectedCuisines); // Set a mutable copy
    }

    public List<String> getOpenDays() {
        return openDays != null ? new ArrayList<>(Arrays.asList(openDays.split(","))) : new ArrayList<>();
    }

    public void setOpenDays(List<String> openDays) {
        this.openDays = String.join(",", openDays);
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getFssaiNumber() {
        return fssaiNumber;
    }

    public void setFssaiNumber(String fssaiNumber) {
        this.fssaiNumber = fssaiNumber;
    }

    public String getFssaiExpiryDate() {
        return fssaiExpiryDate;
    }

    public void setFssaiExpiryDate(String fssaiExpiryDate) {
        this.fssaiExpiryDate = fssaiExpiryDate;
    }

    public String getFssaiDocumentPath() {
        return fssaiDocumentPath;
    }

    public void setFssaiDocumentPath(String fssaiDocumentPath) {
        this.fssaiDocumentPath = fssaiDocumentPath;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getPanDocumentPath() {
        return panDocumentPath;
    }

    public void setPanDocumentPath(String panDocumentPath) {
        this.panDocumentPath = panDocumentPath;
    }

    public boolean isAcceptTerms() {
        return acceptTerms;
    }

    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }
}