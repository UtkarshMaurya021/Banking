package com.banking.controller;

import com.banking.payloads.CustomerDTO;
import com.banking.service.impl.CustomerServiceImpl;
import com.banking.services.CustomerService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.HttpHeaders;

import java.util.List;
import java.util.Optional;

@Path("/customers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(){
        this.customerService = new CustomerServiceImpl();
    }

    public CustomerController(CustomerService customerService){
        this.customerService = customerService;
    }

    @POST
    @Path("/create")
    public Response createCustomer(CustomerDTO customerDTO){
        try{
            CustomerDTO createdCustomer = customerService.createCustomer(customerDTO);
            return Response.ok(createdCustomer).build();
        }catch (Exception ex){
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
        }
    }

    @GET
    @Path("/get/all")
    public Response getAllCustomers(){
        try{
            List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
            return Response.ok(customerDTOList).build();
        }catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Path("/status/{aadhar}")
    public Response changeCustomerStatus(@PathParam("aadhar") String aadhar,
                                         @QueryParam("status") String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST).entity("Missing status parameter").build();
            }
            com.banking.payloads.CustomerDTO dto = new com.banking.payloads.CustomerDTO();
            dto.setStatus(status.toUpperCase().trim());

            Optional<com.banking.payloads.CustomerDTO> updated = customerService.updateCustomerByAadhar(aadhar, dto);
            if (updated.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found or update failed").build();
            }
            return Response.ok(updated.get()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/get/{aadhar}")
    public Response getByAadhar(@PathParam("aadhar") String aadhar){
        try{
            Optional<CustomerDTO> customerOpt = customerService.getCustomerByAadhar(aadhar);
            if (customerOpt.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            return Response.ok(customerOpt.get()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/update/{aadhar}")
    public Response updateByAadhar(@PathParam("aadhar") String aadhar, CustomerDTO customerDTO){
        try{
            Optional<CustomerDTO> updated = customerService.updateCustomerByAadhar(aadhar, customerDTO);
            if (updated.isEmpty()) return Response.status(Response.Status.NOT_FOUND).entity("Update failed or customer not found").build();
            return Response.ok(updated.get()).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/delete/{aadhar}")
    public Response deleteByAadhar(@PathParam("aadhar") String aadhar) {
        try {
            boolean deleted = customerService.deleteCustomerByAadhar(aadhar);
            if (deleted) return Response.ok("Customer deleted successfully").build();
            else return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
