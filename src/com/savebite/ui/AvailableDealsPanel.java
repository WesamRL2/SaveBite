package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.savebite.model.Customer;
import com.savebite.model.FoodProduct;
import com.savebite.model.Order;
import com.savebite.model.Product;
import com.savebite.service.MarketplaceService;
import com.savebite.storage.FileManager;
import com.savebite.util.ValidationUtil;

public class AvailableDealsPanel extends JPanel {

    private final MarketplaceService marketplaceService;
    private final Customer currentCustomer;
    private final Runnable backAction;

    private final DefaultTableModel tableModel;
    private final JTable dealsTable;

    public AvailableDealsPanel(
            MarketplaceService marketplaceService,
            Customer currentCustomer,
            Runnable backAction) {

        this.marketplaceService =
                marketplaceService;

        this.currentCustomer =
                currentCustomer;

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

        String[] columns = {
                "Product ID",
                "Product",
                "Original Price",
                "SaveBite Price",
                "Quantity",
                "Category",
                "Pickup Deadline"
        };

        tableModel =
                new DefaultTableModel(
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

        dealsTable =
                new JTable(
                        tableModel
                );

        dealsTable.setRowHeight(
                30
        );

        dealsTable
                .getTableHeader()
                .setFont(
                        new Font(
                                "Arial",
                                Font.BOLD,
                                14
                        )
                );

        add(
                new JScrollPane(
                        dealsTable
                ),
                BorderLayout.CENTER
        );

        add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );

        refreshDeals();
    }

    private JPanel createTopPanel() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        JButton backButton =
                new JButton(
                        "Back"
                );

        backButton.addActionListener(
                e -> backAction.run()
        );

        JPanel leftPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.LEFT
                        )
                );

        leftPanel.add(
                backButton
        );

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

    private JPanel createBottomPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );

        JButton reserveButton =
                new JButton(
                        "Reserve Selected Deal"
                );

        reserveButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        reserveButton.addActionListener(
                e -> reserveSelectedDeal()
        );

        panel.add(
                reserveButton
        );

        return panel;
    }

    public void refreshDeals() {

        tableModel.setRowCount(
                0
        );

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

            if (product
                    instanceof FoodProduct foodProduct) {

                category =
                        foodProduct.getCategory();

                deadline =
                        foodProduct
                                .getPickupDeadline()
                                .format(
                                        formatter
                                );
            }

            tableModel.addRow(
                    new Object[] {
                            product.getId(),
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

    private void reserveSelectedDeal() {

        int selectedRow =
                dealsTable
                        .getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a deal first.",
                    "No Deal Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String productId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();

        String productName =
                tableModel
                        .getValueAt(
                                selectedRow,
                                1
                        )
                        .toString();

        String quantityInput =
                JOptionPane.showInputDialog(
                        this,
                        "Enter quantity to reserve:",
                        "Reserve "
                                + productName,
                        JOptionPane.QUESTION_MESSAGE
                );

        if (quantityInput == null) {
            return;
        }

        try {

            int quantity =
                    ValidationUtil
                            .parsePositiveInt(
                                    quantityInput,
                                    "Quantity"
                            );

            Order order =
                    marketplaceService
                            .reserveProduct(
                                    currentCustomer,
                                    productId,
                                    quantity
                            );

            FileManager.saveProducts(
                    marketplaceService
                            .getProducts()
            );

            FileManager.saveOrders(
                    marketplaceService
                            .getOrders()
            );

            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            """
                            Reservation successful!

                            Order ID: %s
                            Product: %s
                            Quantity: %d
                            Unit Price: RM %.2f
                            Total Price: RM %.2f
                            Status: %s
                            """,
                            order.getId(),
                            order
                                    .getProduct()
                                    .getName(),
                            order.getQuantity(),
                            order.getUnitPrice(),
                            order.calculateTotal(),
                            order.getStatus()
                    ),
                    "SaveBite Reservation",
                    JOptionPane.INFORMATION_MESSAGE
            );

            refreshDeals();

        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Reservation Failed",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Reservation was created, but data could not be saved.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}