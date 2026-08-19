package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.savebite.service.MarketplaceService;

public class StatisticsPanel extends JPanel {

    private final MarketplaceService marketplaceService;
    private final Runnable backAction;

    public StatisticsPanel(
            MarketplaceService marketplaceService,
            Runnable backAction) {

        this.marketplaceService =
                marketplaceService;

        this.backAction =
                backAction;

        setLayout(
                new BorderLayout(
                        20,
                        20
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
                createHeader(),
                BorderLayout.NORTH
        );

        add(
                createStatisticsGrid(),
                BorderLayout.CENTER
        );

        add(
                createFooter(),
                BorderLayout.SOUTH
        );
    }

    private JPanel createHeader() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        JButton backButton =
                new JButton("Back");

        backButton.addActionListener(
                e -> backAction.run()
        );

        JLabel title =
                new JLabel(
                        "SaveBite Sustainability Statistics",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        panel.add(
                backButton,
                BorderLayout.WEST
        );

        panel.add(
                title,
                BorderLayout.CENTER
        );

        return panel;
    }

    private JPanel createStatisticsGrid() {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                4,
                                20,
                                20
                        )
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        30,
                        40,
                        30,
                        40
                )
        );

        panel.add(
                createStatisticCard(
                        "Product Listings",
                        String.valueOf(
                                marketplaceService
                                        .getTotalProducts()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Available Listings",
                        String.valueOf(
                                marketplaceService
                                        .getAvailableListingCount()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Available Items",
                        String.valueOf(
                                marketplaceService
                                        .getAvailableItemCount()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Total Orders",
                        String.valueOf(
                                marketplaceService
                                        .getTotalOrders()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Items Saved From Waste",
                        String.valueOf(
                                marketplaceService
                                        .getReservedItemCount()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Revenue Recovered",
                        String.format(
                                "RM %.2f",
                                marketplaceService
                                        .getRecoveredRevenue()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Customer Savings",
                        String.format(
                                "RM %.2f",
                                marketplaceService
                                        .getCustomerSavings()
                        )
                )
        );

        panel.add(
                createStatisticCard(
                        "Surplus Rescue Rate",
                        String.format(
                                "%.1f%%",
                                marketplaceService
                                        .getSurplusRescueRate()
                        )
                )
        );

        return panel;
    }

    private JPanel createStatisticCard(
            String title,
            String value) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                5,
                                5
                        )
                );

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEtchedBorder(),

                        BorderFactory.createEmptyBorder(
                                20,
                                15,
                                20,
                                15
                        )
                )
        );

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JLabel valueLabel =
                new JLabel(
                        value,
                        SwingConstants.CENTER
                );

        valueLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        card.add(
                titleLabel,
                BorderLayout.NORTH
        );

        card.add(
                valueLabel,
                BorderLayout.CENTER
        );

        return card;
    }

    private JPanel createFooter() {

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        JLabel sdgLabel =
                new JLabel(
                        "Supporting SDG 12 - Responsible Consumption and Production",
                        SwingConstants.CENTER
                );

        sdgLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        panel.add(
                sdgLabel,
                BorderLayout.CENTER
        );

        return panel;
    }
}