package org.carluselectum.ecommerce.model.cart;

import static org.junit.jupiter.api.Assertions.*;
import org.carluselectum.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class CartTest {
    private Cart cart;
    private Product mouse;
    private Product monitor;

    @BeforeEach
    void setUp() {
        cart = new Cart();
        mouse = new Product("3", "Rato Wireless", 45.0, 10);
        monitor = new Product("1", "Monitor Gaming", 150.0, 5);
    }

    // --- SUCCESS SCENARIOS ---
    @Test
    void testAddItem_ValidQuantity() {
        cart.addItem(mouse, 2);
        assertEquals(1, cart.getItems().size());
        assertEquals(2, cart.getItems().get(0).getQuantity());
    }

    @Test
    void testAddItem_Aggregation() {
        cart.addItem(mouse, 2);
        cart.addItem(mouse, 3);
        assertEquals(5, cart.getItems().get(0).getQuantity(), "Should aggregate quantities for the same product.");
    }

    @Test
    void testAddMultipleDifferentProducts() {
        cart.addItem(mouse, 1);
        cart.addItem(monitor, 1);
        assertEquals(2, cart.getItems().size(), "Cart should contain 2 distinct items.");
    }

    @Test
    void testAddItem_ExactStockLimit() {
        // Boundary Value Analysis: Testing exactly the maximum available stock
        cart.addItem(monitor, 5);
        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().get(0).getQuantity());
    }

    // --- ERROR AND BOUNDARY SCENARIOS ---
    @Test
    void testAddItem_ZeroQuantity_ShouldNotAdd() {
        cart.addItem(mouse, 0); // Below minimum
        assertTrue(cart.getItems().isEmpty(), "Zero quantity should not be allowed.");
    }

    @Test
    void testAddItem_NegativeQuantity_ShouldNotAdd() {
        cart.addItem(mouse, -5);
        assertTrue(cart.getItems().isEmpty(), "Negative quantity should be blocked.");
    }

    @Test
    void testAddItem_ExceedingStock_ShouldNotAdd() {
        cart.addItem(monitor, 6); // Above maximum
        assertTrue(cart.getItems().isEmpty(), "Should not allow quantity exceeding available stock.");
    }

    // --- STATE AND CALCULATION SCENARIOS ---
    @Test
    void testClearCart_ShouldBeEmpty() {
        cart.addItem(mouse, 1);
        cart.clear();
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after clear().");
    }

    @Test
    void testCalculateTotal_EmptyCart() {
        assertEquals(0.0, cart.calculateBaseTotal());
    }

    @Test
    void testGetAllItemPrices_SizeVerification() {
        cart.addItem(mouse, 2); // 2 units
        cart.addItem(monitor, 1); // 1 unit
        List<Double> prices = cart.getAllItemPrices();
        // Crucial for Buy3Pay2 strategy: must return 3 individual prices
        assertEquals(3, prices.size(), "Should return individual prices for each unit in cart.");
    }
}