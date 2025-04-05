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

    // Get all payments for a specific chef (through kitchen->user->chef relationship)
    @Query("SELECT p FROM Payment p " +
           "JOIN p.order o " +
           "JOIN o.kitchen k " +
           "JOIN k.user ku " +
           "JOIN Chef c ON ku.id = c.user.id " +
           "WHERE c.id = :chefId " +
           "AND p.paymentDate BETWEEN :start AND :end ")
    List<Payment> findByChefIdAndPaymentDateBetween(
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("chefId") Long chefId);
    
    // Get payments for a specific chef based on status (through kitchen->user->chef relationship)
    @Query("SELECT p FROM Payment p " +
           "JOIN p.order o " +
           "JOIN o.kitchen k " +
           "JOIN k.user ku " +
           "JOIN Chef c ON ku.id = c.user.id " +
           "WHERE c.id = :chefId " +
           "AND p.paymentDate BETWEEN :start AND :end " +
           "AND p.paymentStatus = :status")
    List<Payment> findByChefIdAndPaymentDateBetweenAndPaymentStatus(
        @Param("chefId") Long chefId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("status") PaymentStatus status);
    
    // Count payments for a specific chef
    @Query("SELECT COUNT(p) FROM Payment p " +
           "JOIN p.order o " +
           "JOIN o.kitchen k " +
           "JOIN k.user ku " +
           "JOIN Chef c ON ku.id = c.user.id " +
           "WHERE c.id = :chefId " +
           "AND p.paymentDate BETWEEN :start AND :end " +
           "AND p.paymentStatus = :status")
    int countByChefIdAndPaymentDateBetweenAndPaymentStatus(
        @Param("chefId") Long chefId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("status") PaymentStatus status);
    
    // Count distinct customers for a specific chef
    @Query("SELECT COUNT(DISTINCT o.user.id) FROM Payment p " +
           "JOIN p.order o " +
           "JOIN o.kitchen k " +
           "JOIN k.user ku " +
           "JOIN Chef c ON ku.id = c.user.id " +
           "WHERE c.id = :chefId " +
           "AND p.paymentDate BETWEEN :start AND :end " +
           "AND p.paymentStatus = :status")
    int countDistinctCustomersByChefIdAndPaymentDateBetweenAndPaymentStatus(
        @Param("chefId") Long chefId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("status") PaymentStatus status);
    
    // Sum payment amounts for a specific chef
    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p " +
           "JOIN p.order o " +
           "JOIN o.kitchen k " +
           "JOIN k.user ku " +
           "JOIN Chef c ON ku.id = c.user.id " +
           "WHERE c.id = :chefId " +
           "AND p.paymentDate BETWEEN :start AND :end " +
           "AND p.paymentStatus = :status")
    double sumAmountByChefIdAndPaymentDateBetweenAndPaymentStatus(
        @Param("chefId") Long chefId,
        @Param("start") LocalDateTime start,
        @Param("end") LocalDateTime end,
        @Param("status") PaymentStatus status);
    
}