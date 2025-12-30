package org.carluselectum.ecommerce.service;

import java.util.Collections;
import java.util.List;

public class Buy2Get3Discount implements DiscountStrategy {
    @Override
    public double applyDiscount(double total, List<Double> prices) {
        if (prices.size() < 3) return total;
        
        Collections.sort(prices);
        int freeItems = prices.size() / 3;
        double discountValue = 0;

        for (int i = 0; i < freeItems; i++) {
            discountValue += prices.get(i);
        }
        
        return total - discountValue;
    }
}