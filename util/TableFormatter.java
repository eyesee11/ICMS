package util;

import ds.CustomArrayList;

public class TableFormatter {
    private String[] headers;
    private CustomArrayList<String[]> rows;
    private int[] columnWidths;
    private static final String HORIZONTAL_LINE = "-";
    private static final String VERTICAL_LINE = "|";
    private static final String CROSS = "+";
    private static final int PADDING = 1;

    public TableFormatter(String... headers) {
        this.headers = headers;
        this.rows = new CustomArrayList<>();
        this.columnWidths = new int[headers.length];

        for (int i = 0; i < headers.length; i++) {
            columnWidths[i] = headers[i].length();
        }
    }

    public void addRow(String... values) {
        if (values.length != headers.length) {
            throw new IllegalArgumentException("Row values count doesn't match header count. Expected: " +
                    headers.length + ", Got: " + values.length);
        }

        for (int i = 0; i < values.length; i++) {
            if (values[i] != null && values[i].length() > columnWidths[i]) {
                columnWidths[i] = values[i].length();
            }
        }

        rows.add(values);
    }

    public static String formatAttendance(boolean present) {
        return present ? "Present" : "Absent";
    }

    @Override
    public String toString() {
        calculateTableWidth();
        StringBuilder table = new StringBuilder();

        // Top border
        table.append(createHorizontalBorder()).append("\n");

        // Headers
        table.append(VERTICAL_LINE);
        for (int i = 0; i < headers.length; i++) {
            table.append(" ");
            String paddedHeader = padRight(headers[i], columnWidths[i]);
            table.append(paddedHeader);
            table.append(" ").append(VERTICAL_LINE);
        }
        table.append("\n");

        // Separator line
        table.append(createHorizontalBorder()).append("\n");

        // Data rows
        for (int i = 0; i < rows.size(); i++) {
            String[] rowValues = rows.get(i);
            table.append(VERTICAL_LINE);

            for (int j = 0; j < rowValues.length; j++) {
                table.append(" ");
                String value = rowValues[j] == null ? "" : rowValues[j];
                String paddedValue = padRight(value, columnWidths[j]);
                table.append(paddedValue);
                table.append(" ").append(VERTICAL_LINE);
            }

            table.append("\n");
        }

        // Bottom border
        table.append(createHorizontalBorder());

        return table.toString();
    }

    private void calculateTableWidth() {
        @SuppressWarnings("unused")
        int width = 1;

        for (int columnWidth : columnWidths) {
            width += columnWidth + (2 * PADDING) + 1;
        }
    }

    private String createHorizontalBorder() {
        StringBuilder border = new StringBuilder();

        for (int i = 0; i < headers.length; i++) {
            if (i == 0) {
                border.append(CROSS);
            }

            int width = columnWidths[i] + (2 * PADDING);
            for (int j = 0; j < width; j++) {
                border.append(HORIZONTAL_LINE);
            }

            border.append(CROSS);
        }

        return border.toString();
    }

    private String padRight(String str, int length) {
        if (str == null) {
            str = "";
        }

        if (str.length() >= length) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str);
        while (sb.length() < length) {
            sb.append(" ");
        }

        return sb.toString();
    }

    public int getRowCount() {
        return rows.size();
    }
}