package com.tama.jfxcrud.util;

import com.tama.jfxcrud.dao.CustomerDAO;
import com.tama.jfxcrud.model.Customer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelHandler {
    private final CustomerDAO customerDAO;
    
    public ExcelHandler() {
        this.customerDAO = new CustomerDAO();
    }
    
    public Map<String, Integer> importExcel(File file) {
        Map<String, Integer> result = new HashMap<>();
        int success = 0;
        int failed = 0;
        
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            
            for (Row row : sheet) {
                if (isFirstRow) {
                    isFirstRow = false;
                    continue; // Skip header
                }
                
                try {
                    Customer customer = new Customer();
                    
                    // NIK
                    Cell nikCell = row.getCell(1);
                    if (nikCell != null) {
                        customer.setNik(getCellValueAsString(nikCell));
                    }
                    
                    // Name
                    Cell nameCell = row.getCell(2);
                    if (nameCell != null) {
                        customer.setName(getCellValueAsString(nameCell));
                    }
                    
                    // Born Date
                    Cell bornCell = row.getCell(3);
                    if (bornCell != null && bornCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(bornCell)) {
                        Date date = bornCell.getDateCellValue();
                        customer.setBorn(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    }
                    
                    // Active
                    Cell activeCell = row.getCell(4);
                    if (activeCell != null) {
                        if (activeCell.getCellType() == CellType.BOOLEAN) {
                            customer.setActive(activeCell.getBooleanCellValue());
                        } else if (activeCell.getCellType() == CellType.NUMERIC) {
                            customer.setActive(activeCell.getNumericCellValue() == 1);
                        } else if (activeCell.getCellType() == CellType.STRING) {
                            String value = activeCell.getStringCellValue().toLowerCase();
                            customer.setActive(value.equals("yes") || value.equals("true") || value.equals("1"));
                        }
                    }
                    
                    // Salary
                    Cell salaryCell = row.getCell(5);
                    if (salaryCell != null && salaryCell.getCellType() == CellType.NUMERIC) {
                        customer.setSalary((int) salaryCell.getNumericCellValue());
                    }
                    
                    customerDAO.addCustomer(customer);
                    success++;
                } catch (Exception e) {
                    failed++;
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            failed++;
        }
        
        result.put("success", success);
        result.put("failed", failed);
        return result;
    }
    
    public Map<String, Integer> exportExcel(File file, List<Customer> customers) {
        Map<String, Integer> result = new HashMap<>();
        int success = 0;
        int failed = 0;
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Customers");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("NIK");
            headerRow.createCell(2).setCellValue("Name");
            headerRow.createCell(3).setCellValue("Born Date");
            headerRow.createCell(4).setCellValue("Active");
            headerRow.createCell(5).setCellValue("Salary");
            
            // Create data rows
            int rowNum = 1;
            for (Customer customer : customers) {
                try {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(customer.getIdx());
                    row.createCell(1).setCellValue(customer.getNik());
                    row.createCell(2).setCellValue(customer.getName());
                    
                    if (customer.getBorn() != null) {
                        Cell dateCell = row.createCell(3);
                        dateCell.setCellValue(Date.from(customer.getBorn().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                        
                        CellStyle dateStyle = workbook.createCellStyle();
                        CreationHelper createHelper = workbook.getCreationHelper();
                        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));
                        dateCell.setCellStyle(dateStyle);
                    }
                    
                    row.createCell(4).setCellValue(customer.isActive());
                    row.createCell(5).setCellValue(customer.getSalary());
                    
                    success++;
                } catch (Exception e) {
                    failed++;
                }
            }
            
            // Auto-size columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            failed++;
        }
        
        result.put("success", success);
        result.put("failed", failed);
        return result;
    }
    
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalDate().toString();
                }
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}
