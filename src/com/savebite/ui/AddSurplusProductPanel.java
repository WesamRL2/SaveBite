package com.savebite.ui;

import java.awt.BorderLayout;
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

public class AddSurplusProductPanel extends JPanel {

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

        setLayout(new BorderLayout(20, 20));

        setBackground(UITheme.BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 30, 35
                )
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createFormCard(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {

        JPanel panel =
                new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JButton backButton =
                UITheme.createBackButton();

        backButton.addActionListener(
                e -> backAction.run()
        );

        JLabel title =
                new JLabel(
                        "Add Surplus Product",
                        SwingConstants.CENTER
                );

        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT);

        JLabel subtitle =
                new JLabel(
                        "List unsold food before the end of the day.",
                        SwingConstants.CENTER
                );

        subtitle.setFont(UITheme.SUBTITLE_FONT);
        subtitle.setForeground(UITheme.MUTED_TEXT);

        JPanel titlePanel =
                new JPanel(new BorderLayout());

        titlePanel.setOpaque(false);

        titlePanel.add(
                title,
                BorderLayout.CENTER
        );

        titlePanel.add(
                subtitle,
                BorderLayout.SOUTH
        );

        panel.add(
                backButton,
                BorderLayout.WEST
        );

        panel.add(
                titlePanel,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createFormCard() {

        JPanel wrapper =
                new JPanel(
                        new GridBagLayout()
                );

        wrapper.setOpaque(false);

        JPanel card =
                createFormPanel();

        card.setBackground(UITheme.CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                UITheme.BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                30, 45, 30, 45
                        )
                )
        );

        wrapper.add(card);

        return wrapper;
    }

    private JPanel createFormPanel() {

        JPanel panel =
                new JPanel(
                        new GridBagLayout()
                );

        panel.setOpaque(false);

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        8, 10, 8, 10
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        sellerIdField =
                createField("S001");

        nameField =
                createField("");

        originalPriceField =
                createField("");

        quantityField =
                createField("");

        categoryField =
                createField("");

        discountField =
                createField("");

        pickupHoursField =
                createField("");

        int row = 0;

        addFormRow(
                panel,
                gbc,
                row++,
                "Seller ID",
                sellerIdField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Product Name",
                nameField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Original Price (RM)",
                originalPriceField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Quantity",
                quantityField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Category",
                categoryField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Discount (%)",
                discountField
        );

        addFormRow(
                panel,
                gbc,
                row++,
                "Pickup Within (Hours)",
                pickupHoursField
        );

        JButton addButton =
                UITheme.createPrimaryButton(
                        "Add Product"
                );

        addButton.addActionListener(
                e -> addProduct()
        );

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        gbc.insets =
                new Insets(
                        20, 10, 5, 10
                );

        panel.add(addButton, gbc);

        return panel;
    }

    private JTextField createField(
            String text) {

        JTextField field =
                new JTextField(
                        text,
                        22
                );

        field.setFont(UITheme.NORMAL_FONT);

        return field;
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints gbc,
            int row,
            String labelText,
            JTextField field) {

        JLabel label =
                new JLabel(labelText);

        label.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        label.setForeground(UITheme.TEXT);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = row;

        panel.add(label, gbc);

        gbc.gridx = 1;

        panel.add(field, gbc);
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
                    marketplaceService.generateProductId();

            Product product =
                    new FoodProduct(
                            productId,
                            name,
                            originalPrice,
                            quantity,
                            sellerId,
                            category,
                            discount,
                            LocalDateTime.now()
                                    .plusHours(
                                            pickupHours
                                    )
                    );

            boolean added =
                    marketplaceService.addProduct(
                            product
                    );

            if (!added) {

                throw new IllegalStateException(
                        "Product could not be added."
                );
            }

            try {

                FileManager.saveProducts(
                        marketplaceService.getProducts()
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