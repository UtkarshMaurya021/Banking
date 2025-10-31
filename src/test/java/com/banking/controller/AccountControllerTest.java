//package com.banking.controller;
//
//import com.banking.payloads.AccountDTO;
//import com.banking.services.AccountService;
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class AccountControllerTest {
//
//    private AccountController accountController;
//    private AccountService accountService;
//
//    @BeforeEach
//    void setUp() {
//        accountService = Mockito.mock(AccountService.class);
//
//        accountController = new AccountController(accountService);
//    }
//
//    @Test
//    void createAccount_success() throws SQLException {
//        AccountDTO dto = new AccountDTO(1, "999988887777", "SBI",
//                "ACC45543216", 500.0, "SAVINGS", "SBIN0123456", "ACTIVE");
//
//        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(dto);
//
//        Response response = accountController.createAccount(dto);
//
//        assertEquals(200, response.getStatus());
//        AccountDTO result = (AccountDTO) response.getEntity();
//        assertEquals("SBI", result.getAccountName());
//    }
//
//    @Test
//    void getAllAccounts_success() throws SQLException {
//        List<AccountDTO> accounts = Arrays.asList(
//                new AccountDTO(1, "111122223333", "HDFC", "ACC456789098", 200.0, "SAVINGS", "HDFC0123456", "ACTIVE"),
//                new AccountDTO(2, "222233331111", "ICIC", "ACC765432112", 300.0, "CHECKING", "ICIC0987654", "ACTIVE")
//        );
//
//        when(accountService.getAllAccounts()).thenReturn(accounts);
//
//        Response response = accountController.getAllAccounts();
//
//        assertEquals(200, response.getStatus());
//        assertEquals(2, ((List<?>) response.getEntity()).size());
//    }
//
//    @Test
//    void getAccountById_found() throws Exception {
//        AccountDTO dto = new AccountDTO(1, "123321456654", "Test", "ACC198087464", 1000.0,
//                "SAVINGS", "PQRS0123456", "ACTIVE");
//
//        when(accountService.getAccountById(1)).thenReturn(Optional.of(dto));
//
//        Response response = accountController.getAccountById(1);
//
//        assertEquals(200, response.getStatus());
//        assertTrue(response.getEntity() instanceof AccountDTO);
//        assertEquals("Test", ((AccountDTO) response.getEntity()).getAccountName());
//    }
//
//    @Test
//    void getAccountById_notFound() throws Exception {
//        when(accountService.getAccountById(1)).thenReturn(Optional.empty());
//
//        Response response = accountController.getAccountById(1);
//
//        assertEquals(404, response.getStatus());
//        assertEquals("Account not found", response.getEntity());
//    }
//
//    @Test
//    void updateAccount_success() throws Exception {
//        AccountDTO dto = new AccountDTO(1, "4444", "Updated", "ACC2",
//                2000.0, "CHECKING", "LMNO0123456", "ACTIVE");
//
//        when(accountService.updateAccount(eq(1), any(AccountDTO.class)))
//                .thenReturn(Optional.of(dto));
//
//        Response response = accountController.updateAccount(1, dto);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Updated", ((AccountDTO) response.getEntity()).getAccountName());
//    }
//
//    @Test
//    void updateAccount_notFound() throws Exception {
//        when(accountService.updateAccount(eq(1), any(AccountDTO.class))).thenReturn(Optional.empty());
//
//        Response response = accountController.updateAccount(1, new AccountDTO());
//
//        assertEquals(404, response.getStatus());
//        assertEquals("Account not found", response.getEntity());
//    }
//
//    @Test
//    void deleteAccount_success() throws Exception {
//        when(accountService.deleteAccountByNumber("123")).thenReturn(true);
//
//        Response response = accountController.deleteAccount("123");
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Account deleted successfully", response.getEntity());
//    }
//
//    @Test
//    void deleteAccount_notFound() throws Exception {
//        when(accountService.deleteAccountByNumber("123")).thenReturn(false);
//
//        Response response = accountController.deleteAccount("123");
//
//        assertEquals(404, response.getStatus());
//        assertEquals("Account not found", response.getEntity());
//    }
//}
