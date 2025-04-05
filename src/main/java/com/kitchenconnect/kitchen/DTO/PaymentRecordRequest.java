package com.kitchenconnect.kitchen.DTO;

import java.time.LocalDateTime;

public class PaymentRecordRequest {
    private Long id;
    private Long orderId;
    private Double amount;
    private Double platformFee;
    private String status;
    private LocalDateTime paymentDate;
    
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
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Double getPlatformFee() {
        return platformFee;
    }
    public void setPlatformFee(Double platformFee) {
        this.platformFee = platformFee;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

}
