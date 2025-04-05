package com.example.oopca5project.DTOs;

public class Supplier {
    private String supplier_id;
    private String supplier_name;
    private String supplier_phone_no;
    private String supplier_email;

    public Supplier(String id, String name, String phone, String email) {
        supplier_id = id;
        supplier_name = name;
        supplier_phone_no = phone;
        supplier_email = email;
    }

    public Supplier() { }

    public String getId() { return supplier_id; }

    public void setId(String id) { supplier_id = id; }

    public String getName() { return supplier_name; }

    public void setName(String name) { supplier_name = name; }

    public String getPhoneNo() { return supplier_phone_no; }

    public void setPhoneNo(String phone) { supplier_phone_no = phone; }

    public String getEmail() { return supplier_email; }

    public void setEmail(String email) { supplier_email = email; }

    @Override
    public String toString() {
        return "ID = " + supplier_id +
                ", Name = " + supplier_name +
                ", Phone no. = " + supplier_phone_no +
                ", Email = " + supplier_email;
    }
}
