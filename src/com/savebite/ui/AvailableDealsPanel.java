package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import com.savebite.storage.ActivityLogger;
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

        this.marketplaceService = marketplaceService;
        this.currentCustomer = currentCustomer;
        this.backAction = backAction;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 30, 35
                )
        );

        add(createHeader(), BorderLayout.NORTH);

        String[] columns = {
                "Product ID",
                "Product",
                "Original Price",
                "SaveBite Price",
                "Quantity",
                "Category",
                "Pickup Deadline"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        dealsTable = new JTable(tableModel);
        UITheme.styleTable(dealsTable);

        JScrollPane scrollPane = new JScrollPane(dealsTable);
        scrollPane.setBorder(
                BorderFactory.createLineBorder(UITheme.BORDER)
        );

        add(scrollPane, BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        refreshDeals();
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JButton backButton = UITheme.createBackButton();
        backButton.addActionListener(e -> backAction.run());

        JLabel title = new JLabel(
                "Available Deals",
                SwingConstants.CENTER
        );
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT);

        JLabel subtitle = new JLabel(
                "Choose surplus food and reserve it at a discounted price.",
                SwingConstants.CENTER
        );
        subtitle.setFont(UITheme.SUBTITLE_FONT);
        subtitle.setForeground(UITheme.MUTED_TEXT);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.CENTER);
        titlePanel.add(subtitle, BorderLayout.SOUTH);

        panel.add(backButton, BorderLayout.WEST);
        panel.add(titlePanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBottomPanel() {

        JPanel panel = new JPanel(
                new FlowLayout(FlowLayout.CENTER)
        );
        panel.setOpaque(false);

        JButton reserveButton = UITheme.createPrimaryButton(
                "Reserve Selected Deal"
        );

        reserveButton.addActionListener(
                e -> reserveSelectedDeal()
        );

        panel.add(reserveButton);
        return panel;
    }

    public void refreshDeals() {

        tableModel.setRowCount(0);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );

        for (Product product : marketplaceService.getProducts()) {

            if (!product.isAvailable()) {
                continue;
            }

            String category = "-";
            String deadline = "-";

            if (product instanceof FoodProduct foodProduct) {
                category = foodProduct.getCategory();
                deadline = foodProduct
                        .getPickupDeadline()
                        .format(formatter);
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

        int selectedRow = dealsTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a deal first.",
                    "No Deal Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String productId = tableModel
                .getValueAt(selectedRow, 0)
                .toString();

        String productName = tableModel
                .getValueAt(selectedRow, 1)
                .toString();

        String quantityInput = JOptionPane.showInputDialog(
                this,
                "Enter quantity to reserve:",
                "Reserve " + productName,
                JOptionPane.QUESTION_MESSAGE
        );

        if (quantityInput == null) {
            return;
        }

        try {

            int quantity = ValidationUtil.parsePositiveInt(
                    quantityInput,
                    "Quantity"
            );

            Order order = marketplaceService.reserveProduct(
                    currentCustomer,
                    productId,
                    quantity
            );

            FileManager.saveProducts(
                    marketplaceService.getProducts()
            );

            FileManager.saveOrders(
                    marketplaceService.getOrders()
            );

            ActivityLogger.logOrderReserved(order);

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
                            order.getProduct().getName(),
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
