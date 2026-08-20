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

        setLayout(new BorderLayout(20, 20));

        setBackground(UITheme.BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 25, 35
                )
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createStatisticsGrid(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
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
                        "Sustainability Impact",
                        SwingConstants.CENTER
                );

        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT);

        JLabel subtitle =
                new JLabel(
                        "See how SaveBite reduces food waste and recovers value.",
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

        panel.setOpaque(false);

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25, 15, 25, 15
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
                                8,
                                8
                        )
                );

        card.setBackground(UITheme.CARD);

        card.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                UITheme.BORDER
                        ),

                        BorderFactory.createEmptyBorder(
                                25,
                                12,
                                25,
                                12
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
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        titleLabel.setForeground(
                UITheme.MUTED_TEXT
        );

        JLabel valueLabel =
                new JLabel(
                        value,
                        SwingConstants.CENTER
                );

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        valueLabel.setForeground(
                UITheme.PRIMARY
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
                new JPanel(new BorderLayout());

        panel.setOpaque(false);

        JLabel sdgLabel =
                new JLabel(
                        "SDG 12 - Responsible Consumption and Production",
                        SwingConstants.CENTER
                );

        sdgLabel.setFont(UITheme.NORMAL_FONT);

        sdgLabel.setForeground(
                UITheme.MUTED_TEXT
        );

        panel.add(
                sdgLabel,
                BorderLayout.CENTER
        );

        return panel;
    }
}