package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.savebite.model.Customer;
import com.savebite.service.MarketplaceService;

public class MainFrame extends JFrame {

    private final MarketplaceService marketplaceService;
    private final Customer currentCustomer;

    public MainFrame(
            MarketplaceService marketplaceService,
            Customer currentCustomer) {

        this.marketplaceService =
                marketplaceService;

        this.currentCustomer =
                currentCustomer;

        setTitle(
                "SaveBite - Surplus Food Marketplace"
        );

        setSize(
                1000,
                650
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        showDashboard();
    }

    private void showDashboard() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout()
                );

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        mainPanel.add(
                createDashboard(),
                BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        revalidate();
        repaint();
    }

    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel title =
                new JLabel(
                        "SaveBite",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "Reduce Food Waste. Save Money.",
                        SwingConstants.CENTER
                );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16
                )
        );

        panel.add(
                title,
                BorderLayout.CENTER
        );

        panel.add(
                subtitle,
                BorderLayout.SOUTH
        );

        return panel;
    }

    private JPanel createDashboard() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                20,
                                20
                        )
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        40,
                        100,
                        80,
                        100
                )
        );

        JButton dealsButton =
                createMenuButton(
                        "Available Deals"
                );

        JButton addProductButton =
                createMenuButton(
                        "Add Surplus Product"
                );

        JButton ordersButton =
                createMenuButton(
                        "My Orders"
                );

        JButton statisticsButton =
                createMenuButton(
                        "Statistics"
                );

        dealsButton.addActionListener(
                e -> showAvailableDeals()
        );

        addProductButton.addActionListener(
                e -> showAddProduct()
        );

        ordersButton.addActionListener(
                e -> JOptionPane.showMessageDialog(
                        this,
                        "My Orders screen will be added next."
                )
        );

        statisticsButton.addActionListener(
                e -> JOptionPane.showMessageDialog(
                        this,
                        "Statistics screen will be added next."
                )
        );

        panel.add(dealsButton);
        panel.add(addProductButton);
        panel.add(ordersButton);
        panel.add(statisticsButton);

        return panel;
    }

    private JButton createMenuButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        button.setFocusPainted(false);

        return button;
    }

    private void showAvailableDeals() {

        AvailableDealsPanel dealsPanel =
                new AvailableDealsPanel(
                        marketplaceService,
                        currentCustomer,
                        this::showDashboard
                );

        setContentPane(dealsPanel);

        revalidate();
        repaint();
    }

    private void showAddProduct() {

        AddSurplusProductPanel addProductPanel =
                new AddSurplusProductPanel(
                        marketplaceService,
                        this::showDashboard
                );

        setContentPane(addProductPanel);

        revalidate();
        repaint();
    }
}