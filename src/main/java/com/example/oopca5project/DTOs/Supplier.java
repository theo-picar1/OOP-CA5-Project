package com.example.oopca5project.DTOs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    // ***** NO TESTS CREATED YET *****
    // Same logic as productsListToJsonString
    public static JSONArray suppliersListToJsonString(List<Supplier> list) {
        if (list != null) {
            // Creates JSONArray
            JSONArray jsonArray = new JSONArray();

            // Loops through given list
            for (Supplier supplier : list) {

                // Places newly created JSONObject into JSONArray
                jsonArray.put(turnSupplierIntoJson(supplier));

            }

            // Returns JSONArray in JSON format
            return jsonArray;
        }
        return null;
    }

    //     ***** NO TESTS CREATED YET *****
    public static JSONObject turnSupplierIntoJson(Supplier supplier) {
        // Checks if the passed product is null
        if(supplier == null) {

            // Returns null if null
            return null;

        }

        // Creates JSONObject
        JSONObject jsonObject = new JSONObject();

        // Puts product info in the JSONObject in a 'Key' -> 'Value' format
        jsonObject.put("supplier_id", supplier.getId());
        jsonObject.put("supplier_name", supplier.getName());
        jsonObject.put("supplier_phone_no", supplier.getPhoneNo());
        jsonObject.put("supplier_email", supplier.getEmail());

        // Returns newly created JSONObject
        return jsonObject;
    }

    //  ***** NO TESTS CREATED YET *****
    public static ArrayList<Supplier> makeSupplierListFromJSONArray(JSONArray jsonArray) {

        // Create list of Supplier objects
        ArrayList<Supplier> list = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            list.add(makeSupplierFromJSON(obj));
        }

        // return initialized Supplier list
        return list;
    }

    public static Supplier createSupplier(String id) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter supplier's name:");
        String name = sc.next();

        System.out.println("Enter supplier's telephone number");
        String phone = sc.next();

        System.out.println("Enter supplier's email");
        String email = sc.next();

        return new Supplier(id, name, phone, email);
    }

    //  ***** NO TESTS CREATED YET *****
    public static Supplier makeSupplierFromJSON(JSONObject jsonObject) {

        // Create supplier
        Supplier supplier = new Supplier();

        // initialize supplier using key -> value method from given JSONObject
        supplier.setId(jsonObject.getString("supplier_id"));
        supplier.setName(jsonObject.getString("supplier_name"));
        supplier.setPhoneNo(jsonObject.getString("supplier_phone_no"));
        supplier.setEmail(jsonObject.getString("supplier_email"));

        // return initialized supplier
        return supplier;
    }

    @Override
    public String toString() {
        return "ID = " + supplier_id +
                ", Name = " + supplier_name +
                ", Phone no. = " + supplier_phone_no +
                ", Email = " + supplier_email;
    }
}
