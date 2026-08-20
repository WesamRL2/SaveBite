package com.savebite.report;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.savebite.model.ActivityRecord;
import com.savebite.storage.ActivityLogger;

public final class ExcelExporter {

    public enum ReportPeriod {
        TODAY,
        THIS_WEEK,
        THIS_MONTH
    }

    private static final Path EXPORT_DIRECTORY = Paths.get("exports");
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter FILE_TIME =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");

    private ExcelExporter() {
    }

    public static Path export(ReportPeriod period)
            throws IOException {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        LocalDateTime end;
        String label;
        String fileName;

        switch (period) {
            case TODAY -> {
                start = now.toLocalDate().atStartOfDay();
                end = start.plusDays(1);
                label = "Today";
                fileName = "SaveBite_Daily_"
                        + now.toLocalDate()
                        + "_"
                        + FILE_TIME.format(now)
                        + ".xlsx";
            }

            case THIS_WEEK -> {
                LocalDate monday = now.toLocalDate()
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

                start = monday.atStartOfDay();
                end = start.plusWeeks(1);
                label = "This Week";

                fileName = "SaveBite_Weekly_"
                        + start.toLocalDate()
                        + "_to_"
                        + end.minusDays(1).toLocalDate()
                        + "_"
                        + FILE_TIME.format(now)
                        + ".xlsx";
            }

            case THIS_MONTH -> {
                LocalDate firstDay = now.toLocalDate().withDayOfMonth(1);

                start = firstDay.atStartOfDay();
                end = start.plusMonths(1);
                label = "This Month";

                fileName = "SaveBite_Monthly_"
                        + now.getYear()
                        + "-"
                        + String.format("%02d", now.getMonthValue())
                        + "_"
                        + FILE_TIME.format(now)
                        + ".xlsx";
            }

            default -> throw new IllegalArgumentException("Unsupported report period.");
        }

        List<ActivityRecord> filtered = new ArrayList<>();

        for (ActivityRecord activity : ActivityLogger.loadActivities()) {

            LocalDateTime timestamp = activity.getTimestamp();

            if (!timestamp.isBefore(start) && timestamp.isBefore(end)) {
                filtered.add(activity);
            }
        }

        filtered.sort(
                Comparator.comparing(ActivityRecord::getTimestamp)
        );

        Files.createDirectories(EXPORT_DIRECTORY);

        Path outputFile = EXPORT_DIRECTORY.resolve(fileName);

        writeWorkbook(
                outputFile,
                filtered,
                label,
                start,
                end
        );

        return outputFile.toAbsolutePath();
    }

    private static void writeWorkbook(
            Path outputFile,
            List<ActivityRecord> activities,
            String periodLabel,
            LocalDateTime start,
            LocalDateTime end)
            throws IOException {

        try (OutputStream outputStream = Files.newOutputStream(outputFile);
             ZipOutputStream zip = new ZipOutputStream(outputStream)) {

            writeEntry(zip, "[Content_Types].xml", contentTypesXml());
            writeEntry(zip, "_rels/.rels", rootRelationshipsXml());
            writeEntry(zip, "xl/workbook.xml", workbookXml());
            writeEntry(zip, "xl/_rels/workbook.xml.rels", workbookRelationshipsXml());
            writeEntry(zip, "xl/styles.xml", stylesXml());
            writeEntry(
                    zip,
                    "xl/worksheets/sheet1.xml",
                    buildSummarySheet(
                            activities,
                            periodLabel,
                            start,
                            end
                    )
            );
            writeEntry(
                    zip,
                    "xl/worksheets/sheet2.xml",
                    buildDetailSheet(activities)
            );
        }
    }

