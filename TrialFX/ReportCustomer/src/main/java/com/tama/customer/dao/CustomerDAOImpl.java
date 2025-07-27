package com.tama.customer.dao;

import com.tama.customer.config.DatabaseConfig;
import com.tama.customer.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private final DatabaseConfig dbConfig;

    public CustomerDAOImpl() {
        this.dbConfig = DatabaseConfig.getInstance();
    }

    @Override
    public void insert(Customer customer) throws SQLException {
        String sql = "INSERT INTO customer (nik, name, born, active, salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setCustomerParameters(stmt, customer);
            stmt.executeUpdate();
        }
    }

    @Override
    public void update(Customer customer) throws SQLException {
        String sql = "UPDATE customer SET nik=?, name=?, born=?, active=?, salary=? WHERE idx=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            setCustomerParameters(stmt, customer);
            stmt.setInt(6, customer.getIdx());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM customer WHERE idx=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Customer findById(Long id) throws SQLException {
        String sql = "SELECT * FROM customer WHERE idx=?";
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null;
    }

    @Override
    public ObservableList<Customer> findAll() throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer ORDER BY idx";

        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    @Override
    public ObservableList<Customer> search(String keyword) throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer WHERE nik LIKE ? OR name LIKE ? ORDER BY idx";

        try (Connection conn = dbConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    customers.add(mapResultSetToCustomer(rs));
                }
            }
        }
        return customers;
    }

    @Override
    public void saveAll(List<Customer> customers) throws SQLException {
        String sql = "INSERT INTO customer (nik, name, born, active, salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = dbConfig.getConnection()) {
            // Disable auto-commit for batch operations
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (Customer customer : customers) {
                    setCustomerParameters(stmt, customer);
                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private void setCustomerParameters(PreparedStatement stmt, Customer customer) throws SQLException {
        stmt.setString(1, customer.getNik());
        stmt.setString(2, customer.getName());
        stmt.setDate(3, Date.valueOf(customer.getBorn()));
        stmt.setBoolean(4, customer.isActive());
        stmt.setBigDecimal(5, customer.getSalary());
    }

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setIdx(rs.getInt("idx"));
        customer.setNik(rs.getString("nik"));
        customer.setName(rs.getString("name"));
        customer.setBorn(rs.getDate("born").toLocalDate());
        customer.setActive(rs.getBoolean("active"));
        customer.setSalary(rs.getBigDecimal("salary"));
        return customer;
    }
}
