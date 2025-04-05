package com.kitchenconnect.kitchen.service;

import java.time.LocalDate;
import java.util.List;

import com.kitchenconnect.kitchen.DTO.PaymentRecordRequest;
import com.kitchenconnect.kitchen.DTO.RevenueData;

public interface RevenueService {

    RevenueData getRevenueData(String period, String startDateStr, String endDateStr);
    List<String> generateDateLabels(String period, LocalDate start, LocalDate end);
    List<Double> getRevenueValues(String period, LocalDate start, LocalDate end, Long chefId);
    int getProductSales(String period, LocalDate start, LocalDate end, Long chefId);
    int getVisitorCount(String period, LocalDate start, LocalDate end, Long chefId);
    double calculatePercentageChange(String period, LocalDate start, LocalDate end, Long chefId);
    double calculateEarnings(List<Double> values);
    List<PaymentRecordRequest> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate);
}
