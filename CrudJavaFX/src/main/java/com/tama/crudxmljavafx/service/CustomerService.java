package com.tama.crudxmljavafx.service;

import com.tama.crudxmljavafx.model.Customer;
import com.tama.crudxmljavafx.repository.CustomerRepository;
import com.tama.crudxmljavafx.util.FileUtil;

import java.util.List;

/**
 * Service class for Customer business logic
 */
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    public CustomerService() {
        this.customerRepository = new CustomerRepository();
    }
    
    /**
     * Get customers with pagination and search
     * @param page Current page (0-based)
     * @param pageSize Number of records per page
     * @param searchTerm Search term
     * @return List of customers
     */
    public List<Customer> getCustomers(int page, int pageSize, String searchTerm) {
        int offset = page * pageSize;
        return customerRepository.findAll(offset, pageSize, searchTerm);
    }
    
    /**
     * Get total count of customers
     * @param searchTerm Search term
     * @return Total count
     */
    public int getTotalCount(String searchTerm) {
        return customerRepository.getTotalCount(searchTerm);
    }
    
    /**
     * Get total pages
     * @param pageSize Number of records per page
     * @param searchTerm Search term
     * @return Total pages
     */
    public int getTotalPages(int pageSize, String searchTerm) {
        int totalCount = getTotalCount(searchTerm);
        return (int) Math.ceil((double) totalCount / pageSize);
    }
    
    /**
     * Find customer by ID
     * @param idx Customer ID
     * @return Customer or null if not found
     */
    public Customer findById(int idx) {
        return customerRepository.findById(idx);
    }
    
    /**
     * Save new customer
     * @param customer Customer to save
     * @return Generated ID or -1 if failed
     */
    public int saveCustomer(Customer customer) {
        // Validate customer data
        if (customer.getNik() == null || customer.getNik().trim().isEmpty()) {
            throw new IllegalArgumentException("NIK is required");
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        return customerRepository.save(customer);
    }
    
    /**
     * Update existing customer
     * @param customer Customer to update
     * @return true if successful
     */
    public boolean updateCustomer(Customer customer) {
        // Validate customer data
        if (customer.getIdx() == null) {
            throw new IllegalArgumentException("Customer ID is required for update");
        }
        if (customer.getNik() == null || customer.getNik().trim().isEmpty()) {
            throw new IllegalArgumentException("NIK is required");
        }
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        return customerRepository.update(customer);
    }
    
    /**
     * Delete customer by ID
     * @param idx Customer ID
     * @return true if successful
     */
    public boolean deleteCustomer(int idx) {
        return customerRepository.deleteById(idx);
    }
    
    /**
     * Export customers to CSV
     * @param filePath Output file path
     * @return true if successful
     */
    public boolean exportToCSV(String filePath) {
        List<Customer> customers = customerRepository.findAll();
        return FileUtil.exportToCSV(customers, filePath);
    }
    
    /**
     * Export customers to Excel
     * @param filePath Output file path
     * @return true if successful
     */
    public boolean exportToExcel(String filePath) {
        List<Customer> customers = customerRepository.findAll();
        return FileUtil.exportToExcel(customers, filePath);
    }
    
    /**
     * Import customers from CSV
     * @param filePath Input file path
     * @return Array with [successCount, failCount]
     */
    public int[] importFromCSV(String filePath) {
        List<Customer> customers = FileUtil.importFromCSV(filePath);
        return importCustomers(customers);
    }
    
    /**
     * Import customers from Excel
     * @param filePath Input file path
     * @return Array with [successCount, failCount]
     */
    public int[] importFromExcel(String filePath) {
        List<Customer> customers = FileUtil.importFromExcel(filePath);
        return importCustomers(customers);
    }
    
    /**
     * Import list of customers to database
     * @param customers List of customers to import
     * @return Array with [successCount, failCount]
     */
    private int[] importCustomers(List<Customer> customers) {
        int successCount = 0;
        int failCount = 0;
        
        for (Customer customer : customers) {
            try {
                int result = customerRepository.save(customer);
                if (result > 0) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                System.err.println("Failed to import customer: " + customer.getName() + " - " + e.getMessage());
            }
        }
        
        return new int[]{successCount, failCount};
    }
    
    /**
     * Get all customers for export
     * @return List of all customers
     */
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
