package com.banking.repositories;

import com.banking.entities.Account;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AccountRepo {
    int save(Account account) throws SQLException;
    Optional<Account> findById(Integer id) throws SQLException;
    List<Account> findAll() throws SQLException;
    boolean update(Account account) throws SQLException;
    boolean deleteByAccountNumber(String accountNumber) throws SQLException;
    List<Account> findByAadharNumber(String aadharNumber) throws SQLException;
    Optional<Account> findByAccountNumber(String accountNumber) throws SQLException;
}
