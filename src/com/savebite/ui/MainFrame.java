package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class MainFrame extends JFrame {

    public MainFrame() {

        setTitle("SaveBite - Surplus Food Marketplace");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createInterface();
    }

    private void createInterface() {

        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createDashboard(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        20,
                        20,
                        20
                )
        );

        JLabel title = new JLabel(
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

        JLabel subtitle = new JLabel(
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

        panel.add(title, BorderLayout.CENTER);
        panel.add(subtitle, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDashboard() {

        JPanel panel = new JPanel(
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

        panel.add(createMenuButton("Available Deals"));
        panel.add(createMenuButton("Add Surplus Product"));
        panel.add(createMenuButton("My Orders"));
        panel.add(createMenuButton("Statistics"));

        return panel;
    }

    private JButton createMenuButton(String text) {

        JButton button = new JButton(text);

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
}