package org.carluselectum.ecommerce.service.discount;

import java.util.List;

public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;

    public PercentageDiscount(double percentage) {
        this.percentage = Math.max(0, percentage);
    }

    @Override
    public double applyDiscount(double total, List<Double> prices) {
        if (total <= 0)
            return 0;

        double discount = total * (percentage / 100);
        return Math.max(0, total - discount);
    }
}