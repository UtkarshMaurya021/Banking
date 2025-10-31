package com.banking.controller;

import com.banking.payloads.AccountDTO;
import com.banking.service.impl.AccountServiceImpl;
import com.banking.services.AccountService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountController {

    private final AccountService accountService;

    public AccountController() {
        this.accountService = new AccountServiceImpl();
    }

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Path("/create")
    public Response createAccount(AccountDTO accountDTO, @Context HttpHeaders headers){
        try {
            // No role check - frontend handles who can create what.
            AccountDTO createdAccount = accountService.createAccount(accountDTO);
            return Response.ok(createdAccount).build();
        } catch (IllegalArgumentException ex) {
            // validation not enforced, but if repo throws, return bad request
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (Exception e){
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/all")
    public Response getAllAccounts() {
        try {
            List<AccountDTO> accounts = accountService.getAllAccounts();
            return Response.ok(accounts).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/customer/{aadhar}")
    public Response getAccountsByAadhar(@PathParam("aadhar") String aadhar) {
        try {
            List<AccountDTO> accounts = accountService.getAccountsByAadhar(aadhar);
            return Response.ok(accounts).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PUT
    @Path("/status/{accountNumber}")
    public Response changeAccountStatus(@PathParam("accountNumber") String accountNumber,
                                        @QueryParam("status") String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing status parameter").build();
            }
            // Prepare minimal DTO - service will only update provided fields
            com.banking.payloads.AccountDTO dto = new com.banking.payloads.AccountDTO();
            dto.setStatus(status.toUpperCase().trim());

            Optional<com.banking.payloads.AccountDTO> updated = accountService.updateAccountByNumber(accountNumber, dto);
            if (updated.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Account not found or update failed").build();
            }
            return Response.ok(updated.get()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/get/{id}")
    public Response getAccountById(@PathParam("id") int accountId) {
        try {
            Optional<AccountDTO> accountOpt = accountService.getAccountById(accountId);
            if (accountOpt.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
            }
            return Response.ok(accountOpt.get()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/update/{accountNumber}")
    public Response updateAccountByNumber(@PathParam("accountNumber") String accountNumber, AccountDTO accountDTO){
        try {
            Optional<AccountDTO> updated = accountService.updateAccountByNumber(accountNumber, accountDTO);
            if (updated.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Update failed").build();
            }
            return Response.ok(updated.get()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/delete/{accountNumber}")
    public Response deleteAccount(@PathParam("accountNumber") String accountNumber) {
        try {
            boolean deleted = accountService.deleteAccountByNumber(accountNumber);
            if (deleted) return Response.ok("Account deleted successfully").build();
            else return Response.status(Response.Status.NOT_FOUND).entity("Account not found").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
