package com.tama.customer.dao;

import com.tama.customer.model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO {
    void insert(Customer customer) throws SQLException;
    void update(Customer customer) throws SQLException;
    void delete(Long id) throws SQLException;
    Customer findById(Long id) throws SQLException;
    ObservableList<Customer> findAll() throws SQLException;
    ObservableList<Customer> search(String keyword) throws SQLException;
    void saveAll(List<Customer> customers) throws SQLException;
}
