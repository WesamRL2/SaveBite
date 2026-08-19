package com.savebite.model;

import java.time.LocalDateTime;

public class Order {

    private String id;
    private Customer customer;
    private Product product;
    private int quantity;
    private double unitPrice;
    private LocalDateTime orderTime;
    private String status;

    public Order(
            String id,
            Customer customer,
            Product product,
            int quantity) {

        this.id = id;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.calculateFinalPrice();
        this.orderTime = LocalDateTime.now();
        this.status = "Reserved";
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public String getStatus() {
        return status;
    }

    public double calculateTotal() {
        return unitPrice * quantity;
    }

    public void markAsCollected() {
        status = "Collected";
    }

    public void cancel() {
        status = "Cancelled";
    }
}