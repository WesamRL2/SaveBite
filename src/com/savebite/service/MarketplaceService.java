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

    public Order findOrderById(
            String orderId) {

        if (orderId == null) {
            return null;
        }

        for (Order order : orders) {

            if (order
                    .getId()
                    .equalsIgnoreCase(orderId)) {

                return order;
            }
        }

        return null;
    }

    public String generateProductId() {

        int number =
                products.size() + 1;

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

        Order order =
                new Order(
                        generateOrderId(),
                        customer,
                        product,
                        quantity
                );

        orders.add(order);

        return order;
    }

    public boolean cancelOrder(
            String orderId) {

        Order order =
                findOrderById(orderId);

        if (order == null) {
            return false;
        }

        if ("Cancelled".equalsIgnoreCase(
                order.getStatus())) {

            return false;
        }

        if ("Collected".equalsIgnoreCase(
                order.getStatus())) {

            return false;
        }

        order.getProduct()
                .increaseQuantity(
                        order.getQuantity()
                );

        order.cancel();

        return true;
    }

    public boolean collectOrder(
            String orderId) {

        Order order =
                findOrderById(orderId);

        if (order == null) {
            return false;
        }

        if (!"Reserved".equalsIgnoreCase(
                order.getStatus())) {

            return false;
        }

        order.markAsCollected();

        return true;
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

    public int getTotalOrders() {
        return orders.size();
    }

    public int getReservedItemCount() {

        int total = 0;

        for (Order order : orders) {

            if (!"Cancelled".equalsIgnoreCase(
                    order.getStatus())) {

                total += order.getQuantity();
            }
        }

        return total;
    }

    public int getAvailableItemCount() {

        int total = 0;

        for (Product product : products) {

            if (product.isAvailable()) {
                total += product.getQuantity();
            }
        }

        return total;
    }

    public double getRecoveredRevenue() {

        double total = 0;

        for (Order order : orders) {

            if (!"Cancelled".equalsIgnoreCase(
                    order.getStatus())) {

                total += order.calculateTotal();
            }
        }

        return total;
    }

    public double getCustomerSavings() {

        double total = 0;

        for (Order order : orders) {

            if (!"Cancelled".equalsIgnoreCase(
                    order.getStatus())) {

                double originalValue =
                        order.getProduct()
                                .getOriginalPrice()
                                * order.getQuantity();

                total +=
                        originalValue
                                - order.calculateTotal();
            }
        }

        return total;
    }

    public double getSurplusRescueRate() {

        int rescued =
                getReservedItemCount();

        int remaining =
                getAvailableItemCount();

        int total =
                rescued + remaining;

        if (total == 0) {
            return 0;
        }

        return (
                (double) rescued / total
        ) * 100;
    }
}