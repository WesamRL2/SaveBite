package com.savebite.model;

import java.time.LocalDateTime;

public class FoodProduct extends Product {

    private String category;
    private double discountPercentage;
    private LocalDateTime pickupDeadline;

    public FoodProduct(
            String id,
            String name,
            double originalPrice,
            int quantity,
            String sellerId,
            String category,
            double discountPercentage,
            LocalDateTime pickupDeadline) {

        super(id, name, originalPrice, quantity, sellerId);

        this.category = category;
        setDiscountPercentage(discountPercentage);
        this.pickupDeadline = pickupDeadline;
    }

    public String getCategory() {
        return category;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public LocalDateTime getPickupDeadline() {
        return pickupDeadline;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDiscountPercentage(double discountPercentage) {
        if (discountPercentage >= 0 && discountPercentage <= 100) {
            this.discountPercentage = discountPercentage;
        }
    }

    public void setPickupDeadline(LocalDateTime pickupDeadline) {
        this.pickupDeadline = pickupDeadline;
    }

    @Override
    public double calculateFinalPrice() {
        return getOriginalPrice()
                - (getOriginalPrice() * discountPercentage / 100);
    }

    @Override
    public String getProductType() {
        return "Food";
    }

    public boolean isPickupExpired() {
        return LocalDateTime.now().isAfter(pickupDeadline);
    }
}