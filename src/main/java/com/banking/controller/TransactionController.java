package com.banking.controller;

import com.banking.entities.Transaction;
import com.banking.payloads.TransactionDTO;
import com.banking.service.impl.TransactionServiceImpl;
import com.banking.services.TransactionService;
import com.banking.exceptions.ResourceNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController() {
        this.transactionService = new TransactionServiceImpl();
    }

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @POST
    @Path("/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(TransactionDTO transactionDTO) {
        try {
            Transaction transaction = transactionService.transfer(transactionDTO);
            if ("COMPLETED".equals(transaction.getStatus().name())) {
                return Response.ok(transaction).build();
            } else {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(transaction).build();
            }
        } catch (ResourceNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ex.getMessage()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Unexpected error: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/account/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsByAccountJson(@PathParam("accountNumber") String accountNumber) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber);
            if (transactions == null || transactions.isEmpty()) {
                return Response.ok(java.util.Collections.emptyList()).build();
            }
            return Response.ok(transactions).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to load transactions: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/account/{accountNumber}/export")
    @Produces({ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "text/csv" })
    public Response exportTransactionsByAccount(@PathParam("accountNumber") String accountNumber,
                                                @QueryParam("type") @DefaultValue("excel") String type) {
        try {
            List<Transaction> transactions = transactionService.getTransactionsByAccount(accountNumber);
            if (transactions.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No transactions found for account: " + accountNumber).build();
            }
            if ("csv".equalsIgnoreCase(type)) {
                java.io.File csvFile = com.banking.utils.TransactionExportUtil.exportToCSV(accountNumber, transactions);
                return Response.ok(csvFile)
                        .header("Content-Disposition", "attachment; filename=\"transactions_" + accountNumber + ".csv\"")
                        .build();
            } else {
                java.io.File excelFile = com.banking.utils.TransactionExportUtil.exportToExcel(accountNumber, transactions);
                return Response.ok(excelFile)
                        .header("Content-Disposition", "attachment; filename=\"transactions_" + accountNumber + ".xlsx\"")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to export transactions: " + e.getMessage()).build();
        }
    }
}
