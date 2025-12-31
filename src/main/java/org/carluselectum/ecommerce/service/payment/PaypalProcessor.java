package org.carluselectum.ecommerce.service.payment;

public class PaypalProcessor implements PaymentProcessor {
    @Override
    public boolean process(double amount) {
        System.out.println("\n[ PAYPAL ] Connecting to PayPal API...");
        System.out.printf("Successfully authorized payment of %.2f EUR.\n", amount);
        return true;
    }
}