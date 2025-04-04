package com.kitchenconnect.kitchen.DTO;

import java.util.List;

public class RevenueData {
    private List<String> labels;
    private List<Double> values;
    private double earnings;
    private int productSales;
    private int visitors;
    private double percentageChange;
    private boolean isIncrease;
    
    public List<String> getLabels() {
        return labels;
    }
    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    public List<Double> getValues() {
        return values;
    }
    public void setValues(List<Double> values) {
        this.values = values;
    }
    public double getEarnings() {
        return earnings;
    }
    public void setEarnings(double earnings) {
        this.earnings = earnings;
    }
    public int getProductSales() {
        return productSales;
    }
    public void setProductSales(int productSales) {
        this.productSales = productSales;
    }
    public int getVisitors() {
        return visitors;
    }
    public void setVisitors(int visitors) {
        this.visitors = visitors;
    }
    public double getPercentageChange() {
        return percentageChange;
    }
    public void setPercentageChange(double percentageChange) {
        this.percentageChange = percentageChange;
    }
    public boolean isIncrease() {
        return isIncrease;
    }
    public void setIncrease(boolean isIncrease) {
        this.isIncrease = isIncrease;
    }
    
    
}