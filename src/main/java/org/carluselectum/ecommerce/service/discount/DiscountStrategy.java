package org.carluselectum.ecommerce.service.discount;

import java.util.List;

public interface DiscountStrategy {
    double applyDiscount(double total, List<Double> prices);
}