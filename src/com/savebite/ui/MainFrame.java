package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
                1100,
                700
        );

        setMinimumSize(
                new Dimension(
                        900,
                        600
                )
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

        mainPanel.setBackground(
                UITheme.BACKGROUND
        );

        mainPanel.add(
                createHeader(),
                BorderLayout.NORTH
        );

        mainPanel.add(
                createDashboard(),
                BorderLayout.CENTER
        );

        mainPanel.add(
                createFooter(),
                BorderLayout.SOUTH
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

        panel.setBackground(
                UITheme.CARD
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                0,
                                0,
                                1,
                                0,
                                UITheme.BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                25,
                                50,
                                25,
                                50
                        )
                )
        );

        JPanel brandPanel =
                new JPanel(
                        new BorderLayout()
                );

        brandPanel.setOpaque(false);

        JLabel title =
                new JLabel(
                        "SaveBite"
                );

        title.setFont(
                UITheme.TITLE_FONT
        );

        title.setForeground(
                UITheme.PRIMARY
        );

        JLabel subtitle =
                new JLabel(
                        "Save food. Save money. Reduce waste."
                );

        subtitle.setFont(
                UITheme.SUBTITLE_FONT
        );

        subtitle.setForeground(
                UITheme.MUTED_TEXT
        );

        brandPanel.add(
                title,
                BorderLayout.NORTH
        );

        brandPanel.add(
                subtitle,
                BorderLayout.SOUTH
        );

        JLabel userLabel =
                new JLabel(
                        "Welcome, "
                                + currentCustomer.getName(),
                        SwingConstants.RIGHT
                );

        userLabel.setFont(
                UITheme.BUTTON_FONT
        );

        userLabel.setForeground(
                UITheme.TEXT
        );

        panel.add(
                brandPanel,
                BorderLayout.WEST
        );

        panel.add(
                userLabel,
                BorderLayout.EAST
        );

        return panel;
    }

    private JPanel createDashboard() {

        JPanel wrapper =
                new JPanel(
                        new BorderLayout()
                );

        wrapper.setBackground(
                UITheme.BACKGROUND
        );

        wrapper.setBorder(
                BorderFactory.createEmptyBorder(
                        45,
                        80,
                        45,
                        80
                )
        );

        JPanel headingPanel =
                new JPanel(
                        new BorderLayout()
                );

        headingPanel.setOpaque(false);

        JLabel heading =
                new JLabel(
                        "What would you like to do?"
                );

        heading.setFont(
                new java.awt.Font(
                        "Segoe UI",
                        java.awt.Font.BOLD,
                        24
                )
        );

        heading.setForeground(
                UITheme.TEXT
        );

        JLabel description =
                new JLabel(
                        "Manage surplus food and help reduce unnecessary waste."
                );

        description.setFont(
                UITheme.SUBTITLE_FONT
        );

        description.setForeground(
                UITheme.MUTED_TEXT
        );

        headingPanel.add(
                heading,
                BorderLayout.NORTH
        );

        headingPanel.add(
                description,
                BorderLayout.SOUTH
        );

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                25,
                                25
                        )
                );

        buttonPanel.setOpaque(false);

        buttonPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        35,
                        0,
                        20,
                        0
                )
        );

        JButton dealsButton =
                createDashboardButton(
                        "Available Deals",
                        "Browse discounted surplus food"
                );

        JButton addProductButton =
                createDashboardButton(
                        "Add Surplus Product",
                        "List food before it goes to waste"
                );

        JButton ordersButton =
                createDashboardButton(
                        "My Orders",
                        "View and manage reservations"
                );

        JButton statisticsButton =
                createDashboardButton(
                        "Statistics",
                        "View sustainability impact"
                );

        dealsButton.addActionListener(
                e -> showAvailableDeals()
        );

        addProductButton.addActionListener(
                e -> showAddProduct()
        );

        ordersButton.addActionListener(
                e -> showMyOrders()
        );

        statisticsButton.addActionListener(
                e -> showStatistics()
        );

        buttonPanel.add(dealsButton);
        buttonPanel.add(addProductButton);
        buttonPanel.add(ordersButton);
        buttonPanel.add(statisticsButton);

        wrapper.add(
                headingPanel,
                BorderLayout.NORTH
        );

        wrapper.add(
                buttonPanel,
                BorderLayout.CENTER
        );

        return wrapper;
    }

    private JButton createDashboardButton(
            String title,
            String description) {

        JButton button =
                UITheme.createPrimaryButton(
                        "<html><center>"
                                + title
                                + "<br>"
                                + "<span style='font-size:10px;'>"
                                + description
                                + "</span>"
                                + "</center></html>"
                );

        return button;
    }

    private JPanel createFooter() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.setBackground(
                UITheme.CARD
        );

        panel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createMatteBorder(
                                1,
                                0,
                                0,
                                0,
                                UITheme.BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                12,
                                20,
                                12,
                                20
                        )
                )
        );

        JLabel label =
                new JLabel(
                        "Supporting SDG 12 - Responsible Consumption and Production",
                        SwingConstants.CENTER
                );

        label.setFont(
                UITheme.NORMAL_FONT
        );

        label.setForeground(
                UITheme.MUTED_TEXT
        );

        panel.add(label);

        return panel;
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

    private void showMyOrders() {

        MyOrdersPanel myOrdersPanel =
                new MyOrdersPanel(
                        marketplaceService,
                        currentCustomer,
                        this::showDashboard
                );

        setContentPane(myOrdersPanel);

        revalidate();
        repaint();
    }

    private void showStatistics() {

        StatisticsPanel statisticsPanel =
                new StatisticsPanel(
                        marketplaceService,
                        this::showDashboard
                );

        setContentPane(statisticsPanel);

        revalidate();
        repaint();
    }
}