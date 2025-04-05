package com.example.oopca5project;

import com.example.oopca5project.DAOs.*;
import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Scanner;
// This class is for separating DAO method logic from the main app. This helps with testing purposes
public class DaoMethods {
    static Scanner sc = new Scanner(System.in);

    static ProductDaoInterface IProductDao = new MySqlProductDao();

    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao();

    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao();

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
        unit_price = Methods.validateDoubleRange(0);
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
                jsonArray.put(turnProductIntoJson(product));

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

    public static void getAllSuppliers() {
        try {
            List<Supplier> suppliers = ISupplierDao.getAllSuppliers();

            System.out.println("Retrieving all suppliers...");
            if (suppliers.isEmpty()) {
                System.out.println("Suppliers table is empty! Please add some data first.");
            } else {
                for (Supplier supplier : suppliers) {
                    System.out.println("{" + supplier.toString() + "}");
                }
            }

            System.out.println();

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void getAllCustomers() {
        try {
            List<Customer> customers = ICustomerDao.getAllCustomers();

            System.out.println("Retrieving all customers...");
            if (customers.isEmpty()) {
                System.out.println("Customers table is empty! Please add some data first.");
            } else {
                for (Customer customer : customers) {
                    System.out.println("{" + customer.toString() + "}");
                }
            }

            System.out.println();

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void addSupplier() {
        String id, name, phone, email;

        System.out.println("please enter the id (SupplierX)");
        id = sc.next();

        System.out.println("Please enter the name");
        name = sc.next();

        System.out.println("Please enter the phone");
        phone = sc.next();

        System.out.println("Please enter the email");
        email = sc.next();

        Supplier supplier = new Supplier(id, name, phone, email);
        try {
            int rowsAffected = ISupplierDao.addSupplier(supplier);

            if(rowsAffected >= 1) {
                System.out.println("Successfully added supplier");
            }
            else {
                System.err.println("Error! Supplier was not added");
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }
}
