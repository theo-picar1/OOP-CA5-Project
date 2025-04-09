package com.example.oopca5project;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
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

    private DataInputStream dataInputStream = null;

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
                    JSONArray jsonArray = JunitTestMethods.productsListToJsonString(products);

                    socketWriter.println(jsonArray);
                }
                // DISPLAY ALL SUPPLIERS
                else if(request.equals("2")) {
                    List<Supplier> suppliers = ISupplierDao.getAllSuppliers();
                    JSONArray jsonArray = JunitTestMethods.suppliersListToJsonString(suppliers);

                    socketWriter.println(jsonArray);
                }
                // DISPLAYS ALL CUSTOMERS
                else if(request.equals("3")) {
                    List<Customer> customers = ICustomerDao.getAllCustomers();
                    JSONArray jsonArray = JunitTestMethods.customersListToJsonString(customers);

                    socketWriter.println(jsonArray);
                }
                // FIND PRODUCT BY ID
                else if (request.equals("4")) {
                    Product product;

                    try {
                        product = IProductDao.getProductById(socketReader.readLine());

                        // Turn product into single json object and pass it back as a string to the client
                        JSONObject message = JunitTestMethods.turnProductIntoJson(product);
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
                            product = JunitTestMethods.makeProductFromJSON(jsonObject);
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
                else if(request.equals("6")) {
                    // Get input and output streams on the Socket to send/receive binary data
                    dataInputStream = new DataInputStream(clientSocket.getInputStream());

                    // call function to extract file data from the data input stream and write to file
                    receiveFile("images/my-beautiful-staffordshire-bull-terrier-v0-czmj26cdl58c1-RECEIVED.jpg");
                }
                // ADD CUSTOMER
                else if (request.equals("7")) {
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
                            customer = JunitTestMethods.makeCustomerFromJSON(jsonObject);
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
                // UPDATE PRODUCT
                else if (request.equals("8")) {

                    // initialize variables
                    Product product;
                    int productUpdated;
                    String id = socketReader.readLine();
                    String jsonString = socketReader.readLine();
                    JSONObject jsonObject = new JSONObject(jsonString);

                    // initialize messages
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("status", "error");
                    errorMessage.put("message", "An error occurred!!");

                    JSONObject successMessage = new JSONObject();
                    successMessage.put("status", "success");
                    successMessage.put("message", jsonString);

                    try {
                        // check if Product doesn't exist
                        if (IProductDao.getProductById(id) != null) {

                            // get and update Product to database
                            product = JunitTestMethods.makeProductFromJSON(jsonObject);
                            productUpdated = IProductDao.updateProduct(id, product);

                            // check if Customer was added
                            if (productUpdated == 1) {

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
                // TERMINATE CLIENT AND WAIT FOR NEW ONE
                else if (request.equals("9")) {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
                else if(request.equals("10")) { // DELETE PRODUCT BY ID
                    String id = socketReader.readLine();
                    int rowsAffected;

                    try {
                        if(IProductDao.getProductById(id) != null) {
                            rowsAffected = IProductDao.deleteProductById(id);

                            if(rowsAffected > 0) {
                                socketWriter.println("Product has successfully been deleted");
                                System.out.println("Server: PRODUCT HAS SUCCESSFULLY BEEN DELETED");
                            }
                            else {
                                socketWriter.println("Error: Product was not deleted!");
                                System.out.println("Server: PRODUCT WAS NOT DELETED! CHECK FOR CODE ERRORS!");
                            }
                        }
                        else {
                            socketWriter.println("Product with given id does not exist in the table!");
                            System.out.println("Server: NO PRODUCT FOUND WITH GIVEN ID");
                        }
                    }
                    catch(DaoException e) {
                        e.printStackTrace();
                    }
                }
                else if (request.equals("11")) { // ADD SUPPLIER
                    Supplier supplier;
                    int rowsAffected;
                    String jsonString = socketReader.readLine();
                    JSONObject jsonObject = new JSONObject(jsonString);
                    String id = jsonObject.getString("supplier_id");

                    // Error messages
                    JSONObject errorMessage = new JSONObject();
                    errorMessage.put("status", "Error");
                    errorMessage.put("message", "An error occurred! Check the server terminal for error message");

                    // Success messages
                    JSONObject successMessage = new JSONObject();
                    successMessage.put("status", "success");
                    successMessage.put("message", jsonString);

                    try {
                        // check if Supplier doesn't exist before proceeding
                        if (ISupplierDao.getSupplierById(id) == null) {
                            supplier = JunitTestMethods.makeSupplierFromJSON(jsonObject);
                            rowsAffected = ISupplierDao.addSupplier(supplier);

                            if (rowsAffected > 0) {
                                socketWriter.println(successMessage);

                            }
                            else {
                                socketWriter.println(errorMessage);
                            }
                        } else {
                            socketWriter.println(errorMessage);
                            System.err.println("Server: SUPPLIER ALREADY EXISTS IN THE TABLE WITH PROVIDED ID!");
                        }

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // INVALID COMMAND
                else {
                    socketWriter.println("Error! I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        }
        catch (Exception e) {
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

    private void receiveFile(String fileName) throws Exception
    {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        // DataInputStream allows us to read Java primitive types from stream, such as readLong()
        long numberOfBytesRemaining = dataInputStream.readLong(); // bytes remaining to be read (initially equal to file size)
        System.out.println("Server: file size in bytes = " + numberOfBytesRemaining);

        // create a buffer to receive the incoming image bytes from the socket
        // A buffer is an array that stores a certain amount of data temporarily.
        byte[] buffer = new byte[4 * 1024];

        System.out.println("Server: Bytes remaining to be read from socket: ");
        int numberOfBytesRead;    // number of bytes read from the socket

        // next, read the incoming bytes in chunks (of buffer size) that make up the image file
        while (numberOfBytesRemaining > 0 &&  (numberOfBytesRead = dataInputStream.read(buffer, 0,(int)Math.min(buffer.length, numberOfBytesRemaining))) != -1) {

            // Here we write the buffer data into the local file
            fileOutputStream.write(buffer, 0, numberOfBytesRead);  // write N number of bytes from buffer into file

            // reduce the 'numberOfBytesRemaining' to be read by the number of bytes read in.
            // 'numberOfBytesRemaining' represents the number of bytes remaining to be read from
            // the input stream.
            // We repeat this until all the bytes are dealt with and the remaining size is reduced to zero
            numberOfBytesRemaining = numberOfBytesRemaining - numberOfBytesRead;

            System.out.print(numberOfBytesRemaining + "... ");
        }
        fileOutputStream.close();

        System.out.println("File was received successfully! Look in the images folder to see the transferred file!");

    }
}
