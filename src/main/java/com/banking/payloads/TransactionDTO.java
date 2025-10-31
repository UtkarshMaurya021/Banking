package com.banking.payloads;

import java.math.BigDecimal;

public class TransactionDTO {

    private String description;
    private String senderAccount;
    private String receiverAccount;
    private BigDecimal amount;
    private String senderPin;

    public TransactionDTO() {}

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSenderAccount() { return senderAccount; }
    public void setSenderAccount(String senderAccount) { this.senderAccount = senderAccount; }

    public String getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(String receiverAccount) { this.receiverAccount = receiverAccount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getSenderPin() { return senderPin; }
    public void setSenderPin(String senderPin) { this.senderPin = senderPin; }
}
