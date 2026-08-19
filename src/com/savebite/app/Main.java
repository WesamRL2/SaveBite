package com.savebite.app;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.savebite.model.Customer;
import com.savebite.model.Order;
import com.savebite.model.Product;
import com.savebite.service.MarketplaceService;
import com.savebite.storage.FileManager;
import com.savebite.ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        MarketplaceService marketplaceService =
                new MarketplaceService();

        Customer currentCustomer =
                new Customer(
                        "C001",
                        "Ali",
                        "ali@email.com"
                );

        loadSavedData(
                marketplaceService,
                currentCustomer
        );

        SwingUtilities.invokeLater(
                () -> {

                    MainFrame mainFrame =
                            new MainFrame(
                                    marketplaceService,
                                    currentCustomer
                            );

                    mainFrame.setVisible(true);
                }
        );
    }

    private static void loadSavedData(
            MarketplaceService marketplaceService,
            Customer currentCustomer) {

        try {

            ArrayList<Product> products =
                    FileManager.loadProducts();

            for (Product product : products) {

                marketplaceService.addProduct(
                        product
                );
            }

            ArrayList<Order> orders =
                    FileManager.loadOrders(
                            marketplaceService
                                    .getProducts(),
                            currentCustomer
                    );

            for (Order order : orders) {

                marketplaceService.addOrder(
                        order
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not load saved data: "
                            + e.getMessage()
            );
        }
    }
}