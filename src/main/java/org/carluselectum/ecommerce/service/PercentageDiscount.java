package org.carluselectum.ecommerce.service;

import java.util.List;


public class PercentageDiscount implements DiscountStrategy {
    private double percentage;

    public PercentageDiscount() {
        this.percentage = 0.10; 
    }

    @Override
    public double applyDiscount(double total, List<Double> prices) {
        return total * (1 - percentage);
    }
}