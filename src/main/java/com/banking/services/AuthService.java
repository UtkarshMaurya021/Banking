package com.banking.services;

import com.banking.payloads.CustomerDTO;

public interface AuthService {
    CustomerDTO signup(CustomerDTO dto) throws Exception;
    String login(String username, String password) throws Exception; // returns JWT
}
