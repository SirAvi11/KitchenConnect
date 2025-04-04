package com.kitchenconnect.kitchen.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kitchenconnect.kitchen.DTO.RevenueData;
import com.kitchenconnect.kitchen.service.RevenueService;

@Controller
@RequestMapping("/revenue")
public class RevenueController {
    
    @Autowired
    private RevenueService revenueService;
    
    @GetMapping("/data")
    @ResponseBody
    public RevenueData getRevenueData(
            @RequestParam String period,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        
        return revenueService.getRevenueData(period, startDate, endDate);
    }
}