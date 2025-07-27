package com.tama.customer.service;

import com.tama.customer.dao.CustomerDAO;
import com.tama.customer.dao.CustomerDAOImpl;
import com.tama.customer.model.Customer;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class CustomerService {
    private final CustomerDAO customerDAO;

    public CustomerService() {
        this.customerDAO = new CustomerDAOImpl();
    }

    public void saveCustomer(Customer customer) throws SQLException {
        if (customer.getIdx() == 0) {
            customerDAO.insert(customer);
        } else {
            customerDAO.update(customer);
        }
    }

    public void updateCustomer(Customer customer) throws SQLException {
        customerDAO.update(customer);
    }

    public void deleteCustomer(int id) throws SQLException {
        customerDAO.delete((long) id);
    }

    public Customer getCustomerById(int id) throws SQLException {
        return customerDAO.findById((long) id);
    }

    public ObservableList<Customer> getAllCustomers() throws SQLException {
        return customerDAO.findAll();
    }

    public ObservableList<Customer> searchCustomers(String keyword) throws SQLException {
        return customerDAO.search(keyword);
    }

    public int getTotalCustomers() throws SQLException {
        ObservableList<Customer> customers = getAllCustomers();
        return customers.size();
    }

    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }
}
