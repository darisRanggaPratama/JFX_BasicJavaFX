package com.tama.crudxmljavafx.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.tama.crudxmljavafx.model.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations (CSV and Excel)
 */
public class FileUtil {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Export customers to CSV file
     * @param customers List of customers to export
     * @param filePath Output file path
     * @return true if successful, false otherwise
     */
    public static boolean exportToCSV(List<Customer> customers, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath), ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {
            // Write header
            String[] header = {"idx", "nik", "name", "born", "active", "salary"};
            writer.writeNext(header);
            
            // Write data
            for (Customer customer : customers) {
                String[] data = {
                    customer.getIdx() != null ? customer.getIdx().toString() : "",
                    customer.getNik() != null ? customer.getNik() : "",
                    customer.getName() != null ? customer.getName() : "",
                    customer.getBorn() != null ? customer.getBorn().format(DATE_FORMATTER) : "",
                    customer.getActive() != null ? (customer.getActive() ? "1" : "0") : "0",
                    customer.getSalary() != null ? customer.getSalary().toString() : "0"
                };
                writer.writeNext(data);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Import customers from CSV file
     * @param filePath Input file path
     * @return List of customers imported
     */
    public static List<Customer> importFromCSV(String filePath) {
        List<Customer> customers = new ArrayList<>();
        
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(filePath))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            List<String[]> records = reader.readAll();
            
            // Skip header row
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                
                if (record.length >= 6) {
                    try {
                        Customer customer = new Customer();
                        
                        // Skip idx for new records
                        customer.setNik(record[1].trim());
                        customer.setName(record[2].trim());
                        
                        // Parse born date
                        if (!record[3].trim().isEmpty()) {
                            customer.setBorn(LocalDate.parse(record[3].trim(), DATE_FORMATTER));
                        }
                        
                        // Parse active status
                        customer.setActive("1".equals(record[4].trim()));
                        
                        // Parse salary
                        if (!record[5].trim().isEmpty()) {
                            customer.setSalary(Integer.parseInt(record[5].trim()));
                        }
                        
                        customers.add(customer);
                    } catch (Exception e) {
                        System.err.println("Error parsing record at line " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        
        return customers;
    }
    
    /**
     * Export customers to Excel file
     * @param customers List of customers to export
     * @param filePath Output file path
     * @return true if successful, false otherwise
     */
    public static boolean exportToExcel(List<Customer> customers, String filePath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Customers");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"idx", "nik", "name", "born", "active", "salary"};
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                
                // Style header
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            for (int i = 0; i < customers.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Customer customer = customers.get(i);
                
                row.createCell(0).setCellValue(customer.getIdx() != null ? customer.getIdx() : 0);
                row.createCell(1).setCellValue(customer.getNik() != null ? customer.getNik() : "");
                row.createCell(2).setCellValue(customer.getName() != null ? customer.getName() : "");
                row.createCell(3).setCellValue(customer.getBorn() != null ? customer.getBorn().format(DATE_FORMATTER) : "");
                row.createCell(4).setCellValue(customer.getActive() != null ? (customer.getActive() ? 1 : 0) : 0);
                row.createCell(5).setCellValue(customer.getSalary() != null ? customer.getSalary() : 0);
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Import customers from Excel file
     * @param filePath Input file path
     * @return List of customers imported
     */
    public static List<Customer> importFromExcel(String filePath) {
        List<Customer> customers = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // Skip header row
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    try {
                        Customer customer = new Customer();
                        
                        // Skip idx for new records
                        customer.setNik(getCellValueAsString(row.getCell(1)));
                        customer.setName(getCellValueAsString(row.getCell(2)));
                        
                        // Parse born date
                        String bornStr = getCellValueAsString(row.getCell(3));
                        if (!bornStr.isEmpty()) {
                            customer.setBorn(LocalDate.parse(bornStr, DATE_FORMATTER));
                        }
                        
                        // Parse active status
                        String activeStr = getCellValueAsString(row.getCell(4));
                        customer.setActive("1".equals(activeStr) || "true".equalsIgnoreCase(activeStr));
                        
                        // Parse salary
                        String salaryStr = getCellValueAsString(row.getCell(5));
                        if (!salaryStr.isEmpty()) {
                            customer.setSalary((int) Double.parseDouble(salaryStr));
                        }
                        
                        customers.add(customer);
                    } catch (Exception e) {
                        System.err.println("Error parsing Excel row " + (i + 1) + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return customers;
    }
    
    /**
     * Get cell value as string
     * @param cell Excel cell
     * @return String value
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().format(DATE_FORMATTER);
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() ? "1" : "0";
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
