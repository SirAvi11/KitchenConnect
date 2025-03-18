package com.kitchenconnect.kitchen.DTO;

import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class KitchenRequest {

    // User Id
    private Long userId;

    // Kitchen Information
    private String kitchenName;
    private String kitchenDescription;

    // Kitchen Address
    private String shopName;
    private String floor;
    private String area;
    private String city;
    private String phoneNumber;

    // Menu and Operational Details
    private List<MultipartFile> menuImages = new ArrayList<>(); // List to store MultipartFile objects
    private List<String> menuImagePaths = new ArrayList<>();   // Multiple images path for menu
    private MultipartFile kitchenImage; // Field to store the kitchen image
    private String kitchenImagePath;    // Single kitchen path profile image
    private List<String> selectedCuisines = new ArrayList<>(); // Selected cuisines
    private List<String> openDays = new ArrayList<>();         // Selected open days
    private String openTime;            // Kitchen opening time
    private String closeTime;           // Kitchen closing time
    private BigDecimal deliveryFees;    // Delivery fees

    // Kitchen Documents
    private String fssaiNumber;
    private String fssaiExpiryDate;
    private MultipartFile fssaiDocument; // FSSAI document
    private String fssaiDocumentPath;    // FSSAI document upload
    private MultipartFile panDocument;   // PAN document
    private String panNumber;
    private String panDocumentPath;     // PAN document upload

    // Partner Contract
    private boolean acceptTerms;        // Checkbox for accepting terms

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        return new ArrayList<>(menuImagePaths); // Return a mutable copy
    }

    public void setMenuImagePaths(List<String> menuImagePaths) {
        this.menuImagePaths = new ArrayList<>(menuImagePaths); // Set a mutable copy
    }

    public String getKitchenImagePath() {
        return kitchenImagePath;
    }

    public void setKitchenImagePath(String kitchenImagePath) {
        this.kitchenImagePath = kitchenImagePath;
    }

    public List<String> getSelectedCuisines() {
        return new ArrayList<>(selectedCuisines); // Return a mutable copy
    }

    public void setSelectedCuisines(List<String> selectedCuisines) {
        this.selectedCuisines = new ArrayList<>(selectedCuisines); // Set a mutable copy
    }

    public List<String> getOpenDays() {
        return new ArrayList<>(openDays); // Return a mutable copy
    }

    public void setOpenDays(List<String> openDays) {
        this.openDays = new ArrayList<>(openDays); // Set a mutable copy
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

    public BigDecimal getDeliveryFees() {
        return deliveryFees;
    }

    public void setDeliveryFees(BigDecimal deliveryFees) {
        this.deliveryFees = deliveryFees;
    }

    public List<MultipartFile> getMenuImages() {
        return new ArrayList<>(menuImages); // Return a mutable copy
    }

    public void setMenuImages(List<MultipartFile> menuImages) {
        this.menuImages = new ArrayList<>(menuImages); // Set a mutable copy
    }

    public MultipartFile getKitchenImage() {
        return kitchenImage;
    }

    public void setKitchenImage(MultipartFile kitchenImage) {
        this.kitchenImage = kitchenImage;
    }

    public MultipartFile getFssaiDocument() {
        return fssaiDocument;
    }

    public void setFssaiDocument(MultipartFile fssaiDocument) {
        this.fssaiDocument = fssaiDocument;
    }

    public MultipartFile getPanDocument() {
        return panDocument;
    }

    public void setPanDocument(MultipartFile panDocument) {
        this.panDocument = panDocument;
    }
}