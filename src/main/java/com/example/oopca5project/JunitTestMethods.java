// IMPORTANT *****
// This class is for separating DAO method logic from the main app. This helps with testing purposes in MainAppTest

package com.example.oopca5project;

import com.example.oopca5project.DAOs.*;
import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;

public class JunitTestMethods {
    static ProductDaoInterface IProductDao = new MySqlProductDao();
    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao();
    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao();

    // Finder: FILTER PRODUCTS < PRICE TESTS
    // Method that returns only the products where their price is less than the price that was passed in
    public static List<Product> filterProductsByPrice(double price, List<Product> products) {
        return products.stream()
                .filter(p -> p.getPrice() < price)
                .collect(Collectors.toList());
    }

    // FINDER: PRODUCT TO JSON OBJECT TESTS
    // Method that returns a passed in product object as a JSON object
    public static JSONObject turnProductIntoJson(Product product) {
        // Checks if the passed product is null
        if(product == null) {

            // Returns null if null
            return null;

        }

        // Creates JSONObject
        JSONObject jsonObject = new JSONObject();

        // Puts product info in the JSONObject in a 'Key' -> 'Value' format
        jsonObject.put("product_id", product.getId());
        jsonObject.put("product_description", product.getDescription());
        jsonObject.put("size", product.getSize());
        jsonObject.put("unit_price", product.getPrice());
        jsonObject.put("supplier_id", product.getSupplierId());

        // Returns newly created JSONObject
        return jsonObject;
    }

    // ***** NO TESTS CREATED *****
    public static Product makeProductFromJSON(JSONObject jsonObject) {

        // Create product
        Product product = new Product();

        // initialize product using key -> value method from given JSONObject
        product.setId(jsonObject.getString("product_id"));
        product.setDescription(jsonObject.getString("product_description"));
        product.setSize(jsonObject.getString("size"));
        product.setPrice(jsonObject.getDouble("unit_price"));
        product.setSupplierId(jsonObject.getString("supplier_id"));

        // return initialized product
        return product;
    }

    // FINDER: PRODUCT LISTS TO JSON STRING TESTS
    // Method that will take in a list of products and returns that list as a JSONArray
    public static JSONArray productsListToJsonString(List<Product> list) {
        if (list != null) {
            // Creates JSONArray
            JSONArray jsonArray = new JSONArray();

            // Loops through given list
            for (Product product : list) {

                // Places newly created JSONObject into JSONArray
                jsonArray.put(turnProductIntoJson(product));

            }

            // Returns JSONArray in JSON format
            return jsonArray;
        }
        return null;
    }

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

    //  ***** NO TESTS CREATED YET *****
    public static ArrayList<Product> makeProductListFromJSONArray(JSONArray jsonArray) {

        // Create list of Product objects
        ArrayList<Product> list = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            list.add(makeProductFromJSON(obj));
        }

        // return initialized Product list
        return list;
    }

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
}