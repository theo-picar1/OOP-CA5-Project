package com.example.oopca5project.DTOs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Product {
    private String product_id;
    private String product_description;
    private String size;
    private double unit_price;
    private String supplier_id;

    public Product(String id, String description, String size, double price, String supplier_id) {
        this.product_id = id;
        product_description = description;
        this.size = size;
        unit_price = price;
        this.supplier_id = supplier_id;
    }

    public Product() { }

    public String getId() {
        return product_id;
    }

    public void setId(String id) {
        product_id = id;
    }

    public String getDescription() {
        return product_description;
    }

    public void setDescription(String description) {
        product_description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return unit_price;
    }

    public void setPrice(double price) {
        this.unit_price = price;
    }

    public String getSupplierId() {
        return supplier_id;
    }

    public void setSupplierId(String id) {
        supplier_id = id;
    }

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

    //  ***** NO TESTS CREATED YET *****
    public static ArrayList<Product> makeProductListFromJSONArray(JSONArray jsonArray) {

        // Create list of Product objects
        ArrayList<Product> list = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            list.add(Product.makeProductFromJSON(obj));
        }

        // return initialized Product list
        return list;
    }

    @Override
    public String toString() {
        return "ID = " + product_id +
                ", Description = " + product_description +
                ", Size = " + size +
                ", Price € = " + unit_price +
                ", SupplierID = " + supplier_id;
    }
}
