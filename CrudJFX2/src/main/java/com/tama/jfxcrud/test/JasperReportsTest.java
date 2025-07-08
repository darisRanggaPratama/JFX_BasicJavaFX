package com.tama.jfxcrud.test;

import com.tama.jfxcrud.model.Customer;
import com.tama.jfxcrud.util.JasperReportsUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple test class to verify JasperReports functionality
 */
public class JasperReportsTest {
    
    public static void main(String[] args) {
        System.out.println("=== JasperReports Test Started ===");
        
        try {
            // Create test data
            List<Customer> testCustomers = createTestData();
            System.out.println("Created " + testCustomers.size() + " test customers");
            
            // Test report generation
            System.out.println("Testing report generation...");
            Object jasperPrint = JasperReportsUtil.generateReport(testCustomers);
            
            if (jasperPrint != null) {
                System.out.println("✓ Report generation successful!");
                System.out.println("JasperPrint object created: " + jasperPrint.getClass().getName());
            } else {
                System.out.println("✗ Report generation failed - null result");
            }
            
        } catch (Exception e) {
            System.err.println("✗ Test failed with exception:");
            e.printStackTrace();
        }
        
        System.out.println("=== JasperReports Test Completed ===");
    }
    
    private static List<Customer> createTestData() {
        List<Customer> customers = new ArrayList<>();
        
        customers.add(new Customer(1, "123456", "John Doe", LocalDate.of(1990, 1, 15), true, 5000000));
        customers.add(new Customer(2, "789012", "Jane Smith", LocalDate.of(1985, 6, 20), true, 6000000));
        customers.add(new Customer(3, "345678", "Bob Johnson", LocalDate.of(1992, 3, 10), false, 4500000));
        
        return customers;
    }
}
