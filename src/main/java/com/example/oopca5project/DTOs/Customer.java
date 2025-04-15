package com.example.oopca5project.DTOs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    public Customer(String name, String email, String address) {
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

    //  ***** NO TESTS CREATED YET *****
    public static ArrayList<Customer> makeCustomerListFromJSONArray(JSONArray jsonArray) {

        // Create list of Customer objects
        ArrayList<Customer> list = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            list.add(makeCustomerFromJSON(obj));
        }

        // return initialized Customer list
        return list;
    }

    //  ***** NO TESTS CREATED YET *****
    public static Customer makeCustomerFromJSON(JSONObject jsonObject) {

        // Create Customer
        Customer customer = new Customer();

        // initialize supplier using key -> value method from given JSONObject
        customer.setId(jsonObject.getInt("customer_id"));
        customer.setName(jsonObject.getString("customer_name"));
        customer.setEmail(jsonObject.getString("customer_email"));
        customer.setAddress(jsonObject.getString("customer_address"));

        // return initialized Customer
        return customer;
    }

    //  ***** NO TESTS CREATED YET *****
    public static JSONArray customersListToJsonString(List<Customer> list) {
        if (list != null) {
            // Creates JSONArray
            JSONArray jsonArray = new JSONArray();

            // Loops through given list
            for (Customer customer : list) {

                // Places newly created JSONObject into JSONArray
                jsonArray.put(turnCustomerIntoJson(customer));

            }

            // Returns JSONArray in JSON format
            return jsonArray;
        }
        return null;
    }

    //  ***** NO TESTS CREATED YET *****
    public static JSONObject turnCustomerIntoJson(Customer customer) {
        // Checks if the passed product is null
        if(customer == null) {

            // Returns null if null
            return null;

        }

        // Creates JSONObject
        JSONObject jsonObject = new JSONObject();

        // Puts customer info in the JSONObject in a 'Key' -> 'Value' format
        jsonObject.put("customer_id", customer.getId());
        jsonObject.put("customer_name", customer.getName());
        jsonObject.put("customer_email", customer.getEmail());
        jsonObject.put("customer_address", customer.getAddress());

        // Returns newly created JSONObject
        return jsonObject;
    }

    // Method that handles user input for getting Customer object
    public static Customer createCustomer() {
        Scanner sc = new Scanner(System.in);

        // initializing variables
        String name, email, phoneNo;

        // Get Customer variables
        System.out.println("Enter customer first name: ");
        name = sc.next();

        System.out.println("Enter customer email: ");
        email = sc.next();

        System.out.println("Enter customer phone number: ");
        phoneNo = sc.next();

        // Return Customer object
        return new Customer(name, email, phoneNo);
    }

    @Override
    public String toString() {
        return "ID = " + customer_id +
                ", Name = " + customer_name +
                ", Email = " + customer_email +
                ", Address = " + customer_address;
    }
}