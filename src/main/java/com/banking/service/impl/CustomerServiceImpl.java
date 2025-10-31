package com.banking.service.impl;

import com.banking.entities.Customer;
import com.banking.payloads.CustomerDTO;
import com.banking.repositories.CustomerRepo;
import com.banking.repositories.CustomerRepoImpl;
import com.banking.services.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo = new CustomerRepoImpl();

    // NOTE: removed strict validation. Frontend must enforce required fields.
    private Customer dtoToEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setEmail(customerDTO.getEmail());
        customer.setAddress(customerDTO.getAddress());
        customer.setCustomerPin(customerDTO.getCustomerPin());
        customer.setAadharNumber(customerDTO.getAadharNumber());
        customer.setDob(customerDTO.getDob());
        customer.setStatus(customerDTO.getStatus() != null ? customerDTO.getStatus() : "ACTIVE");
        customer.setRole(customerDTO.getRole() != null ? customerDTO.getRole() : "CUSTOMER");
        return customer;
    }

    private CustomerDTO entityToDto(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setCustomerPin(customer.getCustomerPin());
        customerDTO.setAadharNumber(customer.getAadharNumber());
        customerDTO.setDob(customer.getDob());
        customerDTO.setStatus(customer.getStatus());
        customerDTO.setRole(customer.getRole());
        return customerDTO;
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDTO) throws Exception {
        Customer entity = dtoToEntity(customerDTO);
        int generatedId = customerRepo.save(entity);
        entity.setId(generatedId);
        return entityToDto(entity);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() throws Exception {
        List<Customer> allCustomers = customerRepo.findAll();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer customer : allCustomers){
            customerDTOS.add(entityToDto(customer));
        }
        return customerDTOS;
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(int id) throws Exception {
        Optional<Customer> customerOpt = customerRepo.findById(id);
        return customerOpt.map(this::entityToDto);
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(int id, CustomerDTO customerDTO) throws Exception {
        Optional<Customer> existingOpt = customerRepo.findById(id);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Customer toUpdate = existingOpt.get();

        if (customerDTO.getName() != null) {
            toUpdate.setName(customerDTO.getName());
        }

        if (customerDTO.getPhoneNumber() != null) {
            toUpdate.setPhoneNumber(customerDTO.getPhoneNumber());
        }

        if (customerDTO.getEmail() != null) {
            toUpdate.setEmail(customerDTO.getEmail());
        }

        if (customerDTO.getAddress() != null) {
            toUpdate.setAddress(customerDTO.getAddress());
        }

        if (customerDTO.getCustomerPin() != null) {
            toUpdate.setCustomerPin(customerDTO.getCustomerPin());
        }

        if (customerDTO.getAadharNumber() != null) {
            toUpdate.setAadharNumber(customerDTO.getAadharNumber());
        }

        if (customerDTO.getDob() != null) {
            toUpdate.setDob(customerDTO.getDob());
        }

        if (customerDTO.getStatus() != null) {
            toUpdate.setStatus(customerDTO.getStatus());
        }

        if (customerDTO.getRole() != null) {
            toUpdate.setRole(customerDTO.getRole().toUpperCase());
        }

        boolean updated = customerRepo.update(toUpdate);
        if (!updated) {
            return Optional.empty();
        }

        return Optional.of(entityToDto(toUpdate));
    }

    @Override
    public boolean deleteCustomer(int id) throws Exception {
        return customerRepo.delete(id);
    }

    @Override
    public Optional<CustomerDTO> getCustomerByAadhar(String aadhar) throws Exception {
        Optional<Customer> c = customerRepo.findByAadhar(aadhar);
        return c.map(this::entityToDto);
    }

    @Override
    public Optional<CustomerDTO> updateCustomerByAadhar(String aadhar, CustomerDTO customerDTO) throws Exception {
        Optional<Customer> existingOpt = customerRepo.findByAadhar(aadhar);
        if (existingOpt.isEmpty()) {
            return Optional.empty();
        }

        Customer toUpdate = existingOpt.get();

        if (customerDTO.getName() != null) {
            toUpdate.setName(customerDTO.getName());
        }

        if (customerDTO.getPhoneNumber() != null) {
            toUpdate.setPhoneNumber(customerDTO.getPhoneNumber());
        }

        if (customerDTO.getEmail() != null) {
            toUpdate.setEmail(customerDTO.getEmail());
        }

        if (customerDTO.getAddress() != null) {
            toUpdate.setAddress(customerDTO.getAddress());
        }

        if (customerDTO.getCustomerPin() != null) {
            toUpdate.setCustomerPin(customerDTO.getCustomerPin());
        }

        // we keep the current approach: frontend decides whether to allow changing aadhar
        if (customerDTO.getDob() != null) {
            toUpdate.setDob(customerDTO.getDob());
        }

        if (customerDTO.getStatus() != null) {
            toUpdate.setStatus(customerDTO.getStatus());
        }

        if (customerDTO.getRole() != null) {
            toUpdate.setRole(customerDTO.getRole().toUpperCase());
        }

        boolean updated = customerRepo.update(toUpdate);
        if (!updated) {
            return Optional.empty();
        }

        return Optional.of(entityToDto(toUpdate));
    }

    @Override
    public boolean deleteCustomerByAadhar(String aadhar) throws Exception {
        return customerRepo.deleteByAadhar(aadhar);
    }
}
