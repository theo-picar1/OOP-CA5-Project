package com.example.oopca5project.DTOs;

public class Customer {
    private int customer_id;
    private String customer_name;
    private String customer_email;
    private String customer_address;

    public Customer() { }

    public Customer(int id, String name, String email, String address) {
        customer_id = id;
        customer_name = name;
        customer_email = email;
        customer_address = address;
    }

    public int getId() { return customer_id; }

    public void setId(int id) { customer_id = id; }

    public String getName() { return customer_name; }

    public void setName(String name) { customer_name = name; }

    public String getEmail() { return customer_email; }

    public void setEmail(String email) { customer_email = email; }

    public String getAddress() { return customer_address; }

    public void setAddress(String address) { customer_address = address; }

    @Override
    public String toString() {
        return "ID = " + customer_id +
                ", Name = " + customer_name +
                ", Email = " + customer_email +
                ", Address = " + customer_address;
    }
}