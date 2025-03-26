package com.example.oopca5project;

import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import java.util.List;
import java.util.Scanner;

public class MainApp {
    static ProductDaoInterface IProductDao = new MySqlProductDao();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("OOP-CA5 PROJECT");

        menu();
    }

    public static void menu() {
        MainApp client = new MainApp();
        String[] options = {
                "End application",
                "Display all products",
                "Find product by ID",
                "Delete product by ID",
                "Add new product",
                "Update product by ID",
                "Filter products",
                "Display all products as JSON",
                "Display product as JSON",
                "Testing server"
        };

        Methods.menuOptions(options);

        int choice = Methods.validateRange(1, 10);

        try {
            switch (choice) {
                case 1:
                    System.out.println("Ending application. Goodbye!");
                    break;
                case 2:
                    getAllProducts();
                    break;
                case 3:
                    getProductById();
                    break;
                case 4:
                    deleteProductById();
                    break;
                case 5:
                    addProduct();
                    break;
                case 6:
                    updateProduct();
                    break;
                case 7:
                    filterProducts();
                    break;
                case 8:
                    List<Product> products = IProductDao.getAllProducts();
                    productsListToJsonString(products);
                    break;
                case 9:
                    productToJsonString();
                    break;
                case 10:
                    client.start();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try (
                Socket socket = new Socket("localhost", 8000);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Client: Client has connected to the server!");
            System.out.println("Valid commands are: \"echo <message>\" to get message echoed back, \"quit\"");
            System.out.println("Please enter a command: ");
            String request = sc.nextLine();

            while (true) {
                out.println(request);

                if (request.startsWith("echo")) {
                    String timeString = in.readLine();  // (blocks) waits for response from server, then input string terminated by a newline character ("\n")
                    System.out.println("Client message: Response from server after \"time\" request: " + timeString);
                }
                else if (request.startsWith("quit")) {
                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }

                request = sc.nextLine();
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e);
        }

        System.out.println("The client is now terminating...");
    }

    // Question 1
    public static void getAllProducts() {
        try {
            List<Product> products = IProductDao.getAllProducts();

            System.out.println("Retrieving all products...");
            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            } else {
                for (Product product : products) {
                    System.out.println("{" + product.toString() + "}");
                }
            }

            System.out.println();
            menu();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 2
    public static void getProductById() {
        try {
            System.out.println("Please enter the id of the product you wish to view:");
            String id = sc.next();

            System.out.println("Finding product with given id...");
            Product product = IProductDao.getProductById(id);

            if (product != null) {
                System.out.println("Product found!\n{" + product.toString() + "}");
            } else {
                System.out.println("No product found with given id!");
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        System.out.println();
        menu();
    }

    // Question 3
    public static void deleteProductById() {
        try {
            System.out.println("Please enter the id of the product you wish to delete:");
            String id = sc.next();

            System.out.println("Deleting product with given id...");
            int rowsAffected = IProductDao.deleteProductById(id);

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted product with id " + id);
            } else {
                System.out.println("An error occurred. Please check if your id exists in the database!");
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        System.out.println();
        menu();
    }

    // Question 4
    public static void addProduct() {
        try {
            System.out.println("Enter product id (e.g. 'product1', 'product2'): ");
            String id = sc.next();

            Product p = IProductDao.getProductById(id);

            if (p == null) {

                int rowsAffected = IProductDao.addProduct(Methods.getProduct(id));

                System.out.println("Adding product...");
                if (rowsAffected > 0) {
                    System.out.println("Product has been successfully added!");
                } else {
                    System.out.println("An error occurred. Product could not be added!");
                }
            } else {
                System.out.println("Product with given id already exists!");
            }

            System.out.println();
            menu();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 5
    public static void updateProduct() {
        try {
            System.out.println("Enter product id you wish to update (e.g. 'product1', 'product2'): ");
            String id = sc.next();

            Product p = IProductDao.getProductById(id);

            if (p != null) {

                int rowsAffected = IProductDao.updateProduct(id, Methods.getProduct(id));

                System.out.println("Updating product with given id...");
                if (rowsAffected > 0) {
                    System.out.println("Product with id " + id + " has been successfully updated");
                } else {
                    System.out.println("Error in updating product. Check if your product id exists in the database!");
                }

            } else {
                System.out.println("Product with given id doesnt exist!");
            }

            System.out.println();
            menu();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 6
    public static void filterProducts() {
        try {
            // Initially get all products first so we can have something to filter
            List<Product> products = IProductDao.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            } else {
                System.out.println("Please enter a price (e.g. 10.00) to filter products below that price");
                double price = sc.nextDouble();

                // Reference: https://stackoverflow.com/questions/66532091/java-8-streams-filter-by-a-property-of-an-object
                List<Product> productsBelowCertainPrice = Methods.filterProductsByPrice(price, products);

                if (productsBelowCertainPrice.isEmpty()) {
                    System.out.println("No product found that is below given price!");
                } else {
                    for (Product product : productsBelowCertainPrice) {
                        System.out.println("{" + product.toString() + "}");
                    }
                }
            }

            System.out.println();
            menu();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 7
    public static void productsListToJsonString(List<Product> list) {
        // Creates JSONArray
        JSONArray jsonArray = new JSONArray();

        // Loops through given list
        for (Product product : list) {

            // Creates JSONObject
            JSONObject jsonObject = new JSONObject();

            // Puts product info in the JSONObject in a 'Key' -> 'Value' format
            jsonObject.put("product_id", product.getId());
            jsonObject.put("product_description", product.getDescription());
            jsonObject.put("size", product.getSize());
            jsonObject.put("unit_price", product.getPrice());
            jsonObject.put("supplier_id", product.getSupplierId());

            // Places newly created JSONObject into JSONArray
            jsonArray.put(jsonObject);

        }

        // Prints JSONArray in JSON format
        System.out.println(jsonArray.toString());
        menu();
    }

    // Question 8
    public static void productToJsonString() {
        try {
            System.out.println("Please enter the id of the product you wish to turn into a JSON:");
            String id = sc.next();

            System.out.println("Finding product with given id...");
            Product product = IProductDao.getProductById(id);

            if (product != null) {
                System.out.println("Product found!\n{" + product.toString() + "}\nTurning found product into a JSON string...");

                // Refer to method productsListToJsonString() for explanation
                JSONObject jsonObject = Methods.turnProductIntoJson(product);

                System.out.println("Product as a JSON string:\n" + jsonObject);
            } else {
                System.out.println("No product found with given id!");
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        System.out.println();
        menu();
    }
}