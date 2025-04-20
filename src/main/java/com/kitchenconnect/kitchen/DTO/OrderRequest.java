package com.kitchenconnect.kitchen.DTO;

import java.util.List;

public class OrderRequest {
    private Long userId;
    private Long kitchenId;
    private String deliveryAddress;
    private String contactNumber;
    private String upiId;
    private String upiTransactionId;
    public Long getUserId() {
        return userId;
    }
    public String getUpiId() {
        return upiId;
    }
    public void setUpiId(String upiId) {
        this.upiId = upiId;
    }
    public String getUpiTransactionId() {
        return upiTransactionId;
    }
    public void setUpiTransactionId(String upiTransactionId) {
        this.upiTransactionId = upiTransactionId;
    }
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }
    public String getContactNumber() {
        return contactNumber;
    }
    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getKitchenId() {
        return kitchenId;
    }
    public void setKitchenId(Long kitchenId) {
        this.kitchenId = kitchenId;
    }
    private double totalAmount;
    private List<OrderItemRequest> items;

    public double getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    public List<OrderItemRequest> getItems() {
        return items;
    }
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
    }

}