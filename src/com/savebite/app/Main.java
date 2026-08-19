package com.savebite.app;

import java.time.LocalDateTime;

import com.savebite.model.Customer;
import com.savebite.model.FoodProduct;
import com.savebite.model.Order;
import com.savebite.model.Product;
import com.savebite.model.Seller;
import com.savebite.model.User;
import com.savebite.service.MarketplaceService;

public class Main {

    public static void main(String[] args) {

        Customer customerObject = new Customer(
                "C001",
                "Ali",
                "ali@email.com"
        );

        User customer = customerObject;

        User seller = new Seller(
                "S001",
                "Ahmed",
                "ahmed@restaurant.com",
                "Ahmed Restaurant",
                "Restaurant"
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

        MarketplaceService marketplace =
                new MarketplaceService();

        marketplace.addProduct(product);

        System.out.println("=== SAVEBITE ===");

        System.out.println();
        System.out.println("Customer: " + customer.getName());
        System.out.println("Role: " + customer.getRole());

        System.out.println();
        System.out.println("Seller: " + seller.getName());
        System.out.println("Role: " + seller.getRole());

        System.out.println();
        System.out.println("=== AVAILABLE DEAL ===");

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
                customerObject,
                "P001",
                2
        );

        System.out.println();
        System.out.println("=== RESERVATION SUCCESSFUL ===");

        System.out.println("Order ID: " + order.getId());
        System.out.println(
                "Product: " + order.getProduct().getName()
        );

        System.out.println(
                "Reserved Quantity: " + order.getQuantity()
        );

        System.out.printf(
                "Total Price: RM %.2f%n",
                order.calculateTotal()
        );

        System.out.println(
                "Status: " + order.getStatus()
        );

        System.out.println(
                "Quantity After Reservation: "
                        + product.getQuantity()
        );
    }
}