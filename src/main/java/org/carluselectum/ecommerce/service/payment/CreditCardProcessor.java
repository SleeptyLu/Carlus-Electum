package org.carluselectum.ecommerce.service.payment;

public class CreditCardProcessor implements PaymentProcessor {
    @Override
    public boolean process(double amount) {
        System.out.println("\n[ CREDIT CARD ] Processing transaction via Gateway...");
        System.out.printf("Payment of %.2f EUR approved by the bank.\n", amount);
        return true;
    }
}