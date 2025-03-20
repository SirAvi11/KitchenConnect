package com.kitchenconnect.kitchen.enums;

public enum OrderStatus {
    PENDING,        // Order is placed but not yet processed
    PREPARING,     // Order is being prepared
    READY,      // Order is completed and delivered
    DELIVERED,      // Order is cancelled
    CANCELLED  // Order is delivered to the customer
}
