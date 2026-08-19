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

import com.savebite.model.FoodProduct;
import com.savebite.model.Product;

public class FileManager {

    private static final Path DATA_DIRECTORY = Paths.get("data");
    private static final Path PRODUCTS_FILE =
            DATA_DIRECTORY.resolve("products.csv");

    private FileManager() {
        // Utility class - prevent object creation
    }

    public static void saveProducts(List<Product> products)
            throws IOException {

        Files.createDirectories(DATA_DIRECTORY);

        try (BufferedWriter writer =
                     Files.newBufferedWriter(PRODUCTS_FILE)) {

            for (Product product : products) {

                if (product instanceof FoodProduct foodProduct) {

                    writer.write(
                            foodProduct.getId() + "," +
                            foodProduct.getName() + "," +
                            foodProduct.getOriginalPrice() + "," +
                            foodProduct.getQuantity() + "," +
                            foodProduct.getSellerId() + "," +
                            foodProduct.getCategory() + "," +
                            foodProduct.getDiscountPercentage() + "," +
                            foodProduct.getPickupDeadline()
                    );

                    writer.newLine();
                }
            }
        }
    }

    public static ArrayList<Product> loadProducts()
            throws IOException {

        ArrayList<Product> products = new ArrayList<>();

        if (!Files.exists(PRODUCTS_FILE)) {
            return products;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(PRODUCTS_FILE)) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length != 8) {
                    continue;
                }

                Product product = new FoodProduct(
                        data[0],
                        data[1],
                        Double.parseDouble(data[2]),
                        Integer.parseInt(data[3]),
                        data[4],
                        data[5],
                        Double.parseDouble(data[6]),
                        LocalDateTime.parse(data[7])
                );

                products.add(product);
            }
        }

        return products;
    }
}