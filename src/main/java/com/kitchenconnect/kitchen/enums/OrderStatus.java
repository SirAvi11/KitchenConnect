package com.kitchenconnect.kitchen.enums;

public enum OrderStatus {
    PENDING,        // Order is placed but not yet processed
    PROCESSING,     // Order is being prepared
    COMPLETED,      // Order is completed and delivered
    CANCELLED,      // Order is cancelled
    DELIVERED       // Order is delivered to the customer
}
