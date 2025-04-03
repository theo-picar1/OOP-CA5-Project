package com.example.oopca5project;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.oopca5project.DTOs.Product;

public class Methods {
    static Scanner sc = new Scanner(System.in);

    public static void menuOptions(String[] options) {
        for(int i = 0; i < options.length; i++) {
            System.out.println(i+1 + ". " + options[i]);
        }
    }

    public static int validateRange(int min, int max) {
        int input = 0;

        while(true) {
            if(sc.hasNextInt()) {
                input = sc.nextInt();

                if(input < min || input > max) {
                    System.out.println("Please enter a valid option (" +min+ "-" +max+ ")");
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Please enter only integer values");
                sc.next();
            }
        }

        return input;
    }

    public static double validateDoubleRange(double min) {

        // initializing variable
        double input = 0;

        // loop to go on forever until correct input is achieved
        while(true) {

            // checking if next input is a double
            if(sc.hasNextDouble()) {

                // when input is a double assign it to a variable
                input = sc.nextDouble();

                // check if input is within range
                if(input < min) {

                    // input is wrong -> print out error message
                    System.out.println("Please enter a valid option (" +min+ "or higher)");

                }
                else {

                    // input is correct -> break out of loop
                    break;

                }
            } else {

                // input was not a double -> print out error message
                System.out.println("Please enter a valid number");

                // get rid of stray \n (new line) character
                sc.next();
            }
        }

        // input is a double + in range -> input is returned
        return input;

    }

    public static Product getProduct(String id) {

        // initializing variables
        String product_description, size, supplier_id;
        double unit_price;

        // getting rid of stray \n (new line) character
        sc.nextLine();

        // getting product description
        System.out.println("Enter product description: ");
        product_description = sc.nextLine();

        // getting product size
        System.out.println("Enter product size: ");
        size = sc.nextLine();

        // getting product unit price
        System.out.println("Enter product unit price: ");
        unit_price = validateDoubleRange(0);
        sc.nextLine();

        // getting product supplier id
        System.out.println("Enter product supplier id (e.g. 'supplier1', 'supplier2'): ");
        supplier_id = sc.next();

        // returns newly made product object
        return new Product(id,product_description,size,unit_price,supplier_id);
        
    }

    // Question 7
    public static JSONArray productsListToJsonString(List<Product> list) {
        if (list != null) {
            // Creates JSONArray
            JSONArray jsonArray = new JSONArray();

            // Loops through given list
            for (Product product : list) {

                // Places newly created JSONObject into JSONArray
                jsonArray.put(Methods.turnProductIntoJson(product));

            }

            // Returns JSONArray in JSON format
            return jsonArray;
        }
        return null;
    }

    // SQL DAO METHODS - FOR JUNIT TESTING
    public static List<Product> filterProductsByPrice(double price, List<Product> products) {
        return products.stream()
                .filter(p -> p.getPrice() < price)
                .collect(Collectors.toList());
    }

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

    public static void printObjectIfNotNull(Object obj){

        // checks if object is null
        if(obj != null){

            // Print object if not null
            System.out.println(obj);

        }
    }
}