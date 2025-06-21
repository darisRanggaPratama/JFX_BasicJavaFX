package com.tama.jfxcrud.controller;

import com.tama.jfxcrud.dao.CustomerDAO;
import com.tama.jfxcrud.model.Customer;
import com.tama.jfxcrud.util.CSVHandler;
import com.tama.jfxcrud.util.ExcelHandler;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class CustomerController {
    private final CustomerDAO customerDAO;
    private final CSVHandler csvHandler;
    private final ExcelHandler excelHandler;
    
    public CustomerController() {
        this.customerDAO = new CustomerDAO();
        this.csvHandler = new CSVHandler();
        this.excelHandler = new ExcelHandler();
    }
    
    public List<Customer> getCustomers(int limit, int offset, String searchTerm) throws SQLException {
        return customerDAO.getAllCustomers(limit, offset, searchTerm);
    }
    
    public int getTotalCustomers(String searchTerm) throws SQLException {
        return customerDAO.getTotalCustomers(searchTerm);
    }
    
    public void addCustomer(Customer customer) throws SQLException {
        customerDAO.addCustomer(customer);
    }
    
    public void updateCustomer(Customer customer) throws SQLException {
        customerDAO.updateCustomer(customer);
    }
    
    public void deleteCustomer(int idx) throws SQLException {
        customerDAO.deleteCustomer(idx);
    }
    
    public Map<String, Integer> importCSV(File file) {
        return csvHandler.importCSV(file);
    }
    
    public Map<String, Integer> exportCSV(File file, List<Customer> customers) {
        return csvHandler.exportCSV(file, customers);
    }
    
    public Map<String, Integer> importExcel(File file) {
        return excelHandler.importExcel(file);
    }
    
    public Map<String, Integer> exportExcel(File file, List<Customer> customers) {
        return excelHandler.exportExcel(file, customers);
    }
}