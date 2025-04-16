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
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.oopca5project.DAOs.*;
import com.example.oopca5project.DTOs.*;
import com.example.oopca5project.Exceptions.DaoException;

public class MainApp {

    static ProductDaoInterface IProductDao = new MySqlProductDao();
    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao();
    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao();
    static CustomersProductsDaoInterface ICustomersProductsDao = new MySqlCustomersProductsDao();

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
            "Display all products as JSON",
            "Display product as JSON",
        };

        Methods.menuOptions(options);

        System.out.println("Enter choice: ");

        int choice = Methods.validateRange(1, 5);

        while (choice != 1) {
            try {
                switch (choice) {
                    case 2:
                        client.start(); // Use the server class
                        break;
                    case 3: // DISPLAY ALL PRODUCTS AS JSON ARRAY
                        Product.productsListToJsonString(IProductDao.getAllProducts());
                        break;
                    case 4: // DISPLAY PRODUCT AS JSON CASE
                        productToJsonString();
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
                    "Quit",
                    "Download an image",
                    "Display all Products",
                    "Find product by ID",
                    "Filter products lower than given price",
                    "Add product",
                    "Update existing product by ID",
                    "Delete product by ID",
                    "Display all Suppliers",
                    "Find supplier by ID -> NOT IMPLEMENTED",
                    "Find supplier by ProductID",
                    "Add supplier",
                    "Update existing supplier by ID", 
                    "Delete supplier by ID",
                    "Display all Customers",
                    "Find customer by ID -> NOT IMPLEMENTED",
                    "Add customer", 
                    "Update existing customer by ID",
                    "Delete customer by ID -> NOT IMPLEMENTED",
                    "Display all customer's products",
                    "Find customer's product by product and customer ID -> NOT IMPLEMENTED",
                    "Add customer product",
                    "Update existing customer's product by product and customer ID",
                    "Delete customer's product by product and customer ID -> NOT IMPLEMENTED",
                };

                System.out.println("***** CLIENT / SERVER APPLICATION *****");
                Methods.menuOptions(options);

                System.out.println("Please enter a command: ");
                request = sc.next();
                sc.nextLine();

                // Passes the user's request to the server
                out.println(request);

                // ******************* OTHER OPTIONS *******************
                // QUIT APPLICATION
                if (request.equals("1")) {
                    String response = in.readLine();
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;
                }
                // DOWNLOAD IMAGE
                else if (request.equals("2")) {
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    System.out.println("Sending the chosen file to the Server");
                    // Call the method responsible for handling sending the file to the server
                    sendFile("images/my-beautiful-staffordshire-bull-terrier-v0-czmj26cdl58c1.jpg", dataOutputStream);

                    dataOutputStream.close();
                }
                
                // ******************** PRODUCT OPTIONS ********************
                
                // DISPLAY ALL PRODUCTS
                else if (request.equals("3")) {
                    // Reads in response from server. Same with all else statements
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Product> list = Product.makeProductListFromJSONArray(jsonArray); // Turn the jsonArray into a list of products
                    Methods.printListOfObjects(list); // Then print it
                }
                // FIND PRODUCT BY ID
                else if (request.equals("4")) {
                    System.out.println("Please enter the id of the product you wish to view:");
                    String id = sc.next();

                    out.println(id);
                    String response = in.readLine();

                    if (response != null) {
                        // Makes JSON string passed from Server into a Product object
                        Product product = Product.makeProductFromJSON(new JSONObject(response));
                        System.out.println("Client message: Response from server: \"" + product + "\"");
                    } else {
                        System.out.println("Product not found");
                    }
                }
                // FILTER PRODUCTS LOWER THAN PRICE
                else if(request.equals("5")) {
                    System.out.println("Please enter the price to filter products by:");
                    double price = sc.nextDouble();
                    out.println(String.valueOf(price));

                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    System.out.println("Products below €" +price+ ":");
                    ArrayList<Product> list = Product.makeProductListFromJSONArray(jsonArray); // Turn the jsonArray into a list of products
                    Methods.printListOfObjects(list);
                }
                // ADD PRODUCT
                else if (request.equals("6")) {
                    System.out.println("Please enter the id of the product you wish to add:");
                    String id = sc.next();

                    Product product = Product.createProduct(id);
                    JSONObject jsonObject = Product.turnProductIntoJson(product);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                // UPDATE EXISTING PRODUCT BY ID
                else if (request.equals("7")) {
                    // Get ID of Product to update and send ID to server
                    System.out.println("Please enter the id of the product you wish to update:");
                    String id = sc.next();
                    out.println(id);

                    // Get Product object, turn it into a JSON string, and send to server
                    Product product = Product.createProduct(id);
                    JSONObject jsonObject = Product.turnProductIntoJson(product);
                    out.println(jsonObject);

                    // Take in response from server
                    String response = in.readLine();

                    // Get message from JSONObject and print 
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);

                }
                // DELETE PRODUCT BY ID
                else if (request.equals("8")) {
                    System.out.println("Please enter the id of the product you wish to delete");
                    String id = sc.next();
                    out.println(id);

                    String response = in.readLine();
                    System.out.println("Client: RESPONSE FROM SERVER: '" +response+ "'");
                }
                
                // ********************* SUPPLIER OPTIONS ***********************
                
                // DISPLAY ALL SUPPLIERS
                else if (request.equals("9")) {
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Supplier> list = Supplier.makeSupplierListFromJSONArray(jsonArray);
                    Methods.printListOfObjects(list);
                }
                // DISPLAY SUPPLIER BY ID
                else if (request.equals("10")) {
                    System.out.println("NOT IMPLEMENTED");
                }
                // DISPLAY SUPPLIER BY PRODUCT ID
                else if(request.equals("11")) {

                    System.out.println("Enter product ID you want to search by");
                    String id = sc.next();
                    out.println(id);

                    String response = in.readLine();

                    if (response != null) {
                        // Makes JSON string passed from Server into a Product object
                        Supplier supplier = Supplier.makeSupplierFromJSON(new JSONObject(response));
                        System.out.println("Client message: Response from server: \"" + supplier + "\"");
                    } else {
                        System.out.println("Supplier not found");
                    }
                }
                // ADD SUPPLIER
                else if(request.equals("12")) {
                    System.out.println("Please enter the id of the supplier you wish to add:");
                    String id = sc.next();

                    Supplier supplier = Supplier.createSupplier(id);
                    JSONObject jsonObject = Supplier.turnSupplierIntoJson(supplier);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                // UPDATE SUPPLIER
                else if(request.equals("13")) {
                    System.out.println("Please enter the id of the supplier you wish to update:");
                    String id = sc.next();
                    out.println(id);

                    Supplier supplier = Supplier.createSupplier(id);
                    JSONObject jsonObject = Supplier.turnSupplierIntoJson(supplier);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                // DELETE SUPPLIER BY ID
                else if(request.equals("14")) {
                    System.out.println("Please enter the id of the supplier you wish to delete:");
                    String id = sc.next();
                    out.println(id);

                    String response = in.readLine();
                    System.out.println("Client: RESPONSE FROM SERVER '" +response+ "'");
                }
                
                // ******************* CUSTOMER OPTIONS *******************
                
                // DISPLAY ALL CUSTOMERS
                else if (request.equals("15")) {
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Customer> list = Customer.makeCustomerListFromJSONArray(jsonArray);
                    Methods.printListOfObjects(list);
                    System.out.println();
                }
                // FIND CUSTOMER BY ID
                else if(request.equals("16")) {
                    System.out.println("NOT IMPLEMENTED\n");
                }
                // ADD CUSTOMER
                else if (request.equals("17")) {
                    // Get Customer object, turn it into a JSON string, and send to server
                    Customer customer = Customer.createCustomer();
                    JSONObject jsonObject = Customer.turnCustomerIntoJson(customer);
                    out.println(jsonObject);

                    // Take in response from server
                    String response = in.readLine();

                    // Get message from JSONObject and print
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message + "\n");

                }
                // UPDATE EXISTING CUSTOMER BY ID
                else if(request.equals("18")) {
                    System.out.println("Please enter the id of the customer you wish to update:");
                    int id = sc.nextInt();  
                    sc.nextLine();

                    Customer customer = Customer.createCustomer();
                    JSONObject jsonObject = Customer.turnCustomerIntoJson(customer);
                    out.println(id);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message +"\n");
                }
                // DELETE CUSTOMER 
                else if(request.equals("19")) {
                    System.out.println("NOT IMPLEMENTED\n");
                }
                
                // ******************* CUSTOMER PRODUCTS OPTIONS *******************

                // DISPLAY ALL CUSTOMER'S PRODUCTS
                else if(request.equals("20")) {
                    System.out.println("NOT IMPLEMENTED\n");
                }
                // FIND CUSTOMER'S PRODUCT BY PRODUCT ID AND CUSTOMER ID
                else if(request.equals("21")) {
                    System.out.println("NOT IMPLEMENTED\n");
                }
                // ADD CUSTOMER PRODUCT
                else if (request.equals("22")) {
                    System.out.println("Please enter the Product id of the customers products you wish to Add:");
                    String productId = sc.next();
                    out.println(productId);

                    System.out.println("Please enter the corresponding customer id of the customers products you wish to update:");
                    int customerId = sc.nextInt();
                    sc.nextLine();
                    out.println(customerId);

                    // Get Customer Product object, turn it into a JSON string, and send to server
                    CustomersProducts CustomersP = CustomersProducts.createCustomersProducts(customerId, productId);
                    JSONObject jsonObject = CustomersProducts.turnCustomersProductsIntoJson(CustomersP);
                    out.println(jsonObject);

                    // Take in response from server
                    String response = in.readLine();

                    // Get message from JSONObject and print
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message + "\n");
                }
                // UPDATE CUSTOMERS PRODUCTS BY PRODUCT ID AND CUSTOMER ID
                else if(request.equals("23")) {
                    System.out.println("Please enter the Product id of the customers products you wish to update:");
                    String productId = sc.next();
                    out.println(productId);

                    System.out.println("Please enter the corresponding customer id of the customers products you wish to update:");
                    int customerId = sc.nextInt();
                    sc.nextLine();
                    out.println(customerId);

                    CustomersProducts CustomersP = CustomersProducts.createCustomersProducts(customerId, productId);
                    JSONObject jsonObject = CustomersProducts.turnCustomersProductsIntoJson(CustomersP);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message +"\n");
                }
                // DELETE CUSTOMER'S PRODUCT BY CUSTOMER AND PRODUCT ID 
                else if(request.equals("24")) {
                    System.out.println("NOT IMPLEMENTED\n");
                }
                // UNKNOWN COMMAND
                else {
                    System.out.println("Command unknown. Try again.\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("The client is now terminating...");
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
                JSONObject jsonObject = Product.turnProductIntoJson(product);

                System.out.println("Product as a JSON string:\n" + jsonObject);
            }
            else {
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

    // public static Supplier getSupplier() {

    //     // Asks for Product ID input
    //     System.out.println("Enter product ID you want to search by");

    //     // Takes in input
    //     String ProductId = sc.next();

    //     // initializes variables
    //     Supplier supplier = null;

    //     try {

    //         // Gets Supplier object from db using product ID
    //         supplier = ISupplierDao.getSupplierByProductId(ProductId);

    //     } catch (Exception e) {

    //         // Prints error
    //         e.printStackTrace();
    //     }

    //     // returns Supplier
    //     return supplier;
    // }
}