    private static String buildSummarySheet(
            List<ActivityRecord> activities,
            String periodLabel,
            LocalDateTime start,
            LocalDateTime end) {

        int productsAdded = 0;
        int reservations = 0;
        int cancellations = 0;
        int collections = 0;
        int unitsReserved = 0;

        double reservationValue = 0.0;
        double cancelledValue = 0.0;
        double collectedValue = 0.0;
        double customerSavings = 0.0;

        for (ActivityRecord activity : activities) {

            switch (activity.getEventType()) {
                case "PRODUCT_ADDED" -> productsAdded++;

                case "ORDER_RESERVED" -> {
                    reservations++;
                    unitsReserved += activity.getQuantity();
                    reservationValue += activity.getTotalAmount();
                    customerSavings +=
                            (activity.getOriginalPrice() - activity.getSaveBitePrice())
                                    * activity.getQuantity();
                }

                case "ORDER_CANCELLED" -> {
                    cancellations++;
                    cancelledValue += activity.getTotalAmount();
                }

                case "ORDER_COLLECTED" -> {
                    collections++;
                    collectedValue += activity.getTotalAmount();
                }

                default -> {
                }
            }
        }

        StringBuilder rows = new StringBuilder();

        rows.append(row(1,
                stringCell(1, 1, "SaveBite Activity Report", true)));

        rows.append(row(3,
                stringCell(3, 1, "Metric", true)
                        + stringCell(3, 2, "Value", true)));

        int row = 4;
        rows.append(summaryRow(row++, "Period", periodLabel));
        rows.append(summaryRow(row++, "Start", DISPLAY_TIME.format(start)));
        rows.append(summaryRow(row++, "End", DISPLAY_TIME.format(end.minusSeconds(1))));
        rows.append(summaryRow(row++, "Total Activity Events", String.valueOf(activities.size())));
        rows.append(summaryRow(row++, "Products Added", String.valueOf(productsAdded)));
        rows.append(summaryRow(row++, "Reservations Created", String.valueOf(reservations)));
        rows.append(summaryRow(row++, "Reservations Cancelled", String.valueOf(cancellations)));
        rows.append(summaryRow(row++, "Orders Collected", String.valueOf(collections)));
        rows.append(summaryRow(row++, "Units Reserved", String.valueOf(unitsReserved)));
        rows.append(summaryRow(row++, "Reservation Value", String.format("RM %.2f", reservationValue)));
        rows.append(summaryRow(row++, "Cancelled Value", String.format("RM %.2f", cancelledValue)));
        rows.append(summaryRow(row++, "Collected Value", String.format("RM %.2f", collectedValue)));
        rows.append(summaryRow(row, "Customer Savings on Reservations", String.format("RM %.2f", customerSavings)));

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<cols>"
                + "<col min=\"1\" max=\"1\" width=\"34\" customWidth=\"1\"/>"
                + "<col min=\"2\" max=\"2\" width=\"36\" customWidth=\"1\"/>"
                + "</cols>"
                + "<sheetData>" + rows + "</sheetData>"
                + "<mergeCells count=\"1\"><mergeCell ref=\"A1:B1\"/></mergeCells>"
                + "</worksheet>";
    }

    private static String summaryRow(
            int row,
            String metric,
            String value) {

        return row(
                row,
                stringCell(row, 1, metric, false)
                        + stringCell(row, 2, value, false)
        );
    }

    private static String buildDetailSheet(
            List<ActivityRecord> activities) {

        String[] headers = {
                "Timestamp",
                "Event Type",
                "Reference ID",
                "Product ID",
                "Product Name",
                "Quantity",
                "Original Price (RM)",
                "SaveBite Price (RM)",
                "Event Value (RM)",
                "Status",
                "Customer ID",
                "Seller ID",
                "Details"
        };

        StringBuilder rows = new StringBuilder();
        StringBuilder headerCells = new StringBuilder();

        for (int column = 0; column < headers.length; column++) {
            headerCells.append(
                    stringCell(
                            1,
                            column + 1,
                            headers[column],
                            true
                    )
            );
        }

        rows.append(row(1, headerCells.toString()));

        int rowNumber = 2;

        for (ActivityRecord activity : activities) {

            String cells =
                    stringCell(rowNumber, 1, DISPLAY_TIME.format(activity.getTimestamp()), false)
                            + stringCell(rowNumber, 2, activity.getEventType(), false)
                            + stringCell(rowNumber, 3, activity.getReferenceId(), false)
                            + stringCell(rowNumber, 4, activity.getProductId(), false)
                            + stringCell(rowNumber, 5, activity.getProductName(), false)
                            + numberCell(rowNumber, 6, activity.getQuantity())
                            + numberCell(rowNumber, 7, activity.getOriginalPrice())
                            + numberCell(rowNumber, 8, activity.getSaveBitePrice())
                            + numberCell(rowNumber, 9, activity.getTotalAmount())
                            + stringCell(rowNumber, 10, activity.getStatus(), false)
                            + stringCell(rowNumber, 11, activity.getCustomerId(), false)
                            + stringCell(rowNumber, 12, activity.getSellerId(), false)
                            + stringCell(rowNumber, 13, activity.getDetails(), false);

            rows.append(row(rowNumber, cells));
            rowNumber++;
        }

        int lastRow = Math.max(1, rowNumber - 1);

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<worksheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<sheetViews><sheetView workbookViewId=\"0\">"
                + "<pane ySplit=\"1\" topLeftCell=\"A2\" activePane=\"bottomLeft\" state=\"frozen\"/>"
                + "</sheetView></sheetViews>"
                + "<cols>"
                + "<col min=\"1\" max=\"1\" width=\"22\" customWidth=\"1\"/>"
                + "<col min=\"2\" max=\"2\" width=\"20\" customWidth=\"1\"/>"
                + "<col min=\"3\" max=\"4\" width=\"16\" customWidth=\"1\"/>"
                + "<col min=\"5\" max=\"5\" width=\"24\" customWidth=\"1\"/>"
                + "<col min=\"6\" max=\"6\" width=\"12\" customWidth=\"1\"/>"
                + "<col min=\"7\" max=\"9\" width=\"18\" customWidth=\"1\"/>"
                + "<col min=\"10\" max=\"12\" width=\"16\" customWidth=\"1\"/>"
                + "<col min=\"13\" max=\"13\" width=\"48\" customWidth=\"1\"/>"
                + "</cols>"
                + "<sheetData>" + rows + "</sheetData>"
                + "<autoFilter ref=\"A1:M" + lastRow + "\"/>"
                + "</worksheet>";
    }

