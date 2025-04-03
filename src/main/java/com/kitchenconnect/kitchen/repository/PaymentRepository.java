package com.kitchenconnect.kitchen.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kitchenconnect.kitchen.entity.Payment;
import com.kitchenconnect.kitchen.enums.PaymentStatus;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    // Find payment by order ID
    Payment findByOrderId(Long orderId);
    
    // Check if payment exists for an order
    boolean existsByOrderId(Long orderId);
    
    // Count payments by status
    long countByPaymentStatus(PaymentStatus status);
    
    // Find payments within a date range
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // Find payments by status and date range
    List<Payment> findByPaymentStatusAndPaymentDateBetween(
        PaymentStatus status, 
        LocalDateTime startDate, 
        LocalDateTime endDate
    );
}