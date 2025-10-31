package com.banking.repositories;

import com.banking.config.DBConnection;
import com.banking.entities.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountRepoImpl implements AccountRepo{

    @Override
    public int save(Account account) throws SQLException {
        String sql = "INSERT INTO accounts (aadhar_number, balance, account_type, account_name, account_number, ifsc_code) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, account.getAadharNumber());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getAccountType());
            ps.setString(4, account.getAccountName());
            ps.setString(5, account.getAccountNumber());
            ps.setString(6, account.getIfsc());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    account.setAccountId(generatedId);
                    return fetchAndFill(account, conn);
                }
            }
        }
        throw new SQLException("Failed to insert account, no ID obtained.");
    }

    private int fetchAndFill(Account account, Connection conn) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, account.getAccountId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Account fullAccount = mapResultSetToAccount(rs);
                    account.setStatus(fullAccount.getStatus());
                    return account.getAccountId();
                }
            }
        }
        return account.getAccountId();
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT account_id, aadhar_number, account_name, account_number, balance, account_type, ifsc_code, status " +
                "FROM accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findById(Integer id) throws SQLException {
        String sql = "SELECT account_id, aadhar_number, account_name, account_number, balance, account_type, ifsc_code, status FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Account> findAll() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, aadhar_number, account_name, account_number, balance, account_type, ifsc_code, status FROM accounts";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                accounts.add(mapResultSetToAccount(rs));
            }
        }
        return accounts;
    }

    @Override
    public boolean update(Account account) throws SQLException {
        String sql = "UPDATE accounts SET aadhar_number=?, balance=?, account_type=?, account_name=?, account_number=?, ifsc_code=?, status=? " +
                "WHERE account_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, account.getAadharNumber());
            ps.setDouble(2, account.getBalance());
            ps.setString(3, account.getAccountType());
            ps.setString(4, account.getAccountName());
            ps.setString(5, account.getAccountNumber());
            ps.setString(6, account.getIfsc());
            ps.setString(7, account.getStatus());
            ps.setInt(8, account.getAccountId());

            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    @Override
    public boolean deleteByAccountNumber(String accountNumber) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public List<Account> findByAadharNumber(String aadharNumber) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, aadhar_number, account_name, account_number, balance, account_type, ifsc_code, status FROM accounts WHERE aadhar_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aadharNumber);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    accounts.add(mapResultSetToAccount(rs));
                }
            }
        }
        return accounts;
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setAadharNumber(rs.getString("aadhar_number"));
        account.setBalance(rs.getDouble("balance"));
        account.setAccountType(rs.getString("account_type"));
        account.setAccountName(rs.getString("account_name"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setIfsc(rs.getString("ifsc_code"));
        account.setStatus(rs.getString("status"));
        return account;
    }
}
