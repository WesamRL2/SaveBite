package com.savebite.service;

import java.util.ArrayList;

import com.savebite.model.Customer;
import com.savebite.model.Order;
import com.savebite.model.Product;

public class MarketplaceService {

    private final ArrayList<Product> products;
    private final ArrayList<Order> orders;

    public MarketplaceService() {
        products = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public boolean addProduct(Product product) {
        if (product == null || findProductById(product.getId()) != null) {
            return false;
        }

        products.add(product);
        return true;
    }

    public Product findProductById(String productId) {
        for (Product product : products) {
            if (product.getId().equalsIgnoreCase(productId)) {
                return product;
            }
        }

        return null;
    }

    public Order reserveProduct(
            Customer customer,
            String productId,
            int quantity) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer is required.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        Product product = findProductById(productId);

        if (product == null) {
            throw new IllegalArgumentException("Product not found.");
        }

        if (!product.isAvailable()) {
            throw new IllegalStateException("Product is sold out.");
        }

        if (!product.reduceQuantity(quantity)) {
            throw new IllegalArgumentException(
                    "Requested quantity is not available."
            );
        }

        String orderId = generateOrderId();

        Order order = new Order(
                orderId,
                customer,
                product,
                quantity
        );

        orders.add(order);

        return order;
    }

    private String generateOrderId() {
        return String.format("ORD%03d", orders.size() + 1);
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}