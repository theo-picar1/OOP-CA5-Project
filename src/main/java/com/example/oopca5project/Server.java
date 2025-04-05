package com.example.oopca5project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Supplier;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;
import static com.example.oopca5project.MainApp.IProductDao;
import static com.example.oopca5project.MainApp.ISupplierDao;
import static com.example.oopca5project.MainApp.ICustomerDao;

public class Server {

    final int SERVER_PORT = 8001;

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

                if (request.equals("1")) {

                    // Get all Product objects from table
                    List<Product> products = IProductDao.getAllProducts();

                    // Turn Product objects into JSONArray
                    JSONArray jsonArray = DaoMethods.productsListToJsonString(products);

                    // Pass jsonArray to Client
                    socketWriter.println(jsonArray);

                } else if (request.equals("4")) { // enters if '2' is typed

                    // Initialize MySqlProductDao object to use Dao methods
                    ProductDaoInterface getProduct = new MySqlProductDao();

                    // initialize new product object
                    Product product = new Product();
                    try {

                        // Get product ID that was added onto the end of '2' and get Product JSONObject
                        product = getProduct.getProductById(socketReader.readLine());

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }

                    // Use retrieved Product and turn it into a JSON object
                    JSONObject message = DaoMethods.turnProductIntoJson(product);

                    // Send product object to Client
                    socketWriter.println(message.toString());

                    // Send confirmation message to Client
                    socketWriter.println("Server message: ID has been passed to server passing back product");
                }
                else if (request.equals("6")) {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
                else if (request.equals("5")) {

                    // Initialize MySqlProductDao object to use Dao methods
                    ProductDaoInterface addProduct = new MySqlProductDao();

                    // initialize all variables
                    Product product;
                    int productAdded;
                    String jsonString = socketReader.readLine();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String id = jsonObject.getString("product_id");

                    // make success and error messages
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("status", "error");
                    errorMessage.put("message", "An error occurred!!");

                    JSONObject successMessage = new JSONObject();
                    successMessage.put("status", "success");
                    successMessage.put("message", jsonString);

                    try {
                        // check if product already exists in database
                        product = addProduct.getProductById(id);

                         // check if product doesn't exist
                        if (product == null) {
                           
                            // get product object from JSON
                            product = DaoMethods.makeProductFromJSON(jsonObject);

                            // add product to database
                            productAdded = addProduct.addProduct(product);

                            // check if product was added
                            if (productAdded == 1) {

                                // send success message
                                socketWriter.println(successMessage);

                            } else {

                                // send error message
                                socketWriter.println(errorMessage);
                            }
                        } else {

                            // send error message
                            socketWriter.println(errorMessage);
                        }

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                } else if(request.equals("2")) {

                    // Get all supplier objects from table
                    List<Supplier> suppliers = ISupplierDao.getAllSuppliers();

                    // Turn supplier objects into JSONArray
                    JSONArray jsonArray = DaoMethods.suppliersListToJsonString(suppliers);

                    // Pass jsonArray to Client
                    socketWriter.println(jsonArray);

                } else if(request.equals("3")){
                    // Get all Customer objects from table
                    List<Customer> customers = ICustomerDao.getAllCustomers();

                    // Turn Customer objects into JSONArray
                    JSONArray jsonArray = DaoMethods.customersListToJsonString(customers);

                    // Pass jsonArray to Client
                    socketWriter.println(jsonArray);
                }else {
                    socketWriter.println("Error! I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        } catch (IOException | DaoException e) {
            e.printStackTrace();
        } finally {
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
