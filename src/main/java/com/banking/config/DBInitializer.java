package com.banking.config;

import java.sql.Connection;
import java.sql.Statement;

public class DBInitializer {

    public static void initialize() {

        String customersTable = "CREATE TABLE IF NOT EXISTS customers ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL, "
                + "phone_number VARCHAR(15) NOT NULL UNIQUE, "
                + "email VARCHAR(100) UNIQUE, "
                + "address VARCHAR(255), "
                + "customer_pin VARCHAR(10) NOT NULL, "
                + "aadhar_number VARCHAR(20) UNIQUE, "
                + "username VARCHAR(100) UNIQUE, "
                + "password_hash VARCHAR(255), "
                + "dob DATE, "
                + "role ENUM('ADMIN','CUSTOMER') DEFAULT 'CUSTOMER', "
                + "status VARCHAR(20) DEFAULT 'ACTIVE'"
                + ")";

        String accountsTable = "CREATE TABLE IF NOT EXISTS accounts("
                + "account_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "aadhar_number VARCHAR(20) NOT NULL, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "balance DECIMAL(12, 2) DEFAULT 0.00, "
                + "account_type ENUM('SAVINGS', 'CHECKING', 'FIXED_DEPOSIT', 'LOAN') DEFAULT 'SAVINGS', "
                + "account_name VARCHAR(100) NOT NULL, "
                + "account_number VARCHAR(30) UNIQUE, "
                + "ifsc_code VARCHAR(11) NOT NULL, "
                + "status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE', "
                + "FOREIGN KEY(aadhar_number) REFERENCES customers(aadhar_number) ON DELETE CASCADE"
                + ")";

        String transactionsTable = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + "description VARCHAR(255), "
                + "sender_account VARCHAR(30) NOT NULL, "
                + "receiver_account VARCHAR(30) NOT NULL, "
                + "amount DECIMAL(12, 2) NOT NULL, "
                + "transaction_type ENUM('ONLINE') DEFAULT 'ONLINE', "
                + "status ENUM('PENDING','COMPLETED','FAILED') DEFAULT 'PENDING', "
                + "failure_reason VARCHAR(255), "
                + "FOREIGN KEY (sender_account) REFERENCES accounts(account_number), "
                + "FOREIGN KEY (receiver_account) REFERENCES accounts(account_number)"
                + ")";

        try (Connection conn = DBConnection.getConnection();
             Statement state = conn.createStatement()) {

            state.execute(customersTable);
            System.out.println("Customers table is ready.");

            state.execute(accountsTable);
            System.out.println("Accounts table is ready.");

            state.execute(transactionsTable);
            System.out.println("Transactions table is ready.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
