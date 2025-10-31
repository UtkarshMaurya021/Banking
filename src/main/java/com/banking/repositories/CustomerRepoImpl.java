package com.banking.repositories;

import com.banking.config.DBConnection;
import com.banking.entities.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepoImpl implements CustomerRepo {

    @Override
    public int save(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(name, phone_number, email, address, customer_pin, aadhar_number, dob, role, username, password_hash) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement pre = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pre.setString(1, customer.getName());
            pre.setString(2, customer.getPhoneNumber());
            pre.setString(3, customer.getEmail());
            pre.setString(4, customer.getAddress());
            pre.setString(5, customer.getCustomerPin());
            pre.setString(6, customer.getAadharNumber());
            pre.setString(7, customer.getDob());
            pre.setString(8, customer.getRole() != null ? customer.getRole() : "CUSTOMER");
            pre.setString(9, customer.getUsername());
            pre.setString(10, customer.getPasswordHash());
            pre.executeUpdate();

            try(ResultSet rs = pre.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    customer.setId(generatedId);
                    return fetchAndFill(customer, conn);
                }
            }
        }
        throw new SQLException("Failed to insert customer, no ID obtained.");
    }

    private int fetchAndFill(Customer customer, Connection conn) throws SQLException {
        String sql = "SELECT * FROM customers WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customer.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Customer fullCustomer = mapResultSetToCustomer(rs);
                    customer.setStatus(fullCustomer.getStatus());
                    customer.setRole(fullCustomer.getRole());
                    customer.setUsername(fullCustomer.getUsername());
                    customer.setPasswordHash(fullCustomer.getPasswordHash());
                    return customer.getId();
                }
            }
        }
        return customer.getId();
    }

    @Override
    public List<Customer> findAll() throws SQLException {
        String sql = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement state = conn.createStatement();
             ResultSet rs = state.executeQuery(sql)) {

            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    @Override
    public Optional<Customer> findById(int id) throws SQLException {
        String sql = "SELECT id, name, phone_number, email, address, customer_pin, aadhar_number, dob, status, role, username, password_hash " +
                "FROM customers WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {
            pre.setInt(1, id);

            try (ResultSet rs = pre.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Customer customer) throws SQLException {

        String sql = "UPDATE customers SET name=?, phone_number=?, email=?, address=?, customer_pin=?, aadhar_number=?, dob=?, status=?, role=?, username=?, password_hash=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pre = conn.prepareStatement(sql)) {

            pre.setString(1, customer.getName());
            pre.setString(2, customer.getPhoneNumber());
            pre.setString(3, customer.getEmail());
            pre.setString(4, customer.getAddress());
            pre.setString(5, customer.getCustomerPin());
            pre.setString(6, customer.getAadharNumber());
            pre.setString(7, customer.getDob());
            pre.setString(8, customer.getStatus());
            pre.setString(9, customer.getRole() != null ? customer.getRole() : "CUSTOMER");
            pre.setString(10, customer.getUsername());
            pre.setString(11, customer.getPasswordHash());
            pre.setInt(12, customer.getId());

            return pre.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE id = ?";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Customer> findByAadhar(String aadhar) throws SQLException {
        String sql = "SELECT id, name, phone_number, email, address, customer_pin, aadhar_number, dob, status, role, username, password_hash FROM customers WHERE aadhar_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aadhar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteByAadhar(String aadhar) throws SQLException {
        String sql = "DELETE FROM customers WHERE aadhar_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, aadhar);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Customer> findByUsername(String username) throws SQLException {
        String sql = "SELECT id, name, phone_number, email, address, customer_pin, aadhar_number, dob, status, role, username, password_hash FROM customers WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCustomer(rs));
                }
            }
        }
        return Optional.empty();
    }


    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getInt("id"));
        customer.setName(rs.getString("name"));
        customer.setPhoneNumber(rs.getString("phone_number"));
        customer.setEmail(rs.getString("email"));
        customer.setAddress(rs.getString("address"));
        customer.setCustomerPin(rs.getString("customer_pin"));
        customer.setAadharNumber(rs.getString("aadhar_number"));
        customer.setDob(rs.getString("dob"));
        customer.setStatus(rs.getString("status"));


        try {
            customer.setUsername(rs.getString("username"));
        } catch (SQLException ignored) {
            customer.setUsername(null);
        }
        try {
            customer.setPasswordHash(rs.getString("password_hash"));
        } catch (SQLException ignored) {
            customer.setPasswordHash(null);
        }

        try {
            customer.setRole(rs.getString("role"));
        } catch (SQLException ignored) {
            customer.setRole("CUSTOMER");
        }
        return customer;
    }
}
