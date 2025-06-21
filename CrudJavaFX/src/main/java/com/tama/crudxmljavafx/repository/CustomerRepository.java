package com.tama.crudxmljavafx.repository;

import com.tama.crudxmljavafx.model.Customer;
import com.tama.crudxmljavafx.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for Customer data access operations
 */
public class CustomerRepository {

    /**
     * Get all customers with pagination and search
     * @param offset Starting record number
     * @param limit Number of records to fetch
     * @param searchTerm Search term for filtering
     * @return List of customers
     */
    public List<Customer> findAll(int offset, int limit, String searchTerm) {
        List<Customer> customers = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM customer WHERE 1=1");
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (nik LIKE ? OR name LIKE ? OR born LIKE ? OR active LIKE ? OR salary LIKE ?)");
        }
        
        sql.append(" ORDER BY idx LIMIT ? OFFSET ?");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            
            stmt.setInt(paramIndex++, limit);
            stmt.setInt(paramIndex, offset);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return customers;
    }

    /**
     * Get total count of customers with search filter
     * @param searchTerm Search term for filtering
     * @return Total count
     */
    public int getTotalCount(String searchTerm) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM customer WHERE 1=1");
        
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (nik LIKE ? OR name LIKE ? OR born LIKE ? OR active LIKE ? OR salary LIKE ?)");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                String searchPattern = "%" + searchTerm + "%";
                stmt.setString(1, searchPattern);
                stmt.setString(2, searchPattern);
                stmt.setString(3, searchPattern);
                stmt.setString(4, searchPattern);
                stmt.setString(5, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }

    /**
     * Find customer by ID
     * @param idx Customer ID
     * @return Customer object or null if not found
     */
    public Customer findById(int idx) {
        String sql = "SELECT * FROM customer WHERE idx = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idx);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToCustomer(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Save new customer
     * @param customer Customer to save
     * @return Generated ID or -1 if failed
     */
    public int save(Customer customer) {
        String sql = "INSERT INTO customer (nik, name, born, active, salary) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, customer.getNik());
            stmt.setString(2, customer.getName());
            stmt.setDate(3, customer.getBorn() != null ? Date.valueOf(customer.getBorn()) : null);
            stmt.setBoolean(4, customer.getActive() != null ? customer.getActive() : false);
            stmt.setInt(5, customer.getSalary() != null ? customer.getSalary() : 0);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }

    /**
     * Update existing customer
     * @param customer Customer to update
     * @return true if successful, false otherwise
     */
    public boolean update(Customer customer) {
        String sql = "UPDATE customer SET nik = ?, name = ?, born = ?, active = ?, salary = ? WHERE idx = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, customer.getNik());
            stmt.setString(2, customer.getName());
            stmt.setDate(3, customer.getBorn() != null ? Date.valueOf(customer.getBorn()) : null);
            stmt.setBoolean(4, customer.getActive() != null ? customer.getActive() : false);
            stmt.setInt(5, customer.getSalary() != null ? customer.getSalary() : 0);
            stmt.setInt(6, customer.getIdx());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Delete customer by ID
     * @param idx Customer ID
     * @return true if successful, false otherwise
     */
    public boolean deleteById(int idx) {
        String sql = "DELETE FROM customer WHERE idx = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idx);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Get all customers (for export)
     * @return List of all customers
     */
    public List<Customer> findAll() {
        return findAll(0, Integer.MAX_VALUE, null);
    }

    /**
     * Map ResultSet to Customer object
     * @param rs ResultSet
     * @return Customer object
     * @throws SQLException if mapping fails
     */
    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setIdx(rs.getInt("idx"));
        customer.setNik(rs.getString("nik"));
        customer.setName(rs.getString("name"));
        
        Date bornDate = rs.getDate("born");
        customer.setBorn(bornDate != null ? bornDate.toLocalDate() : null);
        
        customer.setActive(rs.getBoolean("active"));
        customer.setSalary(rs.getInt("salary"));
        
        return customer;
    }
}
