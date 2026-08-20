package com.savebite.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.savebite.report.ExcelExporter;
import com.savebite.report.ExcelExporter.ReportPeriod;

public class ExportReportPanel extends JPanel {

    private final Runnable backAction;
    private Path lastExportedFile;

    public ExportReportPanel(Runnable backAction) {

        this.backAction = backAction;

        setLayout(new BorderLayout(20, 20));
        setBackground(UITheme.BACKGROUND);

        setBorder(
                BorderFactory.createEmptyBorder(
                        25, 35, 35, 35
                )
        );

        add(createHeader(), BorderLayout.NORTH);
        add(createExportOptions(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JButton backButton = UITheme.createBackButton();
        backButton.addActionListener(e -> backAction.run());

        JLabel title = new JLabel(
                "Export Activity Report",
                SwingConstants.CENTER
        );
        title.setFont(UITheme.TITLE_FONT);
        title.setForeground(UITheme.TEXT);

        JLabel subtitle = new JLabel(
                "Create a detailed Excel workbook for today, this week, or this month.",
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

    private JPanel createExportOptions() {

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(
                BorderFactory.createEmptyBorder(
                        60, 120, 60, 120
                )
        );

        JPanel grid = new JPanel(new GridLayout(1, 3, 25, 25));
        grid.setOpaque(false);

        JButton todayButton = UITheme.createPrimaryButton(
                "<html><center>Export Today<br><span style='font-size:10px;'>Current day activity</span></center></html>"
        );

        JButton weekButton = UITheme.createPrimaryButton(
                "<html><center>Export This Week<br><span style='font-size:10px;'>Monday to Sunday</span></center></html>"
        );

        JButton monthButton = UITheme.createPrimaryButton(
                "<html><center>Export This Month<br><span style='font-size:10px;'>Current calendar month</span></center></html>"
        );

        todayButton.addActionListener(
                e -> exportReport(ReportPeriod.TODAY)
        );

        weekButton.addActionListener(
                e -> exportReport(ReportPeriod.THIS_WEEK)
        );

        monthButton.addActionListener(
                e -> exportReport(ReportPeriod.THIS_MONTH)
        );

        grid.add(todayButton);
        grid.add(weekButton);
        grid.add(monthButton);

        wrapper.add(grid, BorderLayout.CENTER);

        return wrapper;
    }

    private JPanel createFooter() {

        JPanel panel = new JPanel();
        panel.setOpaque(false);

        JButton openFolderButton = new JButton("Open Exports Folder");
        openFolderButton.setFont(UITheme.NORMAL_FONT);

        openFolderButton.addActionListener(
                e -> openExportsFolder()
        );

        panel.add(openFolderButton);

        return panel;
    }

    private void exportReport(ReportPeriod period) {

        try {

            lastExportedFile = ExcelExporter.export(period);

            JOptionPane.showMessageDialog(
                    this,
                    "Excel report exported successfully.\n\n"
                            + lastExportedFile,
                    "SaveBite Export",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "The Excel report could not be created.\n"
                            + e.getMessage(),
                    "Export Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void openExportsFolder() {

        try {

            Path folder;

            if (lastExportedFile != null) {
                folder = lastExportedFile.getParent();
            } else {
                folder = Path.of("exports").toAbsolutePath();
            }

            java.nio.file.Files.createDirectories(folder);

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(folder.toFile());
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Exports folder:\n" + folder,
                        "SaveBite",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (IOException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Could not open the exports folder.\n"
                            + e.getMessage(),
                    "SaveBite",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
