package com.example.oopca5project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;
import static com.example.oopca5project.MainApp.ICustomerDao;
import static com.example.oopca5project.MainApp.IProductDao;
import static com.example.oopca5project.MainApp.ISupplierDao;

public class Server {

    final int SERVER_PORT = 9201;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server: Server has started!");

            int clientNumber = 0;

            while (true) {
                System.out.println("Server: Waiting for any connections on port " + SERVER_PORT + "...");
                clientSocket = serverSocket.accept();
                clientNumber++;

                System.out.println("Server: Client " + clientNumber + " has connected with port number " + clientSocket.getPort());
                System.out.println("Server: Port number " + clientSocket.getLocalPort() + " is currently used to talk with the client");

                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber));
                t.start();

                System.out.println("Server: ClientHandler started in thread " + t.getName() + " for client " + clientNumber + ".");
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        System.out.println("Server: Server exiting, Goodbye!");
    }
}

class ClientHandler implements Runnable {

    BufferedReader socketReader;
    PrintWriter socketWriter;
    Socket clientSocket;
    final int clientNumber;

    public ClientHandler(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;

        try {
            this.socketWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            this.socketReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String request;

        try {
            while ((request = socketReader.readLine()) != null) {
                System.out.println("Server: (ClientHandler): Read command from client " + clientNumber + ": " + request);

                // DISPLAY ALL PRODUCTS
                if (request.equals("1")) {
                    // Get all objects from database, convert them to a jsonArray, then pass it back to the client.
                    List<Product> products = IProductDao.getAllProducts();
                    JSONArray jsonArray = DaoMethods.productsListToJsonString(products);

                    socketWriter.println(jsonArray);
                }
                // DISPLAY ALL SUPPLIERS
                else if(request.equals("2")) {
                    List<Supplier> suppliers = ISupplierDao.getAllSuppliers();
                    JSONArray jsonArray = DaoMethods.suppliersListToJsonString(suppliers);

                    socketWriter.println(jsonArray);
                }
                // DISPLAYS ALL CUSTOMERS
                else if(request.equals("3")) {
                    List<Customer> customers = ICustomerDao.getAllCustomers();
                    JSONArray jsonArray = DaoMethods.customersListToJsonString(customers);

                    socketWriter.println(jsonArray);
                }
                // FIND PRODUCT BY ID
                else if (request.equals("4")) {
                    Product product;

                    try {
                        product = IProductDao.getProductById(socketReader.readLine());

                        // Turn product into single json object and pass it back as a string to the client
                        JSONObject message = DaoMethods.turnProductIntoJson(product);
                        socketWriter.println(message.toString());

                        socketWriter.println("Server message: ID has been passed to server passing back product");
                    }
                    catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // ADD PRODUCT
                else if (request.equals("5")) {

                    // initialize variables
                    Product product;
                    int productAdded;
                    String jsonString = socketReader.readLine();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String id = jsonObject.getString("product_id");

                    // initialize messages
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("status", "error");
                    errorMessage.put("message", "An error occurred!!");

                    JSONObject successMessage = new JSONObject();
                    successMessage.put("status", "success");
                    successMessage.put("message", jsonString);

                    try {
                        // check if product doesn't exist
                        if (IProductDao.getProductById(id) == null) {

                            // get and add Product to database
                            product = DaoMethods.makeProductFromJSON(jsonObject);
                            productAdded = IProductDao.addProduct(product);

                            // check if product was added
                            if (productAdded == 1) {

                                socketWriter.println(successMessage);

                            } else {

                                socketWriter.println(errorMessage);
                            }
                        } else {

                            socketWriter.println(errorMessage);
                        }

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // ADD CUSTOMER
                else if (request.equals("6")) {

                    // initialize variables
                    Customer customer;
                    int customerAdded;
                    String jsonString = socketReader.readLine();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    int id = jsonObject.getInt("customer_id");

                    // initialize messages
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("status", "error");
                    errorMessage.put("message", "An error occurred!!");

                    JSONObject successMessage = new JSONObject();
                    successMessage.put("status", "success");
                    successMessage.put("message", jsonString);

                    try {
                        // check if Customer doesn't exist
                        if (ICustomerDao.getCustomerById(id) == null) {

                            // get and add Customer to database
                            customer = DaoMethods.makeCustomerFromJSON(jsonObject);
                            customerAdded = ICustomerDao.addCustomer(customer);

                            // check if Customer was added
                            if (customerAdded == 1) {

                                socketWriter.println(successMessage);

                            } else {

                                socketWriter.println(errorMessage);
                            }
                        } else {

                            socketWriter.println(errorMessage);
                        }

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // QUIT CLIENT / SERVER APPLICATION AND RETURN TO CONSOLE APPLICATION
                else if (request.equals("7")) {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
                // INVALID COMMAND
                else {
                    socketWriter.println("Error! I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        }
        catch (IOException | DaoException e) {
            e.printStackTrace();
        }
        finally {
            this.socketWriter.close();

            try {
                this.socketReader.close();
                this.clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
    }
}
