//package com.banking.controller;
//
//import com.banking.payloads.CustomerDTO;
//import com.banking.service.impl.CustomerServiceImpl;
//import com.banking.services.CustomerService;
//import jakarta.ws.rs.core.Response;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//class CustomerControllerTest {
//
//    private CustomerController customerController;
//    private CustomerService customerService;
//
//    @BeforeEach
//    void setUp() {
//        customerService = Mockito.mock(CustomerService.class);
//        customerController = new CustomerController(customerService);
//    }
//
//    @Test
//    void createCustomer_success() throws Exception {
//        CustomerDTO dto = new CustomerDTO();
//        dto.setName("Utkarsh");
//        dto.setPhoneNumber("9999888877");
//        dto.setEmail("utkarsh@test.com");
//        dto.setAddress("Test Address");
//
//        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(dto);
//
//        Response response = customerController.createCustomer(dto);
//
//        assertEquals(200, response.getStatus());
//        CustomerDTO result = (CustomerDTO) response.getEntity();
//        assertEquals("Utkarsh", result.getName());
//    }
//
//    @Test
//    void getAllCustomers_success() throws Exception {
//        List<CustomerDTO> customers = Arrays.asList(
//                new CustomerDTO(), new CustomerDTO()
//        );
//        when(customerService.getAllCustomers()).thenReturn(customers);
//
//        Response response = customerController.getAllCustomers();
//
//        assertEquals(200, response.getStatus());
//        assertEquals(2, ((List<?>) response.getEntity()).size());
//    }
//
//    @Test
//    void getById_found() throws Exception {
//        CustomerDTO dto = new CustomerDTO();
//        dto.setName("Test Customer");
//
//        when(customerService.getCustomerById(1)).thenReturn(Optional.of(dto));
//
//        Response response = customerController.getById(1);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Test Customer", ((CustomerDTO) response.getEntity()).getName());
//    }
//
//    @Test
//    void getById_notFound() throws Exception {
//        when(customerService.getCustomerById(1)).thenReturn(Optional.empty());
//
//        Response response = customerController.getById(1);
//
//        assertEquals(404, response.getStatus());
//        assertEquals("Customer not found", response.getEntity());
//    }
//
//    @Test
//    void updateCustomer_success() throws Exception {
//        CustomerDTO dto = new CustomerDTO();
//        dto.setName("Updated Name");
//
//        when(customerService.updateCustomer(eq(1), any(CustomerDTO.class))).thenReturn(Optional.of(dto));
//
//        Response response = customerController.updateCustomer(1, dto);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Updated Name", ((CustomerDTO) response.getEntity()).getName());
//    }
//
//    @Test
//    void updateCustomer_notFound() throws Exception {
//        when(customerService.updateCustomer(eq(1), any(CustomerDTO.class))).thenReturn(Optional.empty());
//
//        Response response = customerController.updateCustomer(1, new CustomerDTO());
//
//        assertEquals(404, response.getStatus());
//        assertEquals("Customer not found", response.getEntity());
//    }
//
//    @Test
//    void deleteCustomer_success() throws Exception {
//        when(customerService.deleteCustomer(1)).thenReturn(true);
//
//        Response response = customerController.delete(1);
//
//        assertEquals(200, response.getStatus());
//        assertEquals("Customer deleted successfully", response.getEntity());
//    }
//
//    @Test
//    void deleteCustomer_notFound() throws Exception {
//        when(customerService.deleteCustomer(1)).thenReturn(false);
//
//        Response response = customerController.delete(1);
//
//        assertEquals(404, response.getStatus());
//        assertEquals("Customer not found", response.getEntity());
//    }
//}
