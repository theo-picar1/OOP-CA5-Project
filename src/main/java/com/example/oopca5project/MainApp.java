package com.example.oopca5project;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.oopca5project.DAOs.CustomerDaoInterface;
import com.example.oopca5project.DAOs.MySqlCustomerDao;
import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.MySqlSupplierDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DAOs.SupplierDaoInterface;
import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;

public class MainApp {

    static ProductDaoInterface IProductDao = new MySqlProductDao();
    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao();
    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("***** OOP-CA5 PROJECT *****");

        menu();
    }

    public static void menu() {
        MainApp client = new MainApp();
        String[] options = {
            "End application",
            "Start Server",
            "Filter products",
            "Display all products as JSON",
            "Display product as JSON",
        };

        Methods.menuOptions(options);

        System.out.println("Enter choice: ");

        int choice = Methods.validateRange(1, 6);

        while (choice != 1) {
            try {
                switch (choice) {
                    case 2:
                        client.start(); // Use the server class
                        break;
                    case 3:
                        filterProducts();
                        break;
                    case 4: // DISPLAY ALL PRODUCTS AS JSON ARRAY
                        JunitTestMethods.productsListToJsonString(IProductDao.getAllProducts());
                        break;
                    case 5: // DISPLAY PRODUCT AS JSON CASE
                        productToJsonString();
                        break;
                    case 6: // GET SUPPLIER THROUGH PRODUCT ID
                        Methods.printObject(getSupplier());
                        break;
                    default:
                        System.out.println("Invalid option, please try again!");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            System.out.println("***** CONSOLE APPLICATION *****");
            Methods.menuOptions(options);
            choice = sc.nextInt();
        }
    }

    public void start() {
        try (
                Socket socket = new Socket("localhost", 9201);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Client: Client has connected to the server!");

            // Initializes variables
            String request;

            while (true) {
                String[] options = {
                    "Display all Products",
                    "Display all Suppliers",
                    "Display all Customers",
                    "Find product by ID",
                    "Add product",
                    "Choose image to display",
                    "Add customer",
                    "Update product by ID",
                    "Quit",
                    "Delete product by ID",
                    "Add supplier",
                    "Update supplier by ID",
                    "Display supplier by ProductID",
                };

                System.out.println("***** CLIENT / SERVER APPLICATION *****");
                Methods.menuOptions(options);

                System.out.println("Please enter a command: ");
                request = sc.next();
                sc.nextLine();

                // Passes the user's request to the server
                out.println(request);

                // DISPLAY ALL PRODUCTS
                if (request.equals("1")) {
                    // Reads in response from server. Same with all else statements
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Product> list = JunitTestMethods.makeProductListFromJSONArray(jsonArray); // Turn the jsonArray into a list of products
                    Methods.printListOfObjects(list); // Then print it
                } 
                // DISPLAY ALL SUPPLIERS
                else if (request.equals("2")) {
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Supplier> list = JunitTestMethods.makeSupplierListFromJSONArray(jsonArray);
                    Methods.printListOfObjects(list);
                } 
                // DISPLAY ALL CUSTOMERS
                else if (request.equals("3")) {
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Customer> list = JunitTestMethods.makeCustomerListFromJSONArray(jsonArray);
                    Methods.printListOfObjects(list);
                } else if (request.equals("4")) {
                    System.out.println("Please enter the id of the product you wish to view:");
                    String id = sc.next();

                    out.println(id);
                    String response = in.readLine();

                    if (response != null) {
                        // Makes JSON string passed from Server into a Product object
                        Product product = JunitTestMethods.makeProductFromJSON(new JSONObject(response));
                        System.out.println("Client message: Response from server: \"" + product + "\"");
                    } else {
                        System.out.println("Product not found");
                    }
                } else if (request.equals("5")) { // ADD PRODUCT
                    System.out.println("Please enter the id of the product you wish to add:");
                    String id = sc.next();

                    Product product = Methods.getProduct(id);
                    JSONObject jsonObject = JunitTestMethods.turnProductIntoJson(product);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                } else if (request.equals("6")) {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    System.out.println("Sending the chosen file to the Server");
                    // Call the method responsible for handling sending the file to the server
                    sendFile("images/my-beautiful-staffordshire-bull-terrier-v0-czmj26cdl58c1.jpg", dataOutputStream);

                    dataOutputStream.close();
                } else if (request.equals("7")) {

                    // Get Customer object, turn it into a JSON string, and send to server
                    Customer customer = Methods.getCustomer();
                    JSONObject jsonObject = JunitTestMethods.turnCustomerIntoJson(customer);
                    out.println(jsonObject);

                    // Take in response from server
                    String response = in.readLine();

                    // Get message from JSONObject and print
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);

                } else if (request.equals("8")) {
                    // Get ID of Product to update and send ID to server
                    System.out.println("Please enter the id of the product you wish to update:");
                    String id = sc.next();
                    out.println(id);

                    // Get Product object, turn it into a JSON string, and send to server
                    Product product = Methods.getProduct(id);
                    JSONObject jsonObject = JunitTestMethods.turnProductIntoJson(product);
                    out.println(jsonObject);

                    // Take in response from server
                    String response = in.readLine();

                    // Get message from JSONObject and print 
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);

                } else if (request.equals("9")) { // QUIT APPLICATION
                    String response = in.readLine();
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;
                }
                else if (request.equals("10")) { // DELETE PRODUCT BY ID
                    System.out.println("Please enter the id of the product you wish to delete");
                    String id = sc.next();
                    out.println(id);

                    String response = in.readLine();
                    System.out.println("Client: RESPONSE FROM SERVER: '" +response+ "'");
                }
                else if(request.equals("11")) { // ADD SUPPLIER
                    System.out.println("Please enter the id of the supplier you wish to add:");
                    String id = sc.next();

                    Supplier supplier = Methods.createSupplier(id);
                    JSONObject jsonObject = JunitTestMethods.turnSupplierIntoJson(supplier);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }else if(request.equals("12")) { // UPDATE SUPPLIER
                    System.out.println("Please enter the id of the supplier you wish to update:");
                    String id = sc.next();
                    out.println(id);

                    Supplier supplier = Methods.createSupplier(id);
                    JSONObject jsonObject = JunitTestMethods.turnSupplierIntoJson(supplier);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                // DISPLAY SUPPLIER BY PRODUCT ID
                else if(request.equals("13")) {

                    System.out.println("Enter product ID you want to search by");
                    String id = sc.next();
                    out.println(id);

                    String response = in.readLine();

                    if (response != null) {
                        // Makes JSON string passed from Server into a Product object
                        Supplier supplier = JunitTestMethods.makeSupplierFromJSON(new JSONObject(response));
                        System.out.println("Client message: Response from server: \"" + supplier + "\"");
                    } else {
                        System.out.println("Supplier not found");
                    }
                }
                // UPDATE CUSTOMER
                else if(request.equals("14")) {
                    System.out.println("Please enter the id of the customer you wish to update:");
                    int id = sc.nextInt();  
                    sc.nextLine();

                    Customer customer = Methods.getCustomer();
                    JSONObject jsonObject = JunitTestMethods.turnCustomerIntoJson(customer);
                    out.println(id);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("The client is now terminating...");
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

    }

    public static void addSupplier() {
        try {
            System.out.println("Please enter the id: (e.g. SupplierX)");
            String id = sc.next();

            System.out.println("Please enter the name of your supplier:");
            String name = sc.next();

            System.out.println("Please enter the Irish telephone number of your supplier:");
            String phone = sc.next();

            System.out.println("Please enter the email of your supplier:");
            String email = sc.next();

            Supplier supplier = new Supplier(id, name, phone, email);

            int rowsAffected = ISupplierDao.addSupplier(supplier);

            if (rowsAffected >= 1) {
                System.out.println("Successfully added supplier");
            } else {
                System.err.println("Error! Supplier was not added");
            }
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
                List<Product> productsBelowCertainPrice = JunitTestMethods.filterProductsByPrice(price, products);

                if (productsBelowCertainPrice.isEmpty()) {
                    System.out.println("No product found that is below given price!");
                } else {
                    for (Product product : productsBelowCertainPrice) {
                        System.out.println("{" + product.toString() + "}");
                    }
                }
            }

            System.out.println();

        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 8
    public static void productToJsonString() {
        try {
            System.out.println("Please enter the id of the product you wish to turn into a JSON:");
            String id = sc.next();

            System.out.println("Finding product with given id...");
            Product product = IProductDao.getProductById(id);

            if (product != null) {
                System.out.println("Product found!\n{" + product + "}\nTurning found product into a JSON string...");

                // Refer to method productsListToJsonString() for explanation
                JSONObject jsonObject = JunitTestMethods.turnProductIntoJson(product);

                System.out.println("Product as a JSON string:\n" + jsonObject);
            } else {
                System.out.println("No product found with given id!");
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        System.out.println();

    }

    private void sendFile(String fileName, DataOutputStream dataOutputStream) throws Exception {
        int numberOfBytes;
        // Open the File at the specified location (path)
        File file = new File(fileName);
        FileInputStream fileInputStream = new FileInputStream(file);

        // Send the length of the file in bytes to the server as a "long"
        dataOutputStream.writeLong(file.length());

        // Here we break file into chunks by using a buffer
        byte[] buffer = new byte[4 * 1024];

        // read bytes from file into the buffer until buffer is full, or until we have reached the end of the image file
        while ((numberOfBytes = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, numberOfBytes);
            dataOutputStream.flush();
        }

        fileInputStream.close();
    }

    public static Supplier getSupplier() {

        // Asks for Product ID input
        System.out.println("Enter product ID you want to search by");

        // Takes in input
        String ProductId = sc.next();

        // initializes variables
        Supplier supplier = null;

        try {

            // Gets Supplier object from db using product ID
            supplier = ISupplierDao.getSupplierByProductId(ProductId);

        } catch (Exception e) {

            // Prints error
            e.printStackTrace();
        }

        // returns Supplier
        return supplier;
    }
}
