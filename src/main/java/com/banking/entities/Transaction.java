package com.banking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {

    private int id;
    private Timestamp createdAt;
    private String description;
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;

    @JsonIgnore
    private TransactionStatus transactionStatus;

    private String transactionType = "ONLINE";
    private TransactionStatus status;

    // NEW
    private String failureReason;

    public Transaction() {}

    public Transaction(String description, String senderAccount, String receiverAccount, BigDecimal amount, TransactionStatus status) {
        this.description = description;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.transactionStatus = status;
        this.amount = amount;
        this.status = status;
    }

    // getters/setters...
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getSenderAccount() { return senderAccount; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }
    public String getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public String getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
