package org.carluselectum.ecommerce.service.payment;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PaymentFactoryTest {

    // --- FACTORY CREATION TESTS ---
    @Test
    void testCreatePaypal_NotNull() {
        PaymentProcessor processor = PaymentFactory.createPayment(1);
        assertNotNull(processor, "Factory should not return null for valid PayPal ID.");
    }

    @Test
    void testCreatePaypal_CorrectType() {
        PaymentProcessor processor = PaymentFactory.createPayment(1);
        assertTrue(processor instanceof PaypalProcessor, "Should return an instance of PaypalProcessor.");
    }

    @Test
    void testCreateCreditCard_NotNull() {
        PaymentProcessor processor = PaymentFactory.createPayment(2);
        assertNotNull(processor, "Factory should not return null for valid Credit Card ID.");
    }

    @Test
    void testCreateCreditCard_CorrectType() {
        PaymentProcessor processor = PaymentFactory.createPayment(2);
        assertTrue(processor instanceof CreditCardProcessor, "Should return an instance of CreditCardProcessor.");
    }

    // --- FACTORY ERROR HANDLING (WHITE-BOX) ---
    @Test
    void testInvalidID_Negative_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> PaymentFactory.createPayment(-1),
                "Negative payment IDs must be rejected.");
    }

    @Test
    void testInvalidID_Zero_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> PaymentFactory.createPayment(0),
                "ID 0 should be treated as an invalid payment method.");
    }

    @Test
    void testInvalidID_OutOfRange_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> PaymentFactory.createPayment(99),
                "Large IDs outside supported range should throw exception.");
    }

    // --- PROCESSOR SIMULATION TESTS ---
    @Test
    void testPaypalProcessor_Process_ReturnsTrue() {
        PaymentProcessor paypal = PaymentFactory.createPayment(1);
        // Simulating a successful transaction
        assertTrue(paypal.process(100.0), "Paypal simulation should return true for positive amounts.");
    }

    @Test
    void testCreditCardProcessor_Process_ReturnsTrue() {
        PaymentProcessor card = PaymentFactory.createPayment(2);
        // Simulating a successful transaction
        assertTrue(card.process(250.0), "Credit Card simulation should return true for positive amounts.");
    }

    // --- INTERFACE COMPLIANCE TESTS ---
    @Test
    void testPaypal_ImplementsProcessorInterface() {
        PaymentProcessor p = new PaypalProcessor();
        assertTrue(p instanceof PaymentProcessor);
    }

    @Test
    void testCreditCard_ImplementsProcessorInterface() {
        PaymentProcessor p = new CreditCardProcessor();
        assertTrue(p instanceof PaymentProcessor);
    }
}