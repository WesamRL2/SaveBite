package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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

        this.marketplaceService = marketplaceService;
        this.currentCustomer = currentCustomer;
        this.backAction = backAction;

        setLayout(new BorderLayout(10, 10));

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
                "Order ID",
                "Product",
                "Quantity",
                "Unit Price",
                "Total",
                "Order Time",
                "Status"
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

        ordersTable = new JTable(tableModel);

        ordersTable.setRowHeight(30);

        ordersTable.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        add(
                new JScrollPane(ordersTable),
                BorderLayout.CENTER
        );

        add(
                createBottomPanel(),
                BorderLayout.SOUTH
        );

        refreshOrders();
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
                        "My Orders - "
                                + currentCustomer.getName(),
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

        JButton collectedButton =
                new JButton(
                        "Mark as Collected"
                );

        JButton cancelButton =
                new JButton(
                        "Cancel Order"
                );

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

                            order
                                    .getProduct()
                                    .getName(),

                            order.getQuantity(),

                            String.format(
                                    "RM %.2f",
                                    order.getUnitPrice()
                            ),

                            String.format(
                                    "RM %.2f",
                                    order.calculateTotal()
                            ),

                            order
                                    .getOrderTime()
                                    .format(formatter),

                            order.getStatus()
                    }
            );
        }
    }

    private Order getSelectedOrder() {

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

        String orderId =
                tableModel
                        .getValueAt(
                                selectedRow,
                                0
                        )
                        .toString();

        for (Order order :
                marketplaceService.getOrders()) {

            if (order
                    .getId()
                    .equalsIgnoreCase(orderId)) {

                return order;
            }
        }

        return null;
    }

    private void markSelectedOrderAsCollected() {

        Order order =
                getSelectedOrder();

        if (order == null) {
            return;
        }

        if ("Cancelled".equalsIgnoreCase(
                order.getStatus())) {

            JOptionPane.showMessageDialog(
                    this,
                    "A cancelled order cannot be collected.",
                    "Invalid Action",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        order.markAsCollected();

        refreshOrders();

        JOptionPane.showMessageDialog(
                this,
                "Order "
                        + order.getId()
                        + " marked as collected.",
                "SaveBite",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void cancelSelectedOrder() {

        Order order =
                getSelectedOrder();

        if (order == null) {
            return;
        }

        if ("Collected".equalsIgnoreCase(
                order.getStatus())) {

            JOptionPane.showMessageDialog(
                    this,
                    "A collected order cannot be cancelled.",
                    "Invalid Action",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if ("Cancelled".equalsIgnoreCase(
                order.getStatus())) {

            JOptionPane.showMessageDialog(
                    this,
                    "This order is already cancelled.",
                    "Invalid Action",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        order.cancel();

        refreshOrders();

        JOptionPane.showMessageDialog(
                this,
                "Order "
                        + order.getId()
                        + " cancelled.",
                "SaveBite",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}