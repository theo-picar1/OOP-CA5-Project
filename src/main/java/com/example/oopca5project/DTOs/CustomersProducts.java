package com.example.oopca5project.DTOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class CustomersProducts {
    private int customerId;
    private String productId;
    private int quantity;

    public CustomersProducts() { }

    public CustomersProducts(int customerId, String productId, int quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


// ***** NO TESTS CREATED YET *****
    public static JSONArray customersProductsListToJsonString(List<CustomersProducts> list) {
        if (list != null) {
            // Creates JSONArray
            JSONArray jsonArray = new JSONArray();

            // Loops through given list
            for (CustomersProducts customerP : list) {

                // Places newly created JSONObject into JSONArray
                jsonArray.put(turnCustomersProductsIntoJson(customerP));

            }

            // Returns JSONArray in JSON format
            return jsonArray;
        }
        return null;
    }

    //     ***** NO TESTS CREATED YET *****
    public static JSONObject turnCustomersProductsIntoJson(CustomersProducts customerP) {
        // Checks if the passed CustomersProducts is null
        if(customerP == null) {

            // Returns null if null
            return null;

        }

        // Creates JSONObject
        JSONObject jsonObject = new JSONObject();

        // Puts product info in the JSONObject in a 'Key' -> 'Value' format
        jsonObject.put("customer_id", customerP.getCustomerId());
        jsonObject.put("product_id", customerP.getProductId());
        jsonObject.put("quantity", customerP.getQuantity());

        // Returns newly created JSONObject
        return jsonObject;
    }

    //  ***** NO TESTS CREATED YET *****
    public static ArrayList<CustomersProducts> makeCustomersProductsListFromJSONArray(JSONArray jsonArray) {

        // Create list of CustomersProducts objects
        ArrayList<CustomersProducts> list = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            list.add(makeCustomersProductsFromJSON(obj));
        }

        // return initialized CustomersProducts list
        return list;
    }

    //  ***** NO TESTS CREATED YET *****
    public static CustomersProducts makeCustomersProductsFromJSON(JSONObject jsonObject) {

        // Create CustomersProducts
        CustomersProducts customerP = new CustomersProducts();

        // initialize CustomersProducts using key -> value method from given JSONObject
        customerP.setCustomerId(jsonObject.getInt("customer_id"));
        customerP.setProductId(jsonObject.getString("product_id"));
        customerP.setQuantity(jsonObject.getInt("quantity"));

        // return initialized CustomersProducts
        return customerP;
    }

    public static CustomersProducts createCustomersProducts(int customerId, String productId) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter Quantity");
        int quantity = sc.nextInt();
        sc.nextLine();

        return new CustomersProducts(customerId, productId, quantity);
    }

    @Override
    public String toString() {
        return "Product ID = " + customerId +
                ", Customer ID = " + productId +
                ", Quantity = " + quantity;
    }
}
