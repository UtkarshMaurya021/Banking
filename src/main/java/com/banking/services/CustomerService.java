package com.banking.services;

import com.banking.payloads.CustomerDTO;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO) throws Exception;
    List<CustomerDTO> getAllCustomers() throws Exception;
    Optional<CustomerDTO> getCustomerById(int id) throws Exception;
    Optional<CustomerDTO> updateCustomer(int id, CustomerDTO customerDTO) throws Exception;
    boolean deleteCustomer(int id) throws Exception;


    Optional<CustomerDTO> getCustomerByAadhar(String aadhar) throws Exception;
    Optional<CustomerDTO> updateCustomerByAadhar(String aadhar, CustomerDTO customerDTO) throws Exception;
    boolean deleteCustomerByAadhar(String aadhar) throws Exception;
}
