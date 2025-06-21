package com.tama.jfxcrud.dao;

import com.tama.jfxcrud.model.Customer;
import com.tama.jfxcrud.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    
    public List<Customer> getAllCustomers(int limit, int offset, String searchTerm) throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customer WHERE 1=1";

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query += " AND (nik LIKE ? OR name LIKE ? OR born LIKE ? OR CAST(active AS CHAR) LIKE ? OR CAST(salary AS CHAR) LIKE ?)";
        }

        query += " LIMIT ? OFFSET ?";

        System.out.println("DEBUG: Executing query: " + query);
        System.out.println("DEBUG: Parameters - limit: " + limit + ", offset: " + offset + ", searchTerm: '" + searchTerm + "'");

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }

            stmt.setInt(paramIndex++, limit);
            stmt.setInt(paramIndex, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setIdx(rs.getInt("idx"));
                    customer.setNik(rs.getString("nik"));
                    customer.setName(rs.getString("name"));

                    Date bornDate = rs.getDate("born");
                    if (bornDate != null) {
                        customer.setBorn(bornDate.toLocalDate());
                    }

                    customer.setActive(rs.getBoolean("active"));
                    customer.setSalary(rs.getInt("salary"));

                    customers.add(customer);
                    System.out.println("DEBUG: Found customer: " + customer);
                }
            }
        }

        System.out.println("DEBUG: Total customers found: " + customers.size());
        return customers;
    }
    
    public int getTotalCustomers(String searchTerm) throws SQLException {
        String query = "SELECT COUNT(*) FROM customer WHERE 1=1";

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query += " AND (nik LIKE ? OR name LIKE ? OR born LIKE ? OR CAST(active AS CHAR) LIKE ? OR CAST(salary AS CHAR) LIKE ?)";
        }

        System.out.println("DEBUG: Count query: " + query);

        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {

            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                stmt.setString(4, searchPattern);
                stmt.setString(5, searchPattern);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    System.out.println("DEBUG: Total customer count: " + count);
                    return count;
                }
            }
        }

        System.out.println("DEBUG: No count result, returning 0");
        return 0;
    }
    
    // Implementasi CRUD lainnya
    public void addCustomer(Customer customer) throws SQLException {
        String query = "INSERT INTO customer (nik, name, born, active, salary) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, customer.getNik());
            stmt.setString(2, customer.getName());
            stmt.setDate(3, customer.getBorn() != null ? Date.valueOf(customer.getBorn()) : null);
            stmt.setBoolean(4, customer.isActive());
            stmt.setInt(5, customer.getSalary());
            
            stmt.executeUpdate();
        }
    }
    
    public void updateCustomer(Customer customer) throws SQLException {
        String query = "UPDATE customer SET nik = ?, name = ?, born = ?, active = ?, salary = ? WHERE idx = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, customer.getNik());
            stmt.setString(2, customer.getName());
            stmt.setDate(3, customer.getBorn() != null ? Date.valueOf(customer.getBorn()) : null);
            stmt.setBoolean(4, customer.isActive());
            stmt.setInt(5, customer.getSalary());
            stmt.setInt(6, customer.getIdx());
            
            stmt.executeUpdate();
        }
    }
    
    public void deleteCustomer(int idx) throws SQLException {
        String query = "DELETE FROM customer WHERE idx = ?";
        
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idx);
            stmt.executeUpdate();
        }
    }
}