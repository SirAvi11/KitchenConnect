package com.kitchenconnect.kitchen.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kitchenconnect.kitchen.DTO.PaymentRecordRequest;
import com.kitchenconnect.kitchen.DTO.RevenueData;
import com.kitchenconnect.kitchen.entity.Chef;
import com.kitchenconnect.kitchen.entity.Payment;
import com.kitchenconnect.kitchen.entity.User;
import com.kitchenconnect.kitchen.enums.PaymentStatus;
import com.kitchenconnect.kitchen.repository.ChefRepository;
import com.kitchenconnect.kitchen.repository.PaymentRepository;
import com.kitchenconnect.kitchen.service.RevenueService;

import jakarta.servlet.http.HttpSession;

@Service
public class RevenueServiceImpl implements RevenueService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ChefRepository chefRepository;
    
    @Autowired
    private HttpSession httpSession;
    
    private Long getCurrentChefId() {
        // Get user ID from session
        User sessionUser = (User) httpSession.getAttribute("loggedInUser");
        
        // Get chef ID from user ID
        Chef chef = chefRepository.findByUser(sessionUser)
                .orElseThrow(() -> new RuntimeException("Chef not found for user"));
        
        return chef.getChefId();
    }
    
    @Override
    public RevenueData getRevenueData(String period, String startDateStr, String endDateStr) {
        Long chefId = getCurrentChefId();
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        RevenueData data = new RevenueData();
        List<String> labels = generateDateLabels(period, startDate, endDate);
        // Ensure dates are formatted as ISO strings (YYYY-MM-DD)
        data.setLabels(labels.stream()
        .map(date -> date.toString()) // LocalDate.toString() gives ISO format
        .collect(Collectors.toList()));
        
        List<Double> values = getRevenueValues(period, startDate, endDate, chefId);
        data.setValues(values);
        
        data.setEarnings(calculateEarnings(values));
        data.setProductSales(getProductSales(period, startDate, endDate, chefId));
        data.setVisitors(getVisitorCount(period, startDate, endDate, chefId));
        
        double percentageChange = calculatePercentageChange(period, startDate, endDate, chefId);
        data.setPercentageChange(Math.abs(percentageChange));
        data.setIncrease(percentageChange >= 0);
        
        return data;
    }

    @Override
    public List<PaymentRecordRequest> getPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        Long chefId = getCurrentChefId();
        
        return paymentRepository.findByChefIdAndPaymentDateBetween(
            startDate.atStartOfDay(),
            endDate.atTime(23, 59, 59),
            chefId
        ).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private PaymentRecordRequest convertToDto(Payment payment) {
        PaymentRecordRequest dto = new PaymentRecordRequest();
        
        // Map all necessary fields from Payment to PaymentRecordRequest
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());
        dto.setChefAmount(payment.getChefAmount());
        dto.setPlatformFee(payment.getPlatformFee());
        dto.setStatus(payment.getPaymentStatus().toString());
        dto.setPaymentDate(payment.getPaymentDate());
        
        return dto;
    }
    
    @Override
    public List<String> generateDateLabels(String period, LocalDate start, LocalDate end) {
        List<String> labels = new ArrayList<>();
        
        if ("weekly".equals(period)) {
            LocalDate current = start;
            while (!current.isAfter(end)) {
                labels.add(current.toString());
                current = current.plusDays(1);
            }
        } else if ("monthly".equals(period)) {
            LocalDate current = start.with(TemporalAdjusters.firstDayOfMonth());
            while (!current.isAfter(end)) {
                labels.add(current.getMonth().toString() + " " + current.getYear());
                current = current.plusMonths(1);
            }
        } else { // yearly
            int startYear = start.getYear();
            int endYear = end.getYear();
            for (int year = startYear; year <= endYear; year++) {
                labels.add(String.valueOf(year));
            }
        }
        
        return labels;
    }
    
    @Override
    public List<Double> getRevenueValues(String period, LocalDate start, LocalDate end, Long chefId) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        List<Payment> payments = paymentRepository.findByChefIdAndPaymentDateBetweenAndPaymentStatus(
            chefId, startDateTime, endDateTime, PaymentStatus.COMPLETED);
        
        if ("weekly".equals(period)) {
            return calculateDailyRevenue(payments, start, end, true);
        } else if ("monthly".equals(period)) {
            return calculateMonthlyRevenue(payments, start, end, true);
        } else {
            return calculateYearlyRevenue(payments, start, end, true);
        }
    }
    
    private List<Double> calculateDailyRevenue(List<Payment> payments, LocalDate start, LocalDate end,boolean isChef) {
        List<Double> dailyRevenue = new ArrayList<>();
        LocalDate current = start;
        
        while (!current.isAfter(end)) {
            final LocalDate date = current;
            double total = payments.stream()
                .filter(p -> p.getPaymentDate().toLocalDate().equals(date))
                .mapToDouble(p -> isChef ? p.getChefAmount() : p.getPlatformFee())
                .sum();
            dailyRevenue.add(total);
            current = current.plusDays(1);
        }
        
        return dailyRevenue;
    }
    
    private List<Double> calculateMonthlyRevenue(List<Payment> payments, LocalDate start, LocalDate end, boolean isChef) {
        List<Double> monthlyRevenue = new ArrayList<>();
        LocalDate current = start.with(TemporalAdjusters.firstDayOfMonth());
        
        while (!current.isAfter(end)) {
            final int month = current.getMonthValue();
            final int year = current.getYear();
            
            double total = payments.stream()
                .filter(p -> {
                    LocalDate paymentDate = p.getPaymentDate().toLocalDate();
                    return paymentDate.getMonthValue() == month && 
                           paymentDate.getYear() == year;
                })
                .mapToDouble(p -> isChef ? p.getChefAmount() : p.getPlatformFee())
                .sum();
            
            monthlyRevenue.add(total);
            current = current.plusMonths(1);
        }
        
        return monthlyRevenue;
    }
    
    private List<Double> calculateYearlyRevenue(List<Payment> payments, LocalDate start, LocalDate end, boolean isChef) {
        List<Double> yearlyRevenue = new ArrayList<>();
        int startYear = start.getYear();
        int endYear = end.getYear();
        
        for (int year = startYear; year <= endYear; year++) {
            final int y = year;
            double total = payments.stream()
                .filter(p -> p.getPaymentDate().getYear() == y)
                .mapToDouble(p -> isChef ? p.getChefAmount() : p.getPlatformFee())
                .sum();
            yearlyRevenue.add(total);
        }
        
        return yearlyRevenue;
    }
    
    @Override
    public int getProductSales(String period, LocalDate start, LocalDate end, Long chefId) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        return paymentRepository.countByChefIdAndPaymentDateBetweenAndPaymentStatus(
            chefId, startDateTime, endDateTime, PaymentStatus.COMPLETED);
    }
    
    @Override
    public int getVisitorCount(String period, LocalDate start, LocalDate end, Long chefId) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        return paymentRepository.countDistinctCustomersByChefIdAndPaymentDateBetweenAndPaymentStatus(
            chefId, startDateTime, endDateTime, PaymentStatus.COMPLETED);
    }
    
    @Override
    public double calculatePercentageChange(String period, LocalDate start, LocalDate end, Long chefId) {
        LocalDateTime currentStart = start.atStartOfDay();
        LocalDateTime currentEnd = end.atTime(23, 59, 59);
        
        double currentTotal = paymentRepository.sumAmountByChefIdAndPaymentDateBetweenAndPaymentStatus(
            chefId, currentStart, currentEnd, PaymentStatus.COMPLETED);
        
        LocalDateTime previousStart;
        LocalDateTime previousEnd;
        
        if ("weekly".equals(period)) {
            previousStart = currentStart.minusWeeks(1);
            previousEnd = currentEnd.minusWeeks(1);
        } else if ("monthly".equals(period)) {
            previousStart = currentStart.minusMonths(1);
            previousEnd = currentEnd.minusMonths(1);
        } else {
            previousStart = currentStart.minusYears(1);
            previousEnd = currentEnd.minusYears(1);
        }
        
        double previousTotal = paymentRepository.sumAmountByChefIdAndPaymentDateBetweenAndPaymentStatus(
            chefId, previousStart, previousEnd, PaymentStatus.COMPLETED);
        
        if (previousTotal == 0) {
            return currentTotal == 0 ? 0 : 100;
        }
        
        double percentageChange = ((currentTotal - previousTotal) / previousTotal) * 100;
    
        // Round to 2 decimal places
        return Math.round(percentageChange * 100.0) / 100.0;
    }
    
    @Override
    public double calculateEarnings(List<Double> values) {
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        return Math.round(sum * 100.0) / 100.0;
    }

    @Override
    public RevenueData getPlatformRevenueData(String period, String startDateStr, String endDateStr) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        LocalDate endDate = LocalDate.parse(endDateStr);
        
        RevenueData data = new RevenueData();
        List<String> labels = generateDateLabels(period, startDate, endDate);
        data.setLabels(labels.stream()
            .map(date -> date.toString())
            .collect(Collectors.toList()));
        
        List<Double> values = getPlatformRevenueValues(period, startDate, endDate);
        data.setValues(values);
        
        data.setEarnings(calculateEarnings(values));
        data.setProductSales(getPlatformProductSales(period, startDate, endDate));
        data.setVisitors(getPlatformVisitorCount(period, startDate, endDate));
        
        double percentageChange = calculatePlatformPercentageChange(period, startDate, endDate);
        data.setPercentageChange(Math.abs(percentageChange));
        data.setIncrease(percentageChange >= 0);
        
        return data;
    }

    @Override
    public List<PaymentRecordRequest> getPlatformPaymentsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return paymentRepository.findByPaymentDateBetween(
            startDate.atStartOfDay(),
            endDate.atTime(23, 59, 59)
        ).stream()
        .map(this::convertToDto)
        .collect(Collectors.toList());
    }

    // Helper methods for platform revenue calculations
    private List<Double> getPlatformRevenueValues(String period, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        List<Payment> payments = paymentRepository.findByPaymentDateBetweenAndPaymentStatus(
            startDateTime, endDateTime, PaymentStatus.COMPLETED);
        
        if ("weekly".equals(period)) {
            return calculateDailyRevenue(payments, start, end, false);
        } else if ("monthly".equals(period)) {
            return calculateMonthlyRevenue(payments, start, end, false);
        } else {
            return calculateYearlyRevenue(payments, start, end, false);
        }
    }

    private int getPlatformProductSales(String period, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        return paymentRepository.countByPaymentDateBetweenAndPaymentStatus(
            startDateTime, endDateTime, PaymentStatus.COMPLETED);
    }

    private int getPlatformVisitorCount(String period, LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);
        
        return paymentRepository.countDistinctCustomersByPaymentDateBetweenAndPaymentStatus(
            startDateTime, endDateTime, PaymentStatus.COMPLETED);
    }

    private double calculatePlatformPercentageChange(String period, LocalDate start, LocalDate end) {
        LocalDateTime currentStart = start.atStartOfDay();
        LocalDateTime currentEnd = end.atTime(23, 59, 59);
        
        double currentTotal = paymentRepository.sumAmountByPaymentDateBetweenAndPaymentStatus(
            currentStart, currentEnd, PaymentStatus.COMPLETED);
        
        LocalDateTime previousStart;
        LocalDateTime previousEnd;
        
        if ("weekly".equals(period)) {
            previousStart = currentStart.minusWeeks(1);
            previousEnd = currentEnd.minusWeeks(1);
        } else if ("monthly".equals(period)) {
            previousStart = currentStart.minusMonths(1);
            previousEnd = currentEnd.minusMonths(1);
        } else {
            previousStart = currentStart.minusYears(1);
            previousEnd = currentEnd.minusYears(1);
        }
        
        double previousTotal = paymentRepository.sumAmountByPaymentDateBetweenAndPaymentStatus(
            previousStart, previousEnd, PaymentStatus.COMPLETED);
        
        if (previousTotal == 0) {
            return currentTotal == 0 ? 0 : 100;
        }
        
        double percentageChange = ((currentTotal - previousTotal) / previousTotal) * 100;
    
        // Round to 2 decimal places
        return Math.round(percentageChange * 100.0) / 100.0;
    }
}