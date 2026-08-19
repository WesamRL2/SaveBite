package com.savebite.storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.savebite.model.Customer;
import com.savebite.model.FoodProduct;
import com.savebite.model.Order;
import com.savebite.model.Product;

public final class FileManager {

    private static final Path DATA_DIRECTORY =
            Paths.get("data");

    private static final Path PRODUCTS_FILE =
            DATA_DIRECTORY.resolve(
                    "products.csv"
            );

    private static final Path ORDERS_FILE =
            DATA_DIRECTORY.resolve(
                    "orders.csv"
            );

    private FileManager() {
        // Utility class
    }

    public static void saveProducts(
            List<Product> products)
            throws IOException {

        Files.createDirectories(
                DATA_DIRECTORY
        );

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             PRODUCTS_FILE
                     )) {

            for (Product product : products) {

                if (product
                        instanceof FoodProduct foodProduct) {

                    writer.write(
                            foodProduct.getId()
                                    + ","
                                    + foodProduct.getName()
                                    + ","
                                    + foodProduct.getOriginalPrice()
                                    + ","
                                    + foodProduct.getQuantity()
                                    + ","
                                    + foodProduct.getSellerId()
                                    + ","
                                    + foodProduct.getCategory()
                                    + ","
                                    + foodProduct.getDiscountPercentage()
                                    + ","
                                    + foodProduct.getPickupDeadline()
                    );

                    writer.newLine();
                }
            }
        }
    }

    public static ArrayList<Product> loadProducts()
            throws IOException {

        ArrayList<Product> products =
                new ArrayList<>();

        if (!Files.exists(
                PRODUCTS_FILE)) {

            return products;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             PRODUCTS_FILE
                     )) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length != 8) {
                    continue;
                }

                Product product =
                        new FoodProduct(
                                data[0],
                                data[1],
                                Double.parseDouble(
                                        data[2]
                                ),
                                Integer.parseInt(
                                        data[3]
                                ),
                                data[4],
                                data[5],
                                Double.parseDouble(
                                        data[6]
                                ),
                                LocalDateTime.parse(
                                        data[7]
                                )
                        );

                products.add(product);
            }
        }

        return products;
    }

    public static void saveOrders(
            List<Order> orders)
            throws IOException {

        Files.createDirectories(
                DATA_DIRECTORY
        );

        try (BufferedWriter writer =
                     Files.newBufferedWriter(
                             ORDERS_FILE
                     )) {

            for (Order order : orders) {

                writer.write(
                        order.getId()
                                + ","
                                + order.getCustomer().getId()
                                + ","
                                + order.getProduct().getId()
                                + ","
                                + order.getQuantity()
                                + ","
                                + order.getUnitPrice()
                                + ","
                                + order.getOrderTime()
                                + ","
                                + order.getStatus()
                );

                writer.newLine();
            }
        }
    }

    public static ArrayList<Order> loadOrders(
            List<Product> products,
            Customer currentCustomer)
            throws IOException {

        ArrayList<Order> orders =
                new ArrayList<>();

        if (!Files.exists(
                ORDERS_FILE)) {

            return orders;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             ORDERS_FILE
                     )) {

            String line;

            while ((line =
                    reader.readLine()) != null) {

                String[] data =
                        line.split(",");

                if (data.length != 7) {
                    continue;
                }

                String orderId =
                        data[0];

                String customerId =
                        data[1];

                String productId =
                        data[2];

                int quantity =
                        Integer.parseInt(
                                data[3]
                        );

                double unitPrice =
                        Double.parseDouble(
                                data[4]
                        );

                LocalDateTime orderTime =
                        LocalDateTime.parse(
                                data[5]
                        );

                String status =
                        data[6];

                if (!currentCustomer
                        .getId()
                        .equalsIgnoreCase(
                                customerId
                        )) {

                    continue;
                }

                Product matchedProduct =
                        findProduct(
                                products,
                                productId
                        );

                if (matchedProduct == null) {
                    continue;
                }

                Order order =
                        new Order(
                                orderId,
                                currentCustomer,
                                matchedProduct,
                                quantity,
                                unitPrice,
                                orderTime,
                                status
                        );

                orders.add(order);
            }
        }

        return orders;
    }

    private static Product findProduct(
            List<Product> products,
            String productId) {

        for (Product product : products) {

            if (product
                    .getId()
                    .equalsIgnoreCase(
                            productId
                    )) {

                return product;
            }
        }

        return null;
    }
}