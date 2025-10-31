package com.banking.entities;

public class Customer {

    private Integer id;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
    private String customerPin;
    private String aadharNumber;
    private String dob;
    private String status;
    private String role;

    private String username;
    private String passwordHash;

    public Customer(){}

    public Customer(Integer id, String name, String phoneNumber, String email, String address,
                    String customerPin, String aadharNumber, String dob, String status,
                    String role, String username, String passwordHash){
        this.id = id; this.name = name; this.phoneNumber = phoneNumber; this.email = email;
        this.address = address; this.customerPin = customerPin; this.aadharNumber = aadharNumber;
        this.dob = dob; this.status = status; this.role = role;
        this.username = username; this.passwordHash = passwordHash;
    }


    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Integer getId() { return id; }
    public void setId(Integer id){ this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCustomerPin() { return customerPin; }
    public void setCustomerPin(String customerPin) { this.customerPin = customerPin; }
    public String getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(String aadharNumber) { this.aadharNumber = aadharNumber; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
