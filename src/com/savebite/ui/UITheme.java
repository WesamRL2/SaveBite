package com.savebite.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTable;

public final class UITheme {

    public static final Color PRIMARY =
            new Color(39, 140, 94);

    public static final Color PRIMARY_DARK =
            new Color(28, 105, 70);

    public static final Color BACKGROUND =
            new Color(245, 248, 246);

    public static final Color CARD =
            Color.WHITE;

    public static final Color TEXT =
            new Color(35, 45, 40);

    public static final Color MUTED_TEXT =
            new Color(100, 110, 105);

    public static final Color BORDER =
            new Color(220, 228, 223);

    public static final Color LIGHT_GREEN =
            new Color(226, 242, 234);

    public static final Font TITLE_FONT =
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    32
            );

    public static final Font SUBTITLE_FONT =
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    16
            );

    public static final Font BUTTON_FONT =
            new Font(
                    "Segoe UI",
                    Font.BOLD,
                    16
            );

    public static final Font NORMAL_FONT =
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    14
            );

    private UITheme() {
        // Utility class
    }

    public static JButton createPrimaryButton(
            String text) {

        JButton button =
                new JButton(text);

        button.setFont(BUTTON_FONT);

        button.setBackground(PRIMARY);

        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createEmptyBorder(
                        14,
                        20,
                        14,
                        20
                )
        );

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e) {

                        button.setBackground(
                                PRIMARY_DARK
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e) {

                        button.setBackground(
                                PRIMARY
                        );
                    }
                }
        );

        return button;
    }

    public static JButton createBackButton() {

        JButton button =
                new JButton("← Back");

        button.setFont(NORMAL_FONT);

        button.setBackground(
                LIGHT_GREEN
        );

        button.setForeground(
                PRIMARY_DARK
        );

        button.setFocusPainted(false);

        button.setCursor(
                Cursor.getPredefinedCursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    public static void styleTable(
            JTable table) {

        table.setFont(NORMAL_FONT);

        table.setRowHeight(32);

        table.setGridColor(BORDER);

        table.setSelectionBackground(
                LIGHT_GREEN
        );

        table.setSelectionForeground(
                TEXT
        );

        table.getTableHeader()
                .setFont(
                        new Font(
                                "Segoe UI",
                                Font.BOLD,
                                14
                        )
                );

        table.getTableHeader()
                .setBackground(
                        PRIMARY
                );

        table.getTableHeader()
                .setForeground(
                        Color.WHITE
                );
    }
}