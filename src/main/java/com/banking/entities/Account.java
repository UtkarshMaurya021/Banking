package com.banking.entities;

public class Account {

    private Integer accountId;
    private String aadharNumber;
    private String createdAt;
    private String modifiedAt;
    private Double balance;
    private String accountType;
    private String accountName;
    private String accountNumber;
    private String ifsc;
    private String status;

    public Account() {}

    public Account(Integer accountId, String aadharNumber, String createdAt, String modifiedAt,
                   Double balance, String accountType, String accountName,
                   String accountNumber, String ifsc, String status) {
        this.accountId = accountId;
        this.aadharNumber = aadharNumber;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.balance = balance;
        this.accountType = accountType;
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.ifsc = ifsc;
        this.status = status;
    }


    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }
    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(String modifiedAt) { this.modifiedAt = modifiedAt; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getIfsc() { return ifsc; }
    public void setIfsc(String ifsc) { this.ifsc = ifsc; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
