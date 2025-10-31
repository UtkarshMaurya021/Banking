package com.banking.service.impl;

import com.banking.entities.Account;
import com.banking.payloads.AccountDTO;
import com.banking.repositories.AccountRepo;
import com.banking.repositories.AccountRepoImpl;
import com.banking.services.AccountService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo = new AccountRepoImpl();

    // NOTE: removed server-side form validation. Frontend must validate inputs.
    private Account dtoToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAadharNumber(accountDTO.getAadharNumber());
        // if balance is null, default to 0.0
        account.setBalance(accountDTO.getBalance() != null ? accountDTO.getBalance() : 0.0);
        account.setAccountType(accountDTO.getAccountType() != null ? accountDTO.getAccountType().toUpperCase() : "SAVINGS");
        account.setAccountName(accountDTO.getAccountName());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setIfsc(accountDTO.getIfsc());
        account.setStatus(accountDTO.getStatus() != null ? accountDTO.getStatus() : "ACTIVE");
        return account;
    }

    @Override
    public Optional<AccountDTO> getAccountByNumber(String accountNumber) throws SQLException {
        Optional<Account> opt = accountRepo.findByAccountNumber(accountNumber);
        return opt.map(this::entityToDto);
    }

    @Override
    public Optional<AccountDTO> updateAccountByNumber(String accountNumber, AccountDTO accountDTO) throws SQLException {
        Optional<Account> existingOpt = accountRepo.findByAccountNumber(accountNumber);
        if (existingOpt.isEmpty()) return Optional.empty();

        Account toUpdate = existingOpt.get();

        if (accountDTO.getAadharNumber() != null) {
            toUpdate.setAadharNumber(accountDTO.getAadharNumber());
        }

        if (accountDTO.getBalance() != null) {
            toUpdate.setBalance(accountDTO.getBalance());
        }

        if (accountDTO.getAccountType() != null) {
            toUpdate.setAccountType(accountDTO.getAccountType().toUpperCase());
        }

        if (accountDTO.getAccountName() != null) {
            toUpdate.setAccountName(accountDTO.getAccountName());
        }

        if (accountDTO.getAccountNumber() != null) {
            toUpdate.setAccountNumber(accountDTO.getAccountNumber());
        }

        if (accountDTO.getIfsc() != null) {
            toUpdate.setIfsc(accountDTO.getIfsc());
        }

        if (accountDTO.getStatus() != null) {
            toUpdate.setStatus(accountDTO.getStatus());
        }

        boolean updated = accountRepo.update(toUpdate);
        if (!updated) return Optional.empty();

        return Optional.of(entityToDto(toUpdate));
    }

    private AccountDTO entityToDto(Account account){
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        dto.setAadharNumber(account.getAadharNumber());
        dto.setBalance(account.getBalance());
        dto.setAccountType(account.getAccountType());
        dto.setAccountName(account.getAccountName());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setIfsc(account.getIfsc());
        dto.setStatus(account.getStatus());
        return dto;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) throws SQLException {
        Account entity = dtoToEntity(accountDTO);
        int generatedId = accountRepo.save(entity);
        entity.setAccountId(generatedId);
        return entityToDto(entity);
    }

    @Override
    public List<AccountDTO> getAllAccounts() throws SQLException {
        List<Account> accounts = accountRepo.findAll();
        List<AccountDTO> dtos = new ArrayList<>();
        for(Account acc : accounts){
            dtos.add(entityToDto(acc));
        }
        return dtos;
    }

    @Override
    public Optional<AccountDTO> getAccountById(Integer accountId) throws SQLException {
        Optional<Account> accountOpt = accountRepo.findById(accountId);
        return accountOpt.map(this::entityToDto);
    }

    @Override
    public Optional<AccountDTO> updateAccount(Integer accountId, AccountDTO accountDTO) throws SQLException {
        Optional<Account> existingOpt = accountRepo.findById(accountId);
        if(existingOpt.isEmpty()){
            return Optional.empty();
        }

        Account toUpdate = existingOpt.get();

        if(accountDTO.getAadharNumber() != null) {
            toUpdate.setAadharNumber(accountDTO.getAadharNumber());
        }

        if(accountDTO.getBalance() != null){
            toUpdate.setBalance(accountDTO.getBalance());
        }

        if(accountDTO.getAccountType() != null) {
            toUpdate.setAccountType(accountDTO.getAccountType().toUpperCase());
        }

        if(accountDTO.getAccountName() != null){
            toUpdate.setAccountName(accountDTO.getAccountName());
        }

        if(accountDTO.getAccountNumber() != null){
            toUpdate.setAccountNumber(accountDTO.getAccountNumber());
        }

        if(accountDTO.getIfsc() != null) {
            toUpdate.setIfsc(accountDTO.getIfsc());
        }

        if(accountDTO.getStatus() != null) {
            toUpdate.setStatus(accountDTO.getStatus());
        }

        boolean updated = accountRepo.update(toUpdate);
        if(!updated){
            return Optional.empty();
        }

        return Optional.of(entityToDto(toUpdate));
    }

    @Override
    public boolean deleteAccountByNumber(String accountNumber) throws SQLException {
        return accountRepo.deleteByAccountNumber(accountNumber);
    }

    @Override
    public List<AccountDTO> getAccountsByAadhar(String aadharNumber) throws SQLException {
        List<Account> accounts = accountRepo.findByAadharNumber(aadharNumber);
        List<AccountDTO> dtos = new ArrayList<>();
        for (Account acc : accounts) {
            dtos.add(entityToDto(acc));
        }
        return dtos;
    }
}
