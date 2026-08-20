package com.savebite.model;

import java.time.LocalDateTime;

public class ActivityRecord {

    private final LocalDateTime timestamp;
    private final String eventType;
    private final String referenceId;
    private final String productId;
    private final String productName;
    private final int quantity;
    private final double originalPrice;
    private final double saveBitePrice;
    private final double totalAmount;
    private final String status;
    private final String customerId;
    private final String sellerId;
    private final String details;

    public ActivityRecord(
            LocalDateTime timestamp,
            String eventType,
            String referenceId,
            String productId,
            String productName,
            int quantity,
            double originalPrice,
            double saveBitePrice,
            double totalAmount,
            String status,
            String customerId,
            String sellerId,
            String details) {

        this.timestamp = timestamp;
        this.eventType = eventType;
        this.referenceId = referenceId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.originalPrice = originalPrice;
        this.saveBitePrice = saveBitePrice;
        this.totalAmount = totalAmount;
        this.status = status;
        this.customerId = customerId;
        this.sellerId = sellerId;
        this.details = details;
    }

    public LocalDateTime getTimestamp() { return timestamp; }
    public String getEventType() { return eventType; }
    public String getReferenceId() { return referenceId; }
    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getOriginalPrice() { return originalPrice; }
    public double getSaveBitePrice() { return saveBitePrice; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getCustomerId() { return customerId; }
    public String getSellerId() { return sellerId; }
    public String getDetails() { return details; }
}
