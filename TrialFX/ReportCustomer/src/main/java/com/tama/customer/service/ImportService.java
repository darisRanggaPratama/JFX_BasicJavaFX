package com.tama.customer.service;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.tama.customer.dao.CustomerDAO;
import com.tama.customer.model.Customer;
import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportService {
    private final CustomerDAO customerDAO;

    public ImportService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public static class ImportResult {
        private final List<Customer> successfulImports;
        private final Map<Integer, String> failures;
        private final int totalRows;

        public ImportResult(List<Customer> successfulImports, Map<Integer, String> failures, int totalRows) {
            this.successfulImports = successfulImports;
            this.failures = failures;
            this.totalRows = totalRows;
        }

        public List<Customer> getSuccessfulImports() {
            return successfulImports;
        }

        public Map<Integer, String> getFailures() {
            return failures;
        }

        public int getTotalRows() {
            return totalRows;
        }
    }

    public interface ImportProgressCallback {
        void onProgress(double progress);
    }

    public ImportResult importFromCSV(File file, ImportProgressCallback progressCallback) throws IOException {
        List<Customer> successfulImports = new ArrayList<>();
        Map<Integer, String> failures = new HashMap<>();
        int totalRows = 0;

        // Count total rows first
        int totalLines = 0;
        try (var lineReader = new FileReader(file)) {
            var bufferedReader = new java.io.BufferedReader(lineReader);
            while (bufferedReader.readLine() != null) totalLines++;
        }
        totalLines--; // Subtract header row

        var csvParser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        try (var reader = new CSVReaderBuilder(new FileReader(file))
                .withCSVParser(csvParser)
                .build()) {

            // Skip header
            reader.readNext();
            
            String[] line;
            int rowNum = 1; // Starting from 1 since we skipped header
            while ((line = reader.readNext()) != null) {
                totalRows++;
                try {
                    if (line.length < 5) {
                        failures.put(rowNum, "Insufficient columns");
                        continue;
                    }

                    Customer customer = new Customer();
                    customer.setNik(line[0].trim());
                    customer.setName(line[1].trim());
                    try {
                        customer.setBorn(LocalDate.parse(line[2].trim()));
                    } catch (DateTimeParseException e) {
                        failures.put(rowNum, "Invalid date format in born column");
                        continue;
                    }
                    customer.setActive(line[3].trim().equals("1"));
                    try {
                        customer.setSalary(new BigDecimal(line[4].trim()));
                    } catch (NumberFormatException e) {
                        failures.put(rowNum, "Invalid salary format");
                        continue;
                    }

                    successfulImports.add(customer);
                } catch (Exception e) {
                    failures.put(rowNum, "Error processing row: " + e.getMessage());
                }
                rowNum++;

                // Report progress
                if (progressCallback != null) {
                    double progress = (double) totalRows / totalLines;
                    progressCallback.onProgress(progress);
                }
            }
        } catch (CsvValidationException e) {
            throw new IOException("Error reading CSV file", e);
        }

        return new ImportResult(successfulImports, failures, totalRows);
    }

    // Keep old method for backward compatibility
    public ImportResult importFromCSV(File file) throws IOException {
        return importFromCSV(file, null);
    }

    public ImportResult importFromExcel(File file) throws IOException {
        List<Customer> successfulImports = new ArrayList<>();
        Map<Integer, String> failures = new HashMap<>();
        int totalRows = 0;

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = file.getName().endsWith(".xlsx") ? 
                                new XSSFWorkbook(fis) : new HSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            int rowNum = 1; // Skip header row

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header
                totalRows++;
                
                try {
                    Customer customer = new Customer();
                    
                    // NIK
                    Cell nikCell = row.getCell(0);
                    if (nikCell == null) {
                        failures.put(rowNum, "NIK is required");
                        continue;
                    }
                    customer.setNik(getCellValueAsString(nikCell));

                    // Name
                    Cell nameCell = row.getCell(1);
                    if (nameCell == null) {
                        failures.put(rowNum, "Name is required");
                        continue;
                    }
                    customer.setName(getCellValueAsString(nameCell));

                    // Born date
                    Cell bornCell = row.getCell(2);
                    if (bornCell == null) {
                        failures.put(rowNum, "Born date is required");
                        continue;
                    }
                    try {
                        if (bornCell.getCellType() == CellType.NUMERIC) {
                            customer.setBorn(bornCell.getLocalDateTimeCellValue().toLocalDate());
                        } else {
                            customer.setBorn(LocalDate.parse(getCellValueAsString(bornCell)));
                        }
                    } catch (Exception e) {
                        failures.put(rowNum, "Invalid born date format");
                        continue;
                    }

                    // Active
                    Cell activeCell = row.getCell(3);
                    if (activeCell != null) {
                        // Handle both numeric (0/1) and boolean values
                        if (activeCell.getCellType() == CellType.NUMERIC) {
                            customer.setActive(activeCell.getNumericCellValue() == 1);
                        } else if (activeCell.getCellType() == CellType.BOOLEAN) {
                            customer.setActive(activeCell.getBooleanCellValue());
                        } else {
                            String value = getCellValueAsString(activeCell).trim();
                            customer.setActive(value.equals("1") || value.equalsIgnoreCase("true"));
                        }
                    } else {
                        customer.setActive(false);
                    }

                    // Salary
                    Cell salaryCell = row.getCell(4);
                    if (salaryCell == null) {
                        failures.put(rowNum, "Salary is required");
                        continue;
                    }
                    try {
                        if (salaryCell.getCellType() == CellType.NUMERIC) {
                            customer.setSalary(BigDecimal.valueOf(salaryCell.getNumericCellValue()));
                        } else {
                            customer.setSalary(new BigDecimal(getCellValueAsString(salaryCell)));
                        }
                    } catch (Exception e) {
                        failures.put(rowNum, "Invalid salary format");
                        continue;
                    }

                    successfulImports.add(customer);
                } catch (Exception e) {
                    failures.put(rowNum, "Error processing row: " + e.getMessage());
                }
                rowNum++;
            }
        }

        return new ImportResult(successfulImports, failures, totalRows);
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    public void saveImportedCustomers(List<Customer> customers) throws SQLException {
        customerDAO.saveAll(customers);
    }
}
