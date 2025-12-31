package org.carluselectum.ecommerce.model.cart;

import java.util.ArrayList;
import java.util.List;

import org.carluselectum.ecommerce.model.Product;
import org.carluselectum.ecommerce.service.discount.DiscountStrategy;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                int totalNewQty = item.getQuantity() + quantity;

                if (totalNewQty <= product.getStock()) {
                    item.setQuantity(totalNewQty);
                }
                return;
            }
        }

        if (quantity <= product.getStock()) {
            items.add(new CartItem(product, quantity));
        }
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public double calculateBaseTotal() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public List<Double> getAllItemPrices() {
        List<Double> prices = new ArrayList<>();
        for (CartItem item : items) {
            for (int i = 0; i < item.getQuantity(); i++) {
                prices.add(item.getProduct().getPrice());
            }
        }
        return prices;
    }

    public double calculateFinalTotal(DiscountStrategy strategy) {
        double baseTotal = calculateBaseTotal();
        return strategy.applyDiscount(baseTotal, getAllItemPrices());
    }
}