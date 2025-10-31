package com.banking.repositories;

import com.banking.entities.Customer;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CustomerRepo {
    int save(Customer customer) throws SQLException;
    List<Customer> findAll() throws SQLException;
    Optional<Customer> findById(int id) throws SQLException;
    boolean update(Customer customer) throws SQLException;
    boolean delete(int id) throws SQLException;


    Optional<Customer> findByAadhar(String aadhar) throws SQLException;
    boolean deleteByAadhar(String aadhar) throws SQLException;

    Optional<Customer> findByUsername(String username) throws SQLException;
}
