package org.carluselectum.ecommerce.service.payment;

/**
 * Common interface for all payment methods.
 */

public interface PaymentProcessor {
    boolean process(double amount);
}