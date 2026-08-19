package com.savebite.model;

public abstract class Product {

    private String id;
    private String name;
    private double originalPrice;
    private int quantity;
    private String sellerId;

    public Product(
            String id,
            String name,
            double originalPrice,
            int quantity,
            String sellerId) {

        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.quantity = quantity;
        this.sellerId = sellerId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOriginalPrice(double originalPrice) {

        if (originalPrice >= 0) {
            this.originalPrice = originalPrice;
        }
    }

    public void setQuantity(int quantity) {

        if (quantity >= 0) {
            this.quantity = quantity;
        }
    }

    public boolean reduceQuantity(int amount) {

        if (amount > 0 && amount <= quantity) {

            quantity -= amount;
            return true;
        }

        return false;
    }

    public void increaseQuantity(int amount) {

        if (amount > 0) {
            quantity += amount;
        }
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public abstract double calculateFinalPrice();

    public abstract String getProductType();
}