package com.banking.services;

import com.banking.payloads.AccountDTO;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountDTO createAccount(AccountDTO accountDTO) throws SQLException;
    List<AccountDTO> getAllAccounts() throws SQLException;
    Optional<AccountDTO> getAccountById(Integer accountId) throws SQLException;
    Optional<AccountDTO> updateAccount(Integer accountId, AccountDTO accountDTO) throws SQLException;
    boolean deleteAccountByNumber(String accountNumber) throws SQLException;
    List<AccountDTO> getAccountsByAadhar(String aadharNumber) throws SQLException;
    Optional<AccountDTO> getAccountByNumber(String accountNumber) throws SQLException;
    Optional<AccountDTO> updateAccountByNumber(String accountNumber, AccountDTO accountDTO) throws SQLException;
}
