package com.banking.service.impl;

import com.banking.entities.Customer;
import com.banking.payloads.CustomerDTO;
import com.banking.repositories.CustomerRepo;
import com.banking.repositories.CustomerRepoImpl;
import com.banking.services.AuthService;
import com.banking.auth.AuthUtil;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {

    private final CustomerRepo customerRepo = new CustomerRepoImpl();

    @Override
    public CustomerDTO signup(CustomerDTO dto) throws Exception {
        // Minimal checks: username/password must be present — other form validations are done on frontend
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username required");
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password required");
        }

        // If username or aadhar already exist, repo.save will fail due to unique constraints; we still attempt to check username
        Optional<Customer> byUsername = customerRepo.findByUsername(dto.getUsername());
        if (byUsername.isPresent()) throw new IllegalArgumentException("Username already exists");

        Optional<Customer> byAadhar = customerRepo.findByAadhar(dto.getAadharNumber());
        if (byAadhar.isPresent()) throw new IllegalArgumentException("Aadhar already registered");

        Customer c = new Customer();
        c.setName(dto.getName());
        c.setPhoneNumber(dto.getPhoneNumber());
        c.setEmail(dto.getEmail());
        c.setAddress(dto.getAddress());
        c.setCustomerPin(dto.getCustomerPin());
        c.setAadharNumber(dto.getAadharNumber());
        c.setDob(dto.getDob());
        c.setStatus("ACTIVE");
        c.setRole(dto.getRole() != null ? dto.getRole() : "CUSTOMER");

        String hashed = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        c.setUsername(dto.getUsername());
        c.setPasswordHash(hashed);

        int id = customerRepo.save(c);
        dto.setId(id);
        dto.setPassword(null);
        dto.setRole(c.getRole());

        return dto;
    }

    @Override
    public String login(String username, String password) throws Exception {
        if (username == null || password == null)
            throw new IllegalArgumentException("Username and password required");

        Optional<Customer> opt = customerRepo.findByUsername(username);
        if (opt.isEmpty()) throw new IllegalArgumentException("Invalid username or password");

        Customer c = opt.get();
        String storedHash = c.getPasswordHash();

        if (storedHash == null || !BCrypt.checkpw(password, storedHash)) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = AuthUtil.generateToken(
                c.getUsername(),
                c.getAadharNumber(),
                c.getRole() == null ? "CUSTOMER" : c.getRole()
        );

        return token;
    }
}
