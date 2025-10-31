package com.banking.service.impl;

import com.banking.config.DBConnection;
import com.banking.entities.Transaction;
import com.banking.entities.TransactionStatus;
import com.banking.payloads.TransactionDTO;
import com.banking.repositories.TransactionRepo;
import com.banking.repositories.TransactionRepoImpl;
import com.banking.exceptions.ResourceNotFoundException;
import com.banking.services.TransactionService;
import com.banking.utils.EmailService;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepo transactionRepository;

    public TransactionServiceImpl() {
        this.transactionRepository = new TransactionRepoImpl();
    }

    public TransactionServiceImpl(TransactionRepo repo) {
        this.transactionRepository = repo;
    }

    @Override
    public Transaction transfer(TransactionDTO dto) throws Exception {

        if (dto == null) throw new IllegalArgumentException("Transaction data required");

        // amount must be > 0
        if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            recordFailedTransactionBestEffort(dto, "Invalid amount");
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (dto.getSenderAccount() == null || dto.getReceiverAccount() == null) {
            recordFailedTransactionBestEffort(dto, "Sender or receiver account missing");
            throw new IllegalArgumentException("Sender and receiver required");
        }
        if (dto.getSenderAccount().equals(dto.getReceiverAccount())) {
            recordFailedTransactionBestEffort(dto, "Sender and receiver same");
            throw new IllegalArgumentException("Sender and receiver cannot be same");
        }

        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // minimal pin check: if senderPin provided, check it; if not provided we proceed.
            String storedPin = null;
            if (dto.getSenderPin() != null && !dto.getSenderPin().isEmpty()) {
                String pinCheckSql = """
                        SELECT c.customer_pin
                        FROM customers c
                        JOIN accounts a ON c.aadhar_number = a.aadhar_number
                        WHERE a.account_number = ?
                        """;
                try (PreparedStatement ps = conn.prepareStatement(pinCheckSql)) {
                    ps.setString(1, dto.getSenderAccount());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            storedPin = rs.getString("customer_pin");
                        }
                    }
                }
                if (storedPin == null) {
                    recordFailedTransaction(conn, dto, "Sender account not found or pin missing");
                    throw new ResourceNotFoundException("Account", "accountNumber", dto.getSenderAccount());
                }
                if (!storedPin.equals(dto.getSenderPin())) {
                    recordFailedTransaction(conn, dto, "Invalid PIN for the sender account");
                    throw new IllegalArgumentException("Invalid PIN for the sender account.");
                }
            }

            String selectSql = "SELECT account_number, balance FROM accounts WHERE account_number IN (?, ?) FOR UPDATE";
            BigDecimal senderBalance = null;
            BigDecimal receiverBalance = null;

            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setString(1, dto.getSenderAccount());
                ps.setString(2, dto.getReceiverAccount());
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String acc = rs.getString("account_number").trim();
                        BigDecimal bal = rs.getBigDecimal("balance");
                        if (acc.equalsIgnoreCase(dto.getSenderAccount().trim())) senderBalance = bal;
                        if (acc.equalsIgnoreCase(dto.getReceiverAccount().trim())) receiverBalance = bal;
                    }
                }
            }

            if (senderBalance == null) {
                recordFailedTransaction(conn, dto, "Sender account not found");
                throw new ResourceNotFoundException("Account", "accountNumber", dto.getSenderAccount());
            }
            if (receiverBalance == null) {
                recordFailedTransaction(conn, dto, "Receiver account not found");
                throw new ResourceNotFoundException("Account", "accountNumber", dto.getReceiverAccount());
            }
            if (senderBalance.compareTo(dto.getAmount()) < 0) {
                recordFailedTransaction(conn, dto, "Insufficient funds in sender account");
                throw new IllegalArgumentException("Insufficient funds");
            }

            BigDecimal newSenderBalance = senderBalance.subtract(dto.getAmount());
            BigDecimal newReceiverBalance = receiverBalance.add(dto.getAmount());

            String updateSql = "UPDATE accounts SET balance = ? WHERE account_number = ?";
            try (PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

                psUpdate.setBigDecimal(1, newSenderBalance);
                psUpdate.setString(2, dto.getSenderAccount());
                psUpdate.executeUpdate();

                psUpdate.setBigDecimal(1, newReceiverBalance);
                psUpdate.setString(2, dto.getReceiverAccount());
                psUpdate.executeUpdate();
            }

            Transaction completed = new Transaction(
                    dto.getDescription(),
                    dto.getSenderAccount(),
                    dto.getReceiverAccount(),
                    dto.getAmount(),
                    TransactionStatus.COMPLETED
            );
            Transaction saved = transactionRepository.save(conn, completed);

            conn.commit();

            // Attempt to send notification emails, swallow exceptions so they don't break transaction
            try {
                String senderEmail = null;
                String receiverEmail = null;

                String emailQuery = """
                    SELECT a.account_number, c.email
                    FROM accounts a
                    JOIN customers c ON a.aadhar_number = c.aadhar_number
                    WHERE a.account_number IN (?, ?)
                """;

                try (PreparedStatement ps = conn.prepareStatement(emailQuery)) {
                    ps.setString(1, dto.getSenderAccount());
                    ps.setString(2, dto.getReceiverAccount());
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String accNum = rs.getString("account_number").trim();
                            String email = rs.getString("email");
                            if (accNum.equalsIgnoreCase(dto.getSenderAccount().trim())) senderEmail = email;
                            if (accNum.equalsIgnoreCase(dto.getReceiverAccount().trim())) receiverEmail = email;
                        }
                    }
                }

                String subject = "Transaction Notification - MyBank";
                String senderBody = String.format(
                        "Dear Customer,\n\nYou have successfully transferred ₹%.2f to account %s.\n\nDescription: %s\n\nRegards,\nMyBank",
                        dto.getAmount(), dto.getReceiverAccount(), dto.getDescription()
                );
                String receiverBody = String.format(
                        "Dear Customer,\n\nYou have received ₹%.2f from account %s.\n\nDescription: %s\n\nRegards,\nMyBank",
                        dto.getAmount(), dto.getSenderAccount(), dto.getDescription()
                );

                // These are example credentials retained from your code; keep as-is per your request
                String smtpHost = "smtp.ethereal.email";
                String smtpPort = "587";
                String smtpUser = "osbaldo4@ethereal.email";
                String smtpPass = "GVcB1fAEtaYN8dwBUG";

                try {
                    if (senderEmail != null && !senderEmail.isEmpty()) {
                        EmailService.sendEmail("noreply@mybank.com", senderEmail, subject, senderBody, smtpHost, smtpPort, smtpUser, smtpPass);
                    }
                    Thread.sleep(500L);
                    if (receiverEmail != null && !receiverEmail.isEmpty()) {
                        EmailService.sendEmail("noreply@mybank.com", receiverEmail, subject, receiverBody, smtpHost, smtpPort, smtpUser, smtpPass);
                    }
                } catch (Exception e) {
                    // log and continue
                    e.printStackTrace();
                }

            } catch (Exception e) {
                // notify error but don't break
                e.printStackTrace();
            }

            return saved;

        } catch (Exception ex) {

            try {
                if (conn != null && !conn.isClosed()) {
                    recordFailedTransaction(conn, dto, ex.getMessage());
                    conn.commit();
                } else {
                    recordFailedTransactionBestEffort(dto, ex.getMessage());
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
            throw ex;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (Exception ignored) {}
            }
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccount(String accountNumber) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            return transactionRepository.getTransactionsByAccount(conn, accountNumber);
        }
    }

    private void recordFailedTransaction(Connection conn, TransactionDTO dto, String reason) {
        try {
            Transaction failed = new Transaction(
                    dto.getDescription(),
                    dto.getSenderAccount(),
                    dto.getReceiverAccount(),
                    dto.getAmount(),
                    TransactionStatus.FAILED
            );
            failed.setFailureReason(reason == null ? "Unknown error" : reason);
            transactionRepository.save(conn, failed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordFailedTransactionBestEffort(TransactionDTO dto, String reason) {
        try (Connection conn2 = DBConnection.getConnection()) {
            Transaction failed = new Transaction(
                    dto == null ? null : dto.getDescription(),
                    dto == null ? null : dto.getSenderAccount(),
                    dto == null ? null : dto.getReceiverAccount(),
                    dto == null ? null : dto.getAmount(),
                    TransactionStatus.FAILED
            );
            failed.setFailureReason(reason == null ? "Validation failed" : reason);
            transactionRepository.save(conn2, failed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
