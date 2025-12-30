package org.carluselectum.ecommerce.presentation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.carluselectum.ecommerce.data.ProductRepository;
import org.carluselectum.ecommerce.model.Product;
import org.carluselectum.ecommerce.service.Buy2Get3Discount;
import org.carluselectum.ecommerce.service.DiscountStrategy;
import org.carluselectum.ecommerce.service.PercentageDiscount;

public class MainUI {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ProductRepository repo = new ProductRepository();
        List<Product> catalog = repo.getAllProducts();

        System.out.println(CYAN + "==========================================" + RESET);
        System.out.println(CYAN + "       ECOMMERCE STORE - CHECKOUT         " + RESET);
        System.out.println(CYAN + "==========================================" + RESET);

        if (catalog.isEmpty()) {
            System.out.println(YELLOW + "No products found." + RESET);
            return;
        }

        System.out.printf(BLUE + "%-5s | %-25s | %-10s\n" + RESET, "ID", "Product Name", "Price");
        System.out.println("------------------------------------------");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.printf("%-5d | %-25s | %.2f EUR\n", i + 1, catalog.get(i).getName(), catalog.get(i).getPrice());
        }
        System.out.println("------------------------------------------");

        int choice = -1;
        while (true) {
            System.out.print(YELLOW + "\nChoose product ID: " + RESET);
            if (input.hasNextInt()) {
                choice = input.nextInt();
                if (choice >= 1 && choice <= catalog.size()) {
                    break; 
                }
            } else {
                input.next(); // clear invalid token
            }
            System.out.println(RED + "Invalid ID. Please try again (1-" + catalog.size() + ")." + RESET);
        }
        
        Product selected = catalog.get(choice - 1);
        
        System.out.println(CYAN + "\n[ PROMOTIONS ]" + RESET);
        System.out.println("1. No Discount");
        System.out.println("2. Winter Sale (10% Off)");
        System.out.println("3. Campaign: Buy 2 Get 3");
        
        int promoChoice = -1;
        while (true) {
            System.out.print(YELLOW + "Select promo (1-3): " + RESET);
            if (input.hasNextInt()) {
                promoChoice = input.nextInt();
                if (promoChoice >= 1 && promoChoice <= 3) break;
            } else {
                input.next();
            }
            System.out.println(RED + "Invalid option. Select 1, 2, or 3." + RESET);
        }

        DiscountStrategy strategy;
        double baseTotal = selected.getPrice();
        int qty = 1;

        if (promoChoice == 3) {
            qty = 3;
            baseTotal = selected.getPrice() * qty;
            strategy = new Buy2Get3Discount();
        } else if (promoChoice == 2) {
            strategy = new PercentageDiscount();
        } else {
            strategy = (total, prices) -> total;
        }

        double finalPrice = strategy.applyDiscount(baseTotal, Arrays.asList(selected.getPrice(), selected.getPrice(), selected.getPrice()));
        double savings = baseTotal - finalPrice;

        System.out.println(CYAN + "\n==========================================" + RESET);
        System.out.println("             FINAL RECEIPT                ");
        System.out.println("==========================================");
        System.out.printf("Item:       %s\n", selected.getName());
        System.out.printf("Quantity:   %d\n", qty);
        System.out.printf("Subtotal:   %.2f EUR\n", baseTotal);
        if (savings > 0.01) {
            System.out.printf(YELLOW + "Discount:  -%.2f EUR\n" + RESET, savings);
        }
        System.out.println("------------------------------------------");
        System.out.printf(GREEN + "TOTAL TO PAY: %.2f EUR\n" + RESET, finalPrice);
        System.out.println(CYAN + "==========================================" + RESET);

        input.close();
    }
    
    public static final String RED = "\u001B[31m";
}