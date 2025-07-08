package com.tama.jfxcrud.util;

import com.tama.jfxcrud.dao.CustomerDAO;
import com.tama.jfxcrud.model.Customer;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVHandler {
    private final CustomerDAO customerDAO;
    private static final String DELIMITER = ";";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public CSVHandler() {
        this.customerDAO = new CustomerDAO();
    }
    
    public Map<String, Integer> importCSV(File file) {
        Map<String, Integer> result = new HashMap<>();
        int success = 0;
        int failed = 0;
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip header
                }
                
                try {
                    String[] values = line.split(DELIMITER);
                    if (values.length >= 5) {
                        Customer customer = new Customer();
                        customer.setNik(values[1].trim());
                        customer.setName(values[2].trim());
                        
                        try {
                            customer.setBorn(LocalDate.parse(values[3].trim(), DATE_FORMATTER));
                        } catch (DateTimeParseException e) {
                            customer.setBorn(null);
                        }
                        
                        customer.setActive(Integer.parseInt(values[4].trim()) == 1);
                        
                        if (values.length > 5) {
                            try {
                                customer.setSalary(Integer.parseInt(values[5].trim()));
                            } catch (NumberFormatException e) {
                                customer.setSalary(0);
                            }
                        }
                        
                        customerDAO.addCustomer(customer);
                        success++;
                    } else {
                        failed++;
                    }
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
    
    public Map<String, Integer> exportCSV(File file, List<Customer> customers) {
        Map<String, Integer> result = new HashMap<>();
        int success = 0;
        int failed = 0;
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            // Write header
            writer.println("idx;nik;name;born;active;salary");
            
            // Write data
            for (Customer customer : customers) {
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(customer.getIdx()).append(DELIMITER);
                    sb.append(customer.getNik()).append(DELIMITER);
                    sb.append(customer.getName()).append(DELIMITER);
                    sb.append(customer.getBorn() != null ? customer.getBorn().format(DATE_FORMATTER) : "").append(DELIMITER);
                    sb.append(customer.isActive() ? "1" : "0").append(DELIMITER);
                    sb.append(customer.getSalary());
                    
                    writer.println(sb.toString());
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
}