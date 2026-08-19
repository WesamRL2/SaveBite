package com.savebite.app;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.savebite.model.Customer;
import com.savebite.model.FoodProduct;
import com.savebite.model.Order;
import com.savebite.model.Product;
import com.savebite.service.MarketplaceService;
import com.savebite.storage.FileManager;

public class Main {

    public static void main(String[] args) {

        Customer customer = new Customer(
                "C001",
                "Ali",
                "ali@email.com"
        );

        Product product = new FoodProduct(
                "P001",
                "Chicken Meal",
                20.00,
                5,
                "S001",
                "Meals",
                40.0,
                LocalDateTime.now().plusHours(3)
        );

        MarketplaceService marketplace = new MarketplaceService();

        marketplace.addProduct(product);

        System.out.println("=== SAVEBITE ===");

        System.out.println();
        System.out.println("Product: " + product.getName());
        System.out.printf(
                "Original Price: RM %.2f%n",
                product.getOriginalPrice()
        );
        System.out.printf(
                "SaveBite Price: RM %.2f%n",
                product.calculateFinalPrice()
        );

        System.out.println(
                "Quantity Before Reservation: "
                        + product.getQuantity()
        );

        Order order = marketplace.reserveProduct(
                customer,
                "P001",
                2
        );

        System.out.println();
        System.out.println("=== RESERVATION SUCCESSFUL ===");
        System.out.println("Order ID: " + order.getId());
        System.out.println("Reserved Quantity: " + order.getQuantity());

        System.out.printf(
                "Total Price: RM %.2f%n",
                order.calculateTotal()
        );

        System.out.println(
                "Quantity After Reservation: "
                        + product.getQuantity()
        );

        try {

            FileManager.saveProducts(
                    marketplace.getProducts()
            );

            System.out.println();
            System.out.println(
                    "Products saved successfully."
            );

            ArrayList<Product> loadedProducts =
                    FileManager.loadProducts();

            System.out.println();
            System.out.println("=== LOADED FROM FILE ===");

            for (Product loadedProduct : loadedProducts) {

                System.out.println(
                        "Product: "
                                + loadedProduct.getName()
                );

                System.out.println(
                        "Quantity: "
                                + loadedProduct.getQuantity()
                );

                System.out.printf(
                        "Price: RM %.2f%n",
                        loadedProduct.calculateFinalPrice()
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "File error: " + e.getMessage()
            );
        }
    }
}