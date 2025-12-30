package org.carluselectum.ecommerce.service;

import java.util.List;

public interface DiscountStrategy {
    double applyDiscount(double total, List<Double> prices);
}