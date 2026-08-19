package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.savebite.model.FoodProduct;
import com.savebite.model.Product;
import com.savebite.service.MarketplaceService;

public class AvailableDealsPanel extends JPanel {

    private final MarketplaceService marketplaceService;
    private final DefaultTableModel tableModel;
    private final Runnable backAction;

    public AvailableDealsPanel(
            MarketplaceService marketplaceService,
            Runnable backAction) {

        this.marketplaceService = marketplaceService;
        this.backAction = backAction;

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

        String[] columns = {
                "Product",
                "Original Price",
                "SaveBite Price",
                "Quantity",
                "Category",
                "Pickup Deadline"
        };

        tableModel = new DefaultTableModel(
                columns,
                0
        ) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        JTable table =
                new JTable(tableModel);

        table.setRowHeight(30);

        table.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        refreshDeals();
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
                        "Available Deals",
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

    public void refreshDeals() {

        tableModel.setRowCount(0);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );

        for (Product product :
                marketplaceService.getProducts()) {

            if (!product.isAvailable()) {
                continue;
            }

            String category = "-";
            String deadline = "-";

            if (product instanceof FoodProduct foodProduct) {

                category =
                        foodProduct.getCategory();

                deadline =
                        foodProduct
                                .getPickupDeadline()
                                .format(formatter);
            }

            tableModel.addRow(
                    new Object[] {
                            product.getName(),

                            String.format(
                                    "RM %.2f",
                                    product.getOriginalPrice()
                            ),

                            String.format(
                                    "RM %.2f",
                                    product.calculateFinalPrice()
                            ),

                            product.getQuantity(),

                            category,

                            deadline
                    }
            );
        }
    }
}