package com.example.oopca5project;

import java.io.BufferedReader;
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
                "Delete product by ID",
                "Update product by ID",
                "Filter products",
                "Display all products as JSON",
                "Display product as JSON",
                "Display supplier by ProductID",
                "Add supplier"
        };

        Methods.menuOptions(options);

        System.out.println("Enter choice: ");

        int choice = Methods.validateRange(1, 14);

        while (choice != 1) {
            try {
                switch (choice) {
                    case 2:
                        client.start(); // Use the server class
                        break;
                    case 3:
                        deleteProductById();
                        break;
                    case 4:
                        updateProduct();
                        break;
                    case 5:
                        filterProducts();
                        break;
                    case 6: // DISPLAY ALL PRODUCTS AS JSON ARRAY
                        DaoMethods.productsListToJsonString(IProductDao.getAllProducts());
                        break;
                    case 7: // DISPLAY PRODUCT AS JSON CASE
                        productToJsonString();
                        break;
                    case 8: // GET SUPPLIER THROUGH PRODUCT ID
                        Methods.printObject(getSupplier());
                        break;
                    case 9: // ADD SUPPLIER CASE
                        System.out.println("Please enter the id: (e.g. SupplierX)");
                        String id = sc.next();

                        System.out.println("Please enter the name of your supplier:");
                        String name = sc.next();

                        System.out.println("Please enter the Irish telephone number of your supplier:");
                        String phone = sc.next();

                        System.out.println("Please enter the email of your supplier:");
                        String email = sc.next();

                        Supplier supplier = new Supplier(id, name, phone, email);

                        DaoMethods.addSupplier(supplier);

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
                        "Add customer",
                        "Quit",
                };

                System.out.println("***** CLIENT / SERVER APPLICATION *****");
                Methods.menuOptions(options);

                System.out.println("Please enter a command: ");
                request = sc.next();

                // Passes the user's request to the server
                out.println(request);

                // DISPLAY ALL PRODUCTS
                if (request.equals("1")) {
                    // Reads in response from server. Same with all else statements
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Product> list = DaoMethods.makeProductListFromJSONArray(jsonArray); // Turn the jsonArray into a list of products
                    DaoMethods.printListOfObjects(list); // Then print it
                }
                // DISPLAY ALL SUPPLIERS
                else if(request.equals("2")){
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Supplier> list = DaoMethods.makeSupplierListFromJSONArray(jsonArray);
                    DaoMethods.printListOfObjects(list);
                }
                // DISPLAY ALL CUSTOMERS
                else if(request.equals("3")){
                    String response = in.readLine();
                    JSONArray jsonArray = new JSONArray(response);

                    ArrayList<Customer> list = DaoMethods.makeCustomerListFromJSONArray(jsonArray);
                    DaoMethods.printListOfObjects(list);
                }
                else if (request.equals("4")) {
                    System.out.println("Please enter the id of the product you wish to view:");
                    String id = sc.next();

                    out.println(id);
                    String response = in.readLine();

                    if (response != null) {
                        // Makes JSON string passed from Server into a Product object
                        Product product = DaoMethods.makeProductFromJSON(new JSONObject(response));
                        System.out.println("Client message: Response from server: \"" + product + "\"");
                    }
                    else {
                        System.out.println("Product not found");
                    }
                }
                else if (request.equals("5")) {
                    System.out.println("Please enter the id of the product you wish to add:");
                    String id = sc.next();

                    Product product = Methods.getProduct(id);
                    JSONObject jsonObject = DaoMethods.turnProductIntoJson(product);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                else if (request.equals("6")) {
                    Customer customer = Methods.getCustomer();
                    JSONObject jsonObject = DaoMethods.turnCustomerIntoJson(customer);
                    out.println(jsonObject);

                    String response = in.readLine();

                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");
                    System.out.println("Message from server: " + message);
                }
                else if (request.equals("7")) {
                    String response = in.readLine();
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

    // Question 5
    public static void updateProduct() {
        try {
            System.out.println("Enter product id you wish to update (e.g. 'product1', 'product2'): ");
            String id = sc.next();

            Product product = IProductDao.getProductById(id);

            if (product != null) {
                product = Methods.getProduct(product.getId());

                int rowsAffected = IProductDao.updateProduct(id, product);

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
                List<Product> productsBelowCertainPrice = DaoMethods.filterProductsByPrice(price, products);

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
                JSONObject jsonObject = DaoMethods.turnProductIntoJson(product);

                System.out.println("Product as a JSON string:\n" + jsonObject);
            } else {
                System.out.println("No product found with given id!");
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }

        System.out.println();

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