package com.banking.repositories;

import com.banking.entities.Transaction;
import com.banking.entities.TransactionStatus;
import com.banking.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepoImpl implements TransactionRepo {

    @Override
    public Transaction save(Connection conn, Transaction transaction) throws SQLException {

        boolean externalConn = (conn != null);
        Connection localConn = conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!externalConn) {
                localConn = DBConnection.getConnection();
            }
            String sql = "INSERT INTO transactions (description, sender_account, receiver_account, amount, transaction_type, status, failure_reason) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            ps = localConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, transaction.getDescription());
            ps.setString(2, transaction.getSenderAccount());
            ps.setString(3, transaction.getReceiverAccount());
            ps.setBigDecimal(4, transaction.getAmount());
            ps.setString(5, transaction.getTransactionType());
            ps.setString(6, transaction.getStatus() != null ? transaction.getStatus().name() : TransactionStatus.PENDING.name());
            ps.setString(7, transaction.getFailureReason());

            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                transaction.setId(rs.getInt(1));
            }
            return transaction;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (!externalConn && localConn != null) try { localConn.close(); } catch (Exception ignored) {}
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccount(Connection conn, String accountNumber) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT id, created_at, description, sender_account, receiver_account, amount, transaction_type, status, failure_reason FROM transactions WHERE sender_account = ? OR receiver_account = ? ORDER BY created_at DESC";
        boolean externalConn = (conn != null);
        Connection localConn = conn;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            if (!externalConn) {
                localConn = DBConnection.getConnection();
            }
            ps = localConn.prepareStatement(sql);
            ps.setString(1, accountNumber);
            ps.setString(2, accountNumber);
            rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setId(rs.getInt("id"));
                t.setCreatedAt(rs.getTimestamp("created_at"));
                t.setDescription(rs.getString("description"));
                t.setSenderAccount(rs.getString("sender_account"));
                t.setReceiverAccount(rs.getString("receiver_account"));
                t.setAmount(rs.getBigDecimal("amount"));
                String status = rs.getString("status");
                t.setStatus(status == null ? TransactionStatus.PENDING : TransactionStatus.valueOf(status));
                t.setFailureReason(rs.getString("failure_reason"));
                list.add(t);
            }
            return list;
        } finally {
            if (rs != null) try { rs.close(); } catch (Exception ignored) {}
            if (ps != null) try { ps.close(); } catch (Exception ignored) {}
            if (!externalConn && localConn != null) try { localConn.close(); } catch (Exception ignored) {}
        }
    }
}
