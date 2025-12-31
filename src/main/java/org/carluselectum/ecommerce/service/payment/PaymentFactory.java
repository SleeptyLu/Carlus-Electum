package org.carluselectum.ecommerce.service.payment;

public class PaymentFactory {
    /**
     * Factory Method to create the appropriate payment processor.
     */
    public static PaymentProcessor createPayment(int type) {
        return switch (type) {
            case 1 -> new PaypalProcessor();
            case 2 -> new CreditCardProcessor();
            default -> throw new IllegalArgumentException("Unknown payment method ID: " + type);
        };
    }
}