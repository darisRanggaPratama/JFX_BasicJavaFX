package com.tama.customer.dao;

import com.tama.customer.model.Customer;
import java.util.List;

public interface CustomerDAO {
    void insert(Customer customer);
    void update(Customer customer);
    void delete(String nik);
    Customer findByNik(String nik);
    List<Customer> findAll();
    List<Customer> getPaginatedResults(int offset, int limit, String searchTerm);
    int getSearchResultCount(String searchTerm);
    double getTotalSalaryFromSearch(String searchTerm);

    // Alias methods for better readability
    default void updateCustomer(Customer customer) {
        update(customer);
    }

    default void deleteCustomer(String nik) {
        delete(nik);
    }
}
