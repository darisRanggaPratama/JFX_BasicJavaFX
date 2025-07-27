package com.tama.customer.service;

import com.opencsv.CSVWriter;
import com.tama.customer.model.Customer;
import javafx.concurrent.Task;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

public class ExportService {

    public Task<Void> exportToPdf(List<Customer> customers, String outputPath) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    // Load the report template
                    String reportPath = getClass().getResource("/reports/customers.jrxml").getPath();
                    JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

                    // Create data source
                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(customers);

                    // Fill report
                    JasperPrint jasperPrint = JasperFillManager.fillReport(
                            jasperReport, new HashMap<>(), dataSource);

                    // Export to PDF
                    JasperExportManager.exportReportToPdfFile(jasperPrint, outputPath);

                    return null;
                } catch (Exception e) {
                    throw new Exception("Failed to export PDF: " + e.getMessage());
                }
            }
        };
    }

    public Task<Void> exportToExcel(List<Customer> customers, String outputPath) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (Workbook workbook = new XSSFWorkbook()) {
                    Sheet sheet = workbook.createSheet("Customers");

                    // Create header row
                    Row headerRow = sheet.createRow(0);
                    String[] columns = {"NIK", "Name", "Birth Date", "Active", "Salary"};
                    for (int i = 0; i < columns.length; i++) {
                        Cell cell = headerRow.createCell(i);
                        cell.setCellValue(columns[i]);
                    }

                    // Create data rows
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    int rowNum = 1;
                    for (Customer customer : customers) {
                        Row row = sheet.createRow(rowNum++);
                        row.createCell(0).setCellValue(customer.getNik());
                        row.createCell(1).setCellValue(customer.getName());
                        row.createCell(2).setCellValue(customer.getBorn().format(dateFormatter));
                        row.createCell(3).setCellValue(customer.isActive());
                        row.createCell(4).setCellValue(customer.getSalary().doubleValue());
                    }

                    // Auto-size columns
                    for (int i = 0; i < columns.length; i++) {
                        sheet.autoSizeColumn(i);
                    }

                    // Write to file
                    try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                        workbook.write(fileOut);
                    }

                    return null;
                } catch (IOException e) {
                    throw new Exception("Failed to export Excel: " + e.getMessage());
                }
            }
        };
    }

    public Task<Void> exportToCsv(List<Customer> customers, String outputPath) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                try (CSVWriter writer = new CSVWriter(new FileWriter(outputPath),
                        ';', // Using semicolon as separator
                        CSVWriter.DEFAULT_QUOTE_CHARACTER,
                        CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                        CSVWriter.DEFAULT_LINE_END)) {

                    // Write header
                    writer.writeNext(new String[]{"NIK", "Name", "Birth Date", "Active", "Salary"});

                    // Write data
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    for (Customer customer : customers) {
                        writer.writeNext(new String[]{
                                customer.getNik(),
                                customer.getName(),
                                customer.getBorn().format(dateFormatter),
                                String.valueOf(customer.isActive()),
                                customer.getSalary().toString()
                        });
                    }

                    return null;
                } catch (IOException e) {
                    throw new Exception("Failed to export CSV: " + e.getMessage());
                }
            }
        };
    }
}
