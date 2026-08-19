package com.savebite.app;

import java.time.LocalDateTime;

import com.savebite.model.Customer;
import com.savebite.model.FoodProduct;
import com.savebite.model.Product;
import com.savebite.model.Seller;
import com.savebite.model.User;

public class Main {

    public static void main(String[] args) {

        // Test User inheritance and polymorphism
        User customer = new Customer(
                "C001",
                "Ali",
                "ali@email.com"
        );

        User seller = new Seller(
                "S001",
                "Ahmed",
                "ahmed@restaurant.com",
                "Ahmed Restaurant",
                "Restaurant"
        );

        System.out.println("=== SAVEBITE USER TEST ===");
        System.out.println("User 1: " + customer.getName());
        System.out.println("Role: " + customer.getRole());

        System.out.println();

        System.out.println("User 2: " + seller.getName());
        System.out.println("Role: " + seller.getRole());

        // Test Product inheritance and polymorphism
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

        System.out.println();
        System.out.println("=== SAVEBITE PRODUCT TEST ===");
        System.out.println("Product: " + product.getName());
        System.out.printf("Original Price: RM %.2f%n", product.getOriginalPrice());
        System.out.printf("Final Price: RM %.2f%n", product.calculateFinalPrice());
        System.out.println("Quantity: " + product.getQuantity());
        System.out.println("Available: " + product.isAvailable());
        System.out.println("Product Type: " + product.getProductType());
    }
}