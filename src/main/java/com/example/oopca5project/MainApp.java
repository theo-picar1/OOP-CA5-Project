package com.example.oopca5project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

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
                "Testing server (Find product by ID)",
                "Get Supplier object via Product ID",
                "Display all Suppliers",
                "Display all Customers"
        };

        Methods.menuOptions(options);

        int choice = Methods.validateRange(1, 13);

        try {
            switch (choice) {
                case 1:
                    System.out.println("Ending application. Goodbye!");
                    break;
                case 2:
                    getAllProducts();
                case 3:
                    System.out.println("\nmoved to 10\n");
                    menu();
                case 4:
                    deleteProductById();
                case 5:
                    System.out.println("\nmoved to 10\n");
                    menu();
                case 6:
                    updateProduct();
                case 7:
                    filterProducts();
                case 8:
                    Methods.productsListToJsonString(IProductDao.getAllProducts());
                case 9:
                    productToJsonString();
                case 10:
                    client.start();
                case 11:

                    // Print Supplier object if not Null
                    Methods.printObjectIfNotNull(getSupplier());

                    // call menu again.
                    menu();
                case 12:
                    getAllSuppliers();
                case 13:
                    getAllCustomers();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try (
                Socket socket = new Socket("localhost", 8001);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            Scanner sc = new Scanner(System.in);

            System.out.println("Client: Client has connected to the server!");

            // Initializes variables
            String request;

            while (true) {

                // Outputs all available commands
                System.out.println("\nValid commands are: 1. Display all products, 2. Find product by ID, 3. Add product, 4. Quit");
                System.out.println("Please enter a command: ");

                // asks for user input to choose command
                request = sc.next();

                // checks if '2' was typed
                if (request.equals("2")) {

                    // Asks for product ID
                    System.out.println("Please enter the id of the product you wish to view:");

                    // Gets product ID from user input
                    String id = sc.next();

                    // Adds product ID onto end of '2' so it can be retrieved later by server
                    request += id;
                } else if(request.equals("3")){
                    // Asks for product ID
                    System.out.println("Please enter the id of the product you wish to add:");

                    // Gets product ID from user input
                    String id = sc.next();

                    // Make product
                    Product product = Methods.getProduct(id);

                    // Turn product into JSONObject and put it at end of '3' so it can be retrieved later by server
                    request += Methods.turnProductIntoJson(product);
                }

                // Passes request to server
                out.println(request);

                if (request.equals("1")) {
                    String response;

                    // Because Server is sending back multiple socketWriter statements, we need to use while loop as readLine() does one line at a time.
                    // End the while loop once "Done!" is read.
                    while((response = in.readLine()) != null && !response.equalsIgnoreCase("Done!")) {
                        System.out.println(response);
                    }
                }
                else if (request.startsWith("2")) { // Enters if request was '2'

                    // Reads in response from Server
                    String response = in.readLine();

                    // Checks if response is null
                    if(response != null) {

                        // Makes JSON string passed from Server into a Product object
                        Product product = Methods.makeProductFromJSON(new JSONObject(response));

                        // Prints product object
                        System.out.println("Client message: Response from server: \"" + product + "\"");
                    } else{
                        // Prints out if product wasn't found
                        System.out.println("Product not found");
                    }

                    // Break out of while loop
                    break;

                } else if(request.startsWith("3")){

                    // Reads in response from Server
                    String response = in.readLine();

                    // makes respense into a JSON object
                    JSONObject jsonResponse = new JSONObject(response);

                    // gets message from passed from server
                    String message = jsonResponse.getString("message");

                    // prints message
                    System.out.println("Message from server: " + message);

                    // Break out of while loop
                    break;
                }
                else if(request.equals("4")) {
                    String response = in.readLine();   // wait for response -
                    System.out.println("Client message: Response from server: \"" + response + "\"");
                    break;
                }
                else {
                    System.out.println("Command unknown. Try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e);
        }

        System.out.println("The client is now terminating...");
        menu();
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

    public static Supplier getSupplier(){

        // Asks for Product ID input
        System.out.println("Enter product ID you want to search by");

        // Takes in input
        String ProductId = sc.next();

        // initializes variables
        Supplier supplier = null;

        try {

            // Gets Supplier object from db using product ID
            supplier = ISupplierDao.getSupplierByProductId(ProductId);

        }catch(Exception e){

            // Prints error
            e.printStackTrace();
        }

        // returns Supplier
        return supplier;
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
            menu();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    public static void getAllCustomers() {
        try {
            List<Customer> customers = ICustomerDao.getAllCustomers();

            System.out.println("Retrieving all suppliers...");
            if (customers.isEmpty()) {
                System.out.println("Suppliers table is empty! Please add some data first.");
            } else {
                for (Customer customer : customers) {
                    System.out.println("{" + customer.toString() + "}");
                }
            }

            System.out.println();
            menu();
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }
}