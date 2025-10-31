//package com.banking.controller;
//
//import com.banking.entities.Transaction;
//import com.banking.entities.TransactionStatus;
//import com.banking.exceptions.ResourceNotFoundException;
//import com.banking.payloads.TransactionDTO;
//import com.banking.services.TransactionService;
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.math.BigDecimal;
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class TransactionControllerTest {
//
//    @Mock
//    private TransactionService transactionService;
//
//    @InjectMocks
//    private TransactionController transactionController;
//
//    private Transaction transaction;
//    private TransactionDTO transactionDTO;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // DTO setup
//        transactionDTO = new TransactionDTO();
//        transactionDTO.setSenderAccount("12345");
//        transactionDTO.setReceiverAccount("67890");
//        transactionDTO.setAmount(new BigDecimal("1000.00"));
//        transactionDTO.setDescription("Test transfer");
//
//        // Transaction setup
//        transaction = new Transaction();
//        transaction.setId(1);
//        transaction.setSenderAccount("12345");
//        transaction.setReceiverAccount("67890");
//        transaction.setAmount(new BigDecimal("1000.00"));
//    }
//
//
//
//    @Test
//    void transfer_SuccessCompletedTransaction() throws Exception {
//        transaction.setStatus(TransactionStatus.COMPLETED);
//
//        when(transactionService.transfer(any(TransactionDTO.class))).thenReturn(transaction);
//
//        Response response = transactionController.transfer(transactionDTO);
//
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//        assertEquals(transaction, response.getEntity());
//    }
//
//    @Test
//    void transfer_FailedTransactionStatusNotCompleted() throws Exception {
//        transaction.setStatus(TransactionStatus.FAILED);
//
//        when(transactionService.transfer(any(TransactionDTO.class))).thenReturn(transaction);
//
//        Response response = transactionController.transfer(transactionDTO);
//
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//        assertEquals(transaction, response.getEntity());
//    }
//
//    @Test
//    void transfer_ResourceNotFoundException() throws Exception {
//        when(transactionService.transfer(any(TransactionDTO.class)))
//                .thenThrow(new ResourceNotFoundException("Account", "accountNumber", transactionDTO.getSenderAccount()));
//
//        Response response = transactionController.transfer(transactionDTO);
//
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//        assertEquals("Account not found", response.getEntity());
//    }
//
//    @Test
//    void transfer_IllegalArgumentException() throws Exception {
//        when(transactionService.transfer(any(TransactionDTO.class)))
//                .thenThrow(new IllegalArgumentException("Invalid amount"));
//
//        Response response = transactionController.transfer(transactionDTO);
//
//        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
//        assertEquals("Invalid amount", response.getEntity());
//    }
//
//    @Test
//    void transfer_SQLException() throws Exception {
//        when(transactionService.transfer(any(TransactionDTO.class)))
//                .thenThrow(new SQLException("Database connection failed"));
//
//        Response response = transactionController.transfer(transactionDTO);
//
//        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
//        assertTrue(response.getEntity().toString().contains("Database error"));
//    }
//
//    @Test
//    void transfer_UnexpectedException() throws Exception {
//        when(transactionService.transfer(any(TransactionDTO.class)))
//                .thenThrow(new RuntimeException("Unexpected crash"));
//
//        Response response = transactionController.transfer(transactionDTO);
//
//        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
//        assertTrue(response.getEntity().toString().contains("Unexpected error"));
//    }
//
//
//    @Test
//    void getTransactionsByAccount_Success() throws Exception {
//        List<Transaction> transactions = List.of(transaction);
//        when(transactionService.getTransactionsByAccount("12345")).thenReturn(transactions);
//
//        Response response = transactionController.getTransactionsByAccount("12345");
//
//        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//        assertEquals(transactions, response.getEntity());
//    }
//
//    @Test
//    void getTransactionsByAccount_NoTransactionsFound() throws Exception {
//        when(transactionService.getTransactionsByAccount("12345")).thenReturn(Collections.emptyList());
//
//        Response response = transactionController.getTransactionsByAccount("12345");
//
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//        assertEquals("No transactions found for account: 12345", response.getEntity());
//    }
//
//    @Test
//    void getTransactionsByAccount_Exception() throws Exception {
//        when(transactionService.getTransactionsByAccount("12345"))
//                .thenThrow(new RuntimeException("Internal failure"));
//
//        Response response = transactionController.getTransactionsByAccount("12345");
//
//        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
//        assertTrue(response.getEntity().toString().contains("Unable to fetch transactions"));
//    }
//}
