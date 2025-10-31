package com.banking.services;

import com.banking.entities.Transaction;
import com.banking.payloads.TransactionDTO;
import java.util.List;

public interface TransactionService {
    Transaction transfer(TransactionDTO dto) throws Exception;
    List<Transaction> getTransactionsByAccount(String accountNumber) throws Exception;
}