    private static String row(
            int rowNumber,
            String cells) {

        return "<row r=\"" + rowNumber + "\">"
                + cells
                + "</row>";
    }

    private static String stringCell(
            int row,
            int column,
            String value,
            boolean header) {

        String style = header ? " s=\"1\"" : "";

        return "<c r=\""
                + cellReference(row, column)
                + "\" t=\"inlineStr\""
                + style
                + "><is><t>"
                + escapeXml(value)
                + "</t></is></c>";
    }

    private static String numberCell(
            int row,
            int column,
            double value) {

        return "<c r=\""
                + cellReference(row, column)
                + "\"><v>"
                + value
                + "</v></c>";
    }

    private static String cellReference(
            int row,
            int column) {

        return columnName(column) + row;
    }

    private static String columnName(int column) {

        StringBuilder result = new StringBuilder();
        int value = column;

        while (value > 0) {
            int remainder = (value - 1) % 26;
            result.insert(0, (char) ('A' + remainder));
            value = (value - 1) / 26;
        }

        return result.toString();
    }

    private static String escapeXml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }

    private static void writeEntry(
            ZipOutputStream zip,
            String path,
            String content)
            throws IOException {

        ZipEntry entry = new ZipEntry(path);
        zip.putNextEntry(entry);
        zip.write(content.getBytes(StandardCharsets.UTF_8));
        zip.closeEntry();
    }

    private static String contentTypesXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Types xmlns=\"http://schemas.openxmlformats.org/package/2006/content-types\">"
                + "<Default Extension=\"rels\" ContentType=\"application/vnd.openxmlformats-package.relationships+xml\"/>"
                + "<Default Extension=\"xml\" ContentType=\"application/xml\"/>"
                + "<Override PartName=\"/xl/workbook.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet.main+xml\"/>"
                + "<Override PartName=\"/xl/styles.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.styles+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet1.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "<Override PartName=\"/xl/worksheets/sheet2.xml\" ContentType=\"application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml\"/>"
                + "</Types>";
    }

    private static String rootRelationshipsXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/officeDocument\" Target=\"xl/workbook.xml\"/>"
                + "</Relationships>";
    }

    private static String workbookXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<workbook xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\" "
                + "xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\">"
                + "<sheets>"
                + "<sheet name=\"Summary\" sheetId=\"1\" r:id=\"rId1\"/>"
                + "<sheet name=\"Activity Details\" sheetId=\"2\" r:id=\"rId2\"/>"
                + "</sheets>"
                + "</workbook>";
    }

    private static String workbookRelationshipsXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<Relationships xmlns=\"http://schemas.openxmlformats.org/package/2006/relationships\">"
                + "<Relationship Id=\"rId1\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet1.xml\"/>"
                + "<Relationship Id=\"rId2\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/worksheet\" Target=\"worksheets/sheet2.xml\"/>"
                + "<Relationship Id=\"rId3\" Type=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships/styles\" Target=\"styles.xml\"/>"
                + "</Relationships>";
    }

    private static String stylesXml() {

        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<styleSheet xmlns=\"http://schemas.openxmlformats.org/spreadsheetml/2006/main\">"
                + "<fonts count=\"2\">"
                + "<font><sz val=\"11\"/><name val=\"Calibri\"/></font>"
                + "<font><b/><color rgb=\"FFFFFFFF\"/><sz val=\"11\"/><name val=\"Calibri\"/></font>"
                + "</fonts>"
                + "<fills count=\"3\">"
                + "<fill><patternFill patternType=\"none\"/></fill>"
                + "<fill><patternFill patternType=\"gray125\"/></fill>"
                + "<fill><patternFill patternType=\"solid\"><fgColor rgb=\"FF278C5E\"/><bgColor indexed=\"64\"/></patternFill></fill>"
                + "</fills>"
                + "<borders count=\"1\"><border><left/><right/><top/><bottom/><diagonal/></border></borders>"
                + "<cellStyleXfs count=\"1\"><xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\"/></cellStyleXfs>"
                + "<cellXfs count=\"2\">"
                + "<xf numFmtId=\"0\" fontId=\"0\" fillId=\"0\" borderId=\"0\" xfId=\"0\"/>"
                + "<xf numFmtId=\"0\" fontId=\"1\" fillId=\"2\" borderId=\"0\" xfId=\"0\" applyFont=\"1\" applyFill=\"1\"/>"
                + "</cellXfs>"
                + "<cellStyles count=\"1\"><cellStyle name=\"Normal\" xfId=\"0\" builtinId=\"0\"/></cellStyles>"
                + "</styleSheet>";
    }
}
