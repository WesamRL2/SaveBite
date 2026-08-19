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

        if (product == null
                || findProductById(product.getId()) != null) {

            return false;
        }

        products.add(product);

        return true;
    }

    public boolean removeProductById(
            String productId) {

        Product product =
                findProductById(productId);

        if (product == null) {
            return false;
        }

        return products.remove(product);
    }

    public Product findProductById(
            String productId) {

        if (productId == null) {
            return null;
        }

        for (Product product : products) {

            if (product
                    .getId()
                    .equalsIgnoreCase(productId)) {

                return product;
            }
        }

        return null;
    }

    public String generateProductId() {

        int number = products.size() + 1;

        String id =
                String.format(
                        "P%03d",
                        number
                );

        while (findProductById(id) != null) {

            number++;

            id = String.format(
                    "P%03d",
                    number
            );
        }

        return id;
    }

    public Order reserveProduct(
            Customer customer,
            String productId,
            int quantity) {

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Customer is required."
            );
        }

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than zero."
            );
        }

        Product product =
                findProductById(productId);

        if (product == null) {

            throw new IllegalArgumentException(
                    "Product not found."
            );
        }

        if (!product.isAvailable()) {

            throw new IllegalStateException(
                    "Product is not available."
            );
        }

        if (!product.reduceQuantity(quantity)) {

            throw new IllegalArgumentException(
                    "Requested quantity is not available."
            );
        }

        String orderId =
                generateOrderId();

        Order order =
                new Order(
                        orderId,
                        customer,
                        product,
                        quantity
                );

        orders.add(order);

        return order;
    }

    private String generateOrderId() {

        return String.format(
                "ORD%03d",
                orders.size() + 1
        );
    }

    public ArrayList<Product> getProducts() {

        return new ArrayList<>(
                products
        );
    }

    public ArrayList<Order> getOrders() {

        return new ArrayList<>(
                orders
        );
    }
}