package org.carluselectum.ecommerce.service.discount;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class DiscountTest {

    // --- WINTER SALE (PERCENTAGE) TESTS ---
    @Test
    void testPercentageDiscount_AppliedCorrecty() {
        PercentageDiscount strategy = new PercentageDiscount(10.0);
        assertEquals(90.0, strategy.applyDiscount(100.0, null), 0.01);
    }

    @Test
    void testPercentageDiscount_ZeroTotal() {
        PercentageDiscount strategy = new PercentageDiscount(10.0);
        assertEquals(0.0, strategy.applyDiscount(0.0, null), "Discount on zero should be zero.");
    }

    @Test
    void testPercentageDiscount_FullDiscount() {
        PercentageDiscount strategy = new PercentageDiscount(100.0);
        assertEquals(0.0, strategy.applyDiscount(500.0, null), "100% discount should result in zero price.");
    }

    // --- BUY 3 PAY 2 (CAMPAIGN) TESTS - WHITE-BOX SCENARIOS ---
    @Test
    void testBuy2Get3_ExactThreeItems() {
        Buy2Get3Discount strategy = new Buy2Get3Discount();
        List<Double> prices = Arrays.asList(10.0, 20.0, 30.0);
        // Cheapest item (10.0) should be free
        assertEquals(50.0, strategy.applyDiscount(60.0, prices), 0.01);
    }

    @Test
    void testBuy2Get3_SixItems_TwoCheapestFree() {
        Buy2Get3Discount strategy = new Buy2Get3Discount();
        // Two groups of 3: two cheapest (10.0 and 10.0) should be free
        List<Double> prices = Arrays.asList(10.0, 10.0, 50.0, 50.0, 100.0, 100.0);
        assertEquals(300.0, strategy.applyDiscount(320.0, prices), 0.01);
    }

    @Test
    void testBuy2Get3_NotEnoughItems_NoDiscount() {
        Buy2Get3Discount strategy = new Buy2Get3Discount();
        List<Double> prices = Arrays.asList(100.0, 200.0); // Only 2 items
        assertEquals(300.0, strategy.applyDiscount(300.0, prices), "No discount for less than 3 items.");
    }

    @Test
    void testBuy2Get3_EmptyList_ReturnsOriginalTotal() {
        Buy2Get3Discount strategy = new Buy2Get3Discount();
        assertEquals(100.0, strategy.applyDiscount(100.0, new ArrayList<>()));
    }

    @Test
    void testBuy2Get3_NullList_ShouldNotCrash() {
        Buy2Get3Discount strategy = new Buy2Get3Discount();
        assertDoesNotThrow(() -> strategy.applyDiscount(100.0, null));
    }

    @Test
    void testBuy2Get3_AllItemsSamePrice() {
        Buy2Get3Discount strategy = new Buy2Get3Discount();
        List<Double> prices = Arrays.asList(10.0, 10.0, 10.0);
        assertEquals(20.0, strategy.applyDiscount(30.0, prices), 0.01);
    }

    // --- NO DISCOUNT STRATEGY ---
    @Test
    void testNoDiscount_ReturnsBaseTotal() {
        DiscountStrategy strategy = (total, prices) -> total; // Identity strategy
        assertEquals(150.0, strategy.applyDiscount(150.0, null));
    }
}