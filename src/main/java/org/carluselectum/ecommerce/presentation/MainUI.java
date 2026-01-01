package org.carluselectum.ecommerce.presentation;

import java.util.List;
import java.util.Scanner;
import org.carluselectum.ecommerce.data.ProductRepository;
import org.carluselectum.ecommerce.model.Product;
import org.carluselectum.ecommerce.model.auth.User;
import org.carluselectum.ecommerce.model.cart.Cart;
import org.carluselectum.ecommerce.service.auth.AuthService;
import org.carluselectum.ecommerce.service.discount.Buy2Get3Discount;
import org.carluselectum.ecommerce.service.discount.DiscountStrategy;
import org.carluselectum.ecommerce.service.discount.PercentageDiscount;
import org.carluselectum.ecommerce.service.payment.PaymentFactory;
import org.carluselectum.ecommerce.service.payment.PaymentProcessor;

public class MainUI {
    public static final String RESET = "\u001B[0m", GREEN = "\u001B[32m", YELLOW = "\u001B[33m",
            BLUE = "\u001B[34m", CYAN = "\u001B[36m", RED = "\u001B[31m";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        AuthService auth = new AuthService();
        User loggedInUser = null;

        // --- BLOCO DE AUTENTICAÇÃO (Mínimas alterações) ---
        while (loggedInUser == null) {
            System.out.println(CYAN + "\n=== CARLUS ELECTUM - ACESSO ===" + RESET);
            System.out.println("1. Login | 2. Criar Conta | 0. Sair");
            System.out.print(YELLOW + "Opção: " + RESET);
            int op = input.nextInt();
            if (op == 0)
                return;

            System.out.print("Email: ");
            String email = input.next();
            System.out.print("Password: ");
            String pass = input.next();

            if (op == 1) {
                loggedInUser = auth.login(email, pass);
                if (loggedInUser == null)
                    System.out.println(RED + "Credenciais erradas!" + RESET);
            } else {
                auth.register(email, pass);
                System.out.println(GREEN + "Conta criada! Faça login agora." + RESET);
            }
        }
        System.out.println(GREEN + "Bem-vindo, " + loggedInUser.getEmail() + "!" + RESET);

        // --- TEU CÓDIGO ORIGINAL CONTINUA AQUI ---
        ProductRepository repo = new ProductRepository();
        Cart cart = new Cart();

        System.out.println(CYAN + "\n=== CARLUS ELECTUM - ACTIVE PROMOTIONS ===" + RESET);
        System.out.println("- Winter Sale: 10% off on all items.");
        System.out.println("- Buy 3 Pay 2: Cheapest item is free when buying 3+ items.");

        while (true) {
            List<Product> catalog = repo.getAllProducts();
            displayCatalog(catalog);

            int choice = -1;
            while (true) {
                System.out.print(YELLOW + "\nSelect ID to add to cart (or 0 to checkout): " + RESET);
                if (input.hasNextInt()) {
                    choice = input.nextInt();
                    if (choice >= 0 && choice <= catalog.size()) {
                        break;
                    }
                } else {
                    input.next();
                }
                System.out.println(
                        RED + "Invalid ID! Choose between 1 and " + catalog.size() + " or 0 to finish." + RESET);
            }

            if (choice == 0)
                break;

            Product selected = catalog.get(choice - 1);
            System.out.print("Quantity to add: ");
            int qty = input.nextInt();

            if (qty > 0 && qty <= selected.getStock()) {
                cart.addItem(selected, qty);
                System.out.println(GREEN + "Added " + qty + "x " + selected.getName() + " to cart." + RESET);
            } else {
                System.out.println(RED + "Invalid quantity! Available stock: " + selected.getStock() + RESET);
            }
        }

        if (cart.getItems().isEmpty())
            return;

        System.out.println(CYAN + "\n[ AVAILABLE DISCOUNTS FOR YOUR CART ]" + RESET);
        System.out.println("1. No Discount");
        System.out.println("2. Apply Winter Sale (10%)");

        boolean canApplyB3P2 = cart.getAllItemPrices().size() >= 3;
        if (canApplyB3P2) {
            System.out.println("3. Apply Campaign: Buy 3 Pay 2 (Cheapest item free)");
        }

        int promoChoice = -1;
        while (true) {
            System.out.print(YELLOW + "Choose (1-3): " + RESET);
            if (input.hasNextInt()) {
                promoChoice = input.nextInt();
                if (promoChoice >= 1 && promoChoice <= 3)
                    break;
            } else {
                input.next();
            }
            System.out.println(RED + "Opção Inválida!" + RESET);
        }

        DiscountStrategy strategy = switch (promoChoice) {
            case 2 -> new PercentageDiscount(10.0);
            case 3 -> canApplyB3P2 ? new Buy2Get3Discount() : (t, p) -> t;
            default -> (total, prices) -> total;
        };

        double baseTotal = cart.calculateBaseTotal();
        double finalPrice = cart.calculateFinalTotal(strategy);

        showReceipt(cart, baseTotal, finalPrice);

        System.out.println(CYAN + "\n[ STEP 2: PAYMENT METHOD ]" + RESET);
        System.out.println("1. PayPal");
        System.out.println("2. Credit Card");
        System.out.print(YELLOW + "Choose method (1-2): " + RESET);
        int payMethod = input.nextInt();

        try {
            PaymentProcessor processor = PaymentFactory.createPayment(payMethod);
            System.out.print(YELLOW + "\nAuthorize payment of " + finalPrice + " EUR? (y/n): " + RESET);
            if (input.next().equalsIgnoreCase("y")) {
                if (processor.process(finalPrice)) {
                    cart.getItems().forEach(item -> repo.updateStock(item.getProduct().getId(), item.getQuantity()));
                    System.out.println(GREEN + "Order processed! Stock updated in database." + RESET);
                } else {
                    System.out.println(RED + "Payment failed. Transaction aborted." + RESET);
                }
            } else {
                System.out.println(RED + "Order cancelled." + RESET);
            }
        } catch (IllegalArgumentException e) {
            System.out.println(RED + "Error: " + e.getMessage() + RESET);
        }
        input.close();
    }

    private static void displayCatalog(List<Product> catalog) {
        System.out.printf(BLUE + "\n%-5s | %-25s | %-10s | %-5s\n" + RESET, "ID", "Product Name", "Price", "Stock");
        for (int i = 0; i < catalog.size(); i++) {
            Product p = catalog.get(i);
            System.out.printf("%-5d | %-25s | %.2f EUR | %-5d\n", i + 1, p.getName(), p.getPrice(), p.getStock());
        }
    }

    private static void showReceipt(Cart cart, double base, double total) {
        System.out.println(CYAN + "\n================ FINAL RECEIPT ================" + RESET);
        cart.getItems().forEach(item -> System.out.printf("%-25s x%-3d | %.2f EUR\n", item.getProduct().getName(),
                item.getQuantity(), item.getSubtotal()));
        System.out.println("-----------------------------------------------");
        System.out.printf("Subtotal: %.2f EUR\n", base);
        System.out.printf(YELLOW + "Savings:  -%.2f EUR\n" + RESET, (base - total));
        System.out.printf(GREEN + "TOTAL TO PAY: %.2f EUR\n" + RESET, total);
    }
}