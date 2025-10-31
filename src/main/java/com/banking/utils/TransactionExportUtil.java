package com.banking.utils;

import com.banking.entities.Transaction;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.util.List;

public class TransactionExportUtil {

    public static File exportToExcel(String accountNumber, List<Transaction> transactions) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");


        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);


        String[] headers = {"Transaction ID", "Date", "Description", "Sender Account", "Receiver Account", "Amount", "Status"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }


        int rowNum = 1;
        for (Transaction t : transactions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(t.getId());
            row.createCell(1).setCellValue(t.getCreatedAt() != null ? t.getCreatedAt().toString() : "");
            row.createCell(2).setCellValue(t.getDescription());
            row.createCell(3).setCellValue(t.getSenderAccount());
            row.createCell(4).setCellValue(t.getReceiverAccount());
            row.createCell(5).setCellValue(t.getAmount() != null ? t.getAmount().doubleValue() : 0);
            row.createCell(6).setCellValue(t.getStatus() != null ? t.getStatus().name() : "");
        }


        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }


        File file = File.createTempFile("transactions_" + accountNumber + "_", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
        workbook.close();

        return file;
    }

    public static File exportToCSV(String accountNumber, List<Transaction> transactions) throws IOException {
        File file = File.createTempFile("transactions_" + accountNumber + "_", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Transaction ID,Date,Description,Sender Account,Receiver Account,Amount,Status\n");
            for (Transaction t : transactions) {
                writer.write(String.format("%d,%s,%s,%s,%s,%.2f,%s\n",
                        t.getId(),
                        t.getCreatedAt() != null ? t.getCreatedAt().toString() : "",
                        safeCsv(t.getDescription()),
                        safeCsv(t.getSenderAccount()),
                        safeCsv(t.getReceiverAccount()),
                        t.getAmount() != null ? t.getAmount().doubleValue() : 0,
                        t.getStatus() != null ? t.getStatus().name() : ""));
            }
        }
        return file;
    }

    private static String safeCsv(String value) {
        if (value == null) return "";
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
