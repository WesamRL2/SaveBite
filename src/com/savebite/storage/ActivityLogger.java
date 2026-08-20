package com.savebite.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.savebite.model.ActivityRecord;
import com.savebite.model.FoodProduct;
import com.savebite.model.Order;
import com.savebite.model.Product;

public final class ActivityLogger {

    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path ACTIVITY_FILE = DATA_DIRECTORY.resolve("activity.tsv");

    private ActivityLogger() {
    }

    public static void logProductAdded(Product product) {

        String details = "Product listed";

        if (product instanceof FoodProduct foodProduct) {
            details = "Category: " + foodProduct.getCategory()
                    + "; Pickup deadline: " + foodProduct.getPickupDeadline();
        }

        logSafely(
                new ActivityRecord(
                        LocalDateTime.now(),
                        "PRODUCT_ADDED",
                        product.getId(),
                        product.getId(),
                        product.getName(),
                        product.getQuantity(),
                        product.getOriginalPrice(),
                        product.calculateFinalPrice(),
                        0.0,
                        "Listed",
                        "",
                        product.getSellerId(),
                        details
                )
        );
    }

    public static void logOrderReserved(Order order) {
        logOrderEvent("ORDER_RESERVED", order, "Reservation created");
    }

    public static void logOrderCancelled(Order order) {
        logOrderEvent("ORDER_CANCELLED", order, "Reservation cancelled and stock restored");
    }

    public static void logOrderCollected(Order order) {
        logOrderEvent("ORDER_COLLECTED", order, "Order marked as collected");
    }

    private static void logOrderEvent(
            String eventType,
            Order order,
            String details) {

        Product product = order.getProduct();

        logSafely(
                new ActivityRecord(
                        LocalDateTime.now(),
                        eventType,
                        order.getId(),
                        product.getId(),
                        product.getName(),
                        order.getQuantity(),
                        product.getOriginalPrice(),
                        order.getUnitPrice(),
                        order.calculateTotal(),
                        order.getStatus(),
                        order.getCustomer().getId(),
                        product.getSellerId(),
                        details
                )
        );
    }

    private static void logSafely(ActivityRecord record) {

        try {
            append(record);
        } catch (IOException e) {
            System.err.println(
                    "Activity log could not be updated: " + e.getMessage()
            );
        }
    }

    private static void append(ActivityRecord record)
            throws IOException {

        Files.createDirectories(DATA_DIRECTORY);

        try (BufferedWriter writer = Files.newBufferedWriter(
                ACTIVITY_FILE,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            writer.write(toLine(record));
            writer.newLine();
        }
    }

    public static List<ActivityRecord> loadActivities()
            throws IOException {

        List<ActivityRecord> activities = new ArrayList<>();

        if (!Files.exists(ACTIVITY_FILE)) {
            return activities;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                ACTIVITY_FILE,
                StandardCharsets.UTF_8)) {

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] data = line.split("\\t", -1);

                if (data.length != 13) {
                    continue;
                }

                try {
                    activities.add(
                            new ActivityRecord(
                                    LocalDateTime.parse(data[0]),
                                    data[1],
                                    data[2],
                                    data[3],
                                    data[4],
                                    Integer.parseInt(data[5]),
                                    Double.parseDouble(data[6]),
                                    Double.parseDouble(data[7]),
                                    Double.parseDouble(data[8]),
                                    data[9],
                                    data[10],
                                    data[11],
                                    data[12]
                            )
                    );
                } catch (RuntimeException ignored) {
                    // Skip malformed historical rows instead of crashing the app.
                }
            }
        }

        return activities;
    }

    private static String toLine(ActivityRecord record) {

        return String.join(
                "\t",
                sanitize(record.getTimestamp().toString()),
                sanitize(record.getEventType()),
                sanitize(record.getReferenceId()),
                sanitize(record.getProductId()),
                sanitize(record.getProductName()),
                String.valueOf(record.getQuantity()),
                String.valueOf(record.getOriginalPrice()),
                String.valueOf(record.getSaveBitePrice()),
                String.valueOf(record.getTotalAmount()),
                sanitize(record.getStatus()),
                sanitize(record.getCustomerId()),
                sanitize(record.getSellerId()),
                sanitize(record.getDetails())
        );
    }

    private static String sanitize(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace('\t', ' ')
                .replace('\r', ' ')
                .replace('\n', ' ')
                .trim();
    }
}
