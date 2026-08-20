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
import com.savebite.model.Order;
import com.savebite.service.MarketplaceService;
import com.savebite.storage.FileManager;

public class MyOrdersPanel extends JPanel {

    private final MarketplaceService marketplaceService;
    private final Customer currentCustomer;
    private final Runnable backAction;

    private final DefaultTableModel tableModel;
    private final JTable ordersTable;

    public MyOrdersPanel(
            MarketplaceService marketplaceService,
            Customer currentCustomer,
            Runnable backAction) {

        this.marketplaceService =
                marketplaceService;

        this.currentCustomer =
                currentCustomer;

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

        String[] columns = {
                "Order ID",
                "Product",
                "Quantity",
                "Unit Price",
                "Total",
                "Order Time",
                "Status"
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

        ordersTable =
                new JTable(tableModel);

        UITheme.styleTable(
                ordersTable
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        ordersTable
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        UITheme.BORDER
                )
        );

        add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );

        refreshOrders();
    }

    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setOpaque(false);

        JButton backButton =
                UITheme.createBackButton();

        backButton.addActionListener(
                e -> backAction.run()
        );

        JLabel title =
                new JLabel(
                        "My Orders",
                        SwingConstants.CENTER
                );

        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT);

        JLabel subtitle =
                new JLabel(
                        "Reservations for "
                                + currentCustomer.getName(),
                        SwingConstants.CENTER
                );

        subtitle.setFont(UITheme.SUBTITLE_FONT);
        subtitle.setForeground(UITheme.MUTED_TEXT);

        JPanel titlePanel =
                new JPanel(
                        new BorderLayout()
                );

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

    private JPanel createBottomPanel() {

        JPanel panel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                15,
                                5
                        )
                );

        panel.setOpaque(false);

        JButton collectedButton =
                UITheme.createPrimaryButton(
                        "Mark as Collected"
                );

        JButton cancelButton =
                new JButton(
                        "Cancel Order"
                );

        cancelButton.setFont(
                UITheme.BUTTON_FONT
        );

        cancelButton.setFocusPainted(false);

        collectedButton.addActionListener(
                e -> markSelectedOrderAsCollected()
        );

        cancelButton.addActionListener(
                e -> cancelSelectedOrder()
        );

        panel.add(collectedButton);
        panel.add(cancelButton);

        return panel;
    }

    public void refreshOrders() {

        tableModel.setRowCount(0);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy HH:mm"
                );

        for (Order order :
                marketplaceService.getOrders()) {

            if (!order
                    .getCustomer()
                    .getId()
                    .equalsIgnoreCase(
                            currentCustomer.getId()
                    )) {

                continue;
            }

            tableModel.addRow(
                    new Object[] {
                            order.getId(),
                            order.getProduct().getName(),
                            order.getQuantity(),

                            String.format(
                                    "RM %.2f",
                                    order.getUnitPrice()
                            ),

                            String.format(
                                    "RM %.2f",
                                    order.calculateTotal()
                            ),

                            order.getOrderTime()
                                    .format(formatter),

                            order.getStatus()
                    }
            );
        }
    }

    private String getSelectedOrderId() {

        int selectedRow =
                ordersTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an order first.",
                    "No Order Selected",
                    JOptionPane.WARNING_MESSAGE
            );

            return null;
        }

        return tableModel
                .getValueAt(
                        selectedRow,
                        0
                )
                .toString();
    }

    private void markSelectedOrderAsCollected() {

        String orderId =
                getSelectedOrderId();

        if (orderId == null) {
            return;
        }

        boolean success =
                marketplaceService.collectOrder(
                        orderId
                );

        if (!success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only reserved orders can be collected.",
                    "Invalid Action",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            FileManager.saveOrders(
                    marketplaceService.getOrders()
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Order status changed but could not be saved.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        refreshOrders();

        JOptionPane.showMessageDialog(
                this,
                "Order "
                        + orderId
                        + " marked as collected.",
                "SaveBite",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void cancelSelectedOrder() {

        String orderId =
                getSelectedOrderId();

        if (orderId == null) {
            return;
        }

        boolean success =
                marketplaceService.cancelOrder(
                        orderId
                );

        if (!success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Only reserved orders can be cancelled.",
                    "Invalid Action",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        try {

            FileManager.saveProducts(
                    marketplaceService.getProducts()
            );

            FileManager.saveOrders(
                    marketplaceService.getOrders()
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Order changed but data could not be saved.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        refreshOrders();

        JOptionPane.showMessageDialog(
                this,
                "Order "
                        + orderId
                        + " cancelled. Quantity returned to stock.",
                "SaveBite",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}