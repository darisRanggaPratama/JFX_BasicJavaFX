package com.tama.customer.dao;

import com.tama.customer.model.Customer;
import com.tama.customer.util.DBConnectionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    private static final String INSERT_QUERY = "INSERT INTO customer (nik, name, born, active, salary) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE customer SET name = ?, born = ?, active = ?, salary = ? WHERE nik = ?";
    private static final String DELETE_QUERY = "DELETE FROM customer WHERE nik = ?";
    private static final String FIND_BY_NIK_QUERY = "SELECT * FROM customer WHERE nik = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM customer";
    private static final String SEARCH_QUERY = "SELECT * FROM customer WHERE nik LIKE ? OR name LIKE ? OR DATE_FORMAT(born, '%Y-%m-%d') LIKE ? OR CAST(salary AS CHAR) LIKE ?";
    private static final String COUNT_SEARCH_QUERY = "SELECT COUNT(*) FROM customer WHERE nik LIKE ? OR name LIKE ? OR DATE_FORMAT(born, '%Y-%m-%d') LIKE ? OR CAST(salary AS CHAR) LIKE ?";
    private static final String SUM_SALARY_SEARCH_QUERY = "SELECT SUM(salary) FROM customer WHERE nik LIKE ? OR name LIKE ? OR DATE_FORMAT(born, '%Y-%m-%d') LIKE ? OR CAST(salary AS CHAR) LIKE ?";
    private static final String PAGINATED_SEARCH_QUERY = "SELECT * FROM customer WHERE nik LIKE ? OR name LIKE ? OR DATE_FORMAT(born, '%Y-%m-%d') LIKE ? OR CAST(salary AS CHAR) LIKE ? LIMIT ? OFFSET ?";

    @Override
    public void insert(Customer customer) {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_QUERY)) {
            setCustomerParameters(stmt, customer);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting customer", e);
        }
    }

    @Override
    public void update(Customer customer) {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_QUERY)) {
            stmt.setString(1, customer.getName());
            stmt.setDate(2, Date.valueOf(customer.getBorn()));
            stmt.setBoolean(3, customer.isActive());
            stmt.setDouble(4, customer.getSalary());
            stmt.setString(5, customer.getNik());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating customer", e);
        }
    }

    @Override
    public void delete(String nik) {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_QUERY)) {
            stmt.setString(1, nik);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer", e);
        }
    }

    @Override
    public Customer findByNik(String nik) {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_BY_NIK_QUERY)) {
            stmt.setString(1, nik);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return createCustomerFromResultSet(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer by NIK", e);
        }
        return null;
    }

    @Override
    public List<Customer> findAll() {
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(FIND_ALL_QUERY)) {
            while (rs.next()) {
                customers.add(createCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding all customers", e);
        }
        return customers;
    }

    @Override
    public List<Customer> getPaginatedResults(int offset, int limit, String searchTerm) {
        List<Customer> results = new ArrayList<>();
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(PAGINATED_SEARCH_QUERY)) {
            String searchPattern = "%" + searchTerm + "%";
            setSearchParameters(stmt, searchPattern);
            stmt.setInt(5, limit);
            stmt.setInt(6, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(createCustomerFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching customers", e);
        }
        return results;
    }

    @Override
    public int getSearchResultCount(String searchTerm) {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_SEARCH_QUERY)) {
            String searchPattern = "%" + searchTerm + "%";
            setSearchParameters(stmt, searchPattern);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error counting search results", e);
        }
    }

    @Override
    public double getTotalSalaryFromSearch(String searchTerm) {
        try (Connection conn = DBConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SUM_SALARY_SEARCH_QUERY)) {
            String searchPattern = "%" + searchTerm + "%";
            setSearchParameters(stmt, searchPattern);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
            return 0.0;
        } catch (SQLException e) {
            throw new RuntimeException("Error calculating total salary", e);
        }
    }

    private void setSearchParameters(PreparedStatement stmt, String searchPattern) throws SQLException {
        stmt.setString(1, searchPattern); // NIK
        stmt.setString(2, searchPattern); // Name
        stmt.setString(3, searchPattern); // Born date
        stmt.setString(4, searchPattern); // Salary
    }

    private void setCustomerParameters(PreparedStatement stmt, Customer customer) throws SQLException {
        stmt.setString(1, customer.getNik());
        stmt.setString(2, customer.getName());
        stmt.setDate(3, Date.valueOf(customer.getBorn()));
        stmt.setBoolean(4, customer.isActive());
        stmt.setDouble(5, customer.getSalary());
    }

    private Customer createCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setNik(rs.getString("nik"));
        customer.setName(rs.getString("name"));
        customer.setBorn(rs.getDate("born").toLocalDate());
        customer.setActive(rs.getBoolean("active"));
        customer.setSalary(rs.getDouble("salary"));
        return customer;
    }
}
