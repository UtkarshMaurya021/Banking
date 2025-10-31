package com.banking;

import com.banking.config.DBInitializer;

public class BankingApplication {
    public static void main(String[] args){

        DBInitializer.initialize();
        System.out.println("Welcome to Banking Application");
    }

}
