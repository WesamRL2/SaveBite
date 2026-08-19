package com.savebite.app;

import javax.swing.SwingUtilities;

import com.savebite.ui.MainFrame;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);

        });
    }
}