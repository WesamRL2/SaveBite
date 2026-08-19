package com.savebite.app;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.savebite.model.Product;
import com.savebite.service.MarketplaceService;
import com.savebite.storage.FileManager;
import com.savebite.ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        MarketplaceService marketplaceService =
                new MarketplaceService();

        loadSavedProducts(
                marketplaceService
        );

        SwingUtilities.invokeLater(
                () -> {

                    MainFrame mainFrame =
                            new MainFrame(
                                    marketplaceService
                            );

                    mainFrame.setVisible(true);
                }
        );
    }

    private static void loadSavedProducts(
            MarketplaceService marketplaceService) {

        try {

            ArrayList<Product> savedProducts =
                    FileManager.loadProducts();

            for (Product product :
                    savedProducts) {

                marketplaceService.addProduct(
                        product
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not load saved products: "
                            + e.getMessage()
            );
        }
    }
}