package com.banking.repositories;

import com.banking.entities.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface TransactionRepo {
    Transaction save(Connection conn, Transaction transaction) throws SQLException;
    List<Transaction> getTransactionsByAccount(Connection conn, String accountNumber) throws SQLException;
}
