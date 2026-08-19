package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.savebite.model.FoodProduct;
import com.savebite.model.Product;
import com.savebite.service.MarketplaceService;
import com.savebite.storage.FileManager;
import com.savebite.util.ValidationUtil;

public class AddSurplusProductPanel
        extends JPanel {

    private final MarketplaceService marketplaceService;
    private final Runnable backAction;

    private JTextField sellerIdField;
    private JTextField nameField;
    private JTextField originalPriceField;
    private JTextField quantityField;
    private JTextField categoryField;
    private JTextField discountField;
    private JTextField pickupHoursField;

    public AddSurplusProductPanel(
            MarketplaceService marketplaceService,
            Runnable backAction) {

        this.marketplaceService =
                marketplaceService;

        this.backAction =
                backAction;

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        add(
                createTopPanel(),
                BorderLayout.NORTH
        );

        add(
                createFormPanel(),
                BorderLayout.CENTER
        );
    }

    private JPanel createTopPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        JButton backButton =
                new JButton("Back");

        backButton.addActionListener(
                e -> backAction.run()
        );

        JPanel leftPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        leftPanel.add(backButton);

        JLabel title =
                new JLabel(
                        "Add Surplus Product",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        panel.add(
                leftPanel,
                BorderLayout.WEST
        );

        panel.add(
                title,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createFormPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8,
                        8,
                        8,
                        8
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        sellerIdField =
                new JTextField(
                        "S001",
                        20
                );

        nameField =
                new JTextField(
                        20
                );

        originalPriceField =
                new JTextField(
                        20
                );

        quantityField =
                new JTextField(
                        20
                );

        categoryField =
                new JTextField(
                        20
                );

        discountField =
                new JTextField(
                        20
                );

        pickupHoursField =
                new JTextField(
                        20
                );

        int row = 0;

        addFormRow(
                panel,
                gbc,
                row++,
                "Seller ID:",
                sellerIdField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Product Name:",
                nameField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Original Price (RM):",
                originalPriceField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Quantity:",
                quantityField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Category:",
                categoryField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Discount (%):",
                discountField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Pickup Within (Hours):",
                pickupHoursField
        );

        JButton addButton =
                new JButton(
                        "Add Product"
                );

        addButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        addButton.addActionListener(
                e -> addProduct()
        );

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        panel.add(
                addButton,
                gbc
        );

        return panel;
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JTextField textField) {

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = row;

        panel.add(
                new JLabel(labelText),
                gbc
        );

        gbc.gridx = 1;

        panel.add(
                textField,
                gbc
        );
    }

    private void addProduct() {

        try {

            String sellerId =
                    ValidationUtil.requireText(
                            sellerIdField.getText(),
                            "Seller ID"
                    );

            String name =
                    ValidationUtil.requireText(
                            nameField.getText(),
                            "Product name"
                    );

            double originalPrice =
                    ValidationUtil.parsePositiveDouble(
                            originalPriceField.getText(),
                            "Original price"
                    );

            int quantity =
                    ValidationUtil.parsePositiveInt(
                            quantityField.getText(),
                            "Quantity"
                    );

            String category =
                    ValidationUtil.requireText(
                            categoryField.getText(),
                            "Category"
                    );

            double discount =
                    ValidationUtil.parseDiscount(
                            discountField.getText()
                    );

            int pickupHours =
                    ValidationUtil.parsePositiveInt(
                            pickupHoursField.getText(),
                            "Pickup hours"
                    );

            String productId =
                    marketplaceService
                            .generateProductId();

            Product product =
                    new FoodProduct(
                            productId,
                            name,
                            originalPrice,
                            quantity,
                            sellerId,
                            category,
                            discount,
                            LocalDateTime
                                    .now()
                                    .plusHours(
                                            pickupHours
                                    )
                    );

            boolean added =
                    marketplaceService
                            .addProduct(
                                    product
                            );

            if (!added) {

                throw new IllegalStateException(
                        "Product could not be added."
                );
            }

            try {

                FileManager.saveProducts(
                        marketplaceService
                                .getProducts()
                );

            } catch (IOException e) {

                marketplaceService
                        .removeProductById(
                                productId
                        );

                throw new IllegalStateException(
                        "Product could not be saved.",
                        e
                );
            }

            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            """
                            Product added successfully!

                            Product ID: %s
                            Product: %s
                            Original Price: RM %.2f
                            SaveBite Price: RM %.2f
                            Quantity: %d
                            """,
                            product.getId(),
                            product.getName(),
                            product.getOriginalPrice(),
                            product.calculateFinalPrice(),
                            product.getQuantity()
                    ),
                    "SaveBite",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearForm();

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearForm() {

        nameField.setText("");
        originalPriceField.setText("");
        quantityField.setText("");
        categoryField.setText("");
        discountField.setText("");
        pickupHoursField.setText("");

        nameField.requestFocus();
    }
}