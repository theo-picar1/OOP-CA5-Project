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

import com.example.oopca5project.DTOs.*;
import com.example.oopca5project.Exceptions.DaoException;
import static com.example.oopca5project.MainApp.ICustomerDao;
import static com.example.oopca5project.MainApp.ICustomersProductsDao;
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
            System.out.println("SERVER HAS SUCCESSFULLY STARTED");

            int clientNumber = 0;

            while (true) {
                System.out.println("WAITING FOR ANY CONNECTIONS ON PORT " + SERVER_PORT + "...");
                clientSocket = serverSocket.accept();
                clientNumber++;

                System.out.println("\nCLIENT " + clientNumber + " HAS CONNECTED WITH PORT NUMBER " + clientSocket.getPort());
                System.out.println("PORT " + clientSocket.getLocalPort() + " IS BEING USED TO COMMUNICATE WITH THE CLIENT");

                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber));
                t.start();

                System.out.println("CLIENTHANDLER STARTED IN THREAD " + t.getName() + " FOR CLIENT " + clientNumber + ".\n");
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

        System.out.println("THE SERVER IS ENDING... DONE! GOODBYE.");
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
                System.out.println("READ COMMAND FROM CLIENT " + clientNumber + ": " + request);

                // ******************** OTHER OPTIONS ********************

                // TERMINATE CLIENT AND WAIT FOR NEW ONE
                if (request.equals("1")) {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("CLIENT HAS NOTIFIED THAT IT IS QUITTING...");
                }
                // DOWNLOAD IMAGE
                else if(request.equals("2")) {
                    // Get input and output streams on the Socket to send/receive binary data
                    dataInputStream = new DataInputStream(clientSocket.getInputStream());

                    // call function to extract file data from the data input stream and write to file
                    receiveFile("images/my-beautiful-staffordshire-bull-terrier-v0-czmj26cdl58c1-RECEIVED.jpg");
                }

                // ******************** PRODUCT OPTIONS ********************

                // DISPLAY ALL PRODUCTS
                if (request.equals("3")) {
                    // Get all objects from database, convert them to a jsonArray, then pass it back to the client.
                    List<Product> products = IProductDao.getAllProducts();
                    JSONArray jsonArray = Product.productsListToJsonString(products);

                    socketWriter.println(jsonArray);
                }
                // FIND PRODUCT BY ID
                else if (request.equals("4")) {
                    Product product;

                    try {
                        product = IProductDao.getProductById(socketReader.readLine());

                        // Turn product into single json object and pass it back as a string to the client
                        JSONObject message = Product.turnProductIntoJson(product);
                        socketWriter.println(message.toString());

                        socketWriter.println("Server message: ID has been passed to server passing back product");
                    }
                    catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // FILTER PRODUCTS LOWER THAN GIVEN PRICE
                else if(request.equals("5")) {
                    try {
                        // Initially get all products first so we can have something to filter
                        List<Product> products = IProductDao.getAllProducts();

                        if (products.isEmpty()) {
                            socketWriter.println("I'm sorry, this request could not be done. Please add some data to the Products table first");
                            System.out.println("PRODUCT TABLE IS EMPTY. ADD DATA BEFORE FILTERING");
                        }
                        else {
                            String price = socketReader.readLine();

                            // Reference: https://stackoverflow.com/questions/66532091/java-8-streams-filter-by-a-property-of-an-object
                            List<Product> productsBelowCertainPrice = Product.filterProductsByPrice(Double.parseDouble(price), products);

                            if (productsBelowCertainPrice.isEmpty()) {
                                socketWriter.println("No products below the given price: €" +price);
                                System.out.println("NO PRODUCTS THAT MATCHED FILTER");
                            }
                            else {
                                JSONArray jsonArray = Product.productsListToJsonString(productsBelowCertainPrice);
                                socketWriter.println(jsonArray);
                                System.out.println("MATCHING PRODUCTS HAVE BEEN SENT TO THE CLIENT!");
                            }
                        }

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // ADD PRODUCT
                else if (request.equals("6")) {

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
                            product = Product.makeProductFromJSON(jsonObject);
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
                // UPDATE PRODUCT
                else if (request.equals("7")) {

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
                            product = Product.makeProductFromJSON(jsonObject);
                            productUpdated = IProductDao.updateProduct(id, product);

                            // check if Product was added
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
                // DELETE PRODUCT BY ID
                else if(request.equals("8")) {
                    String id = socketReader.readLine();
                    int rowsAffected;

                    try {
                        if(IProductDao.getProductById(id) != null) {
                            rowsAffected = IProductDao.deleteProductById(id);

                            if(rowsAffected > 0) {
                                socketWriter.println("Product has successfully been deleted");
                                System.out.println("PRODUCT HAS SUCCESSFULLY BEEN DELETED");
                            }
                            else {
                                socketWriter.println("Error: Product was not deleted!");
                                System.out.println("PRODUCT WAS NOT DELETED! CHECK CODE FOR ERRORS!");
                            }
                        }
                        else {
                            socketWriter.println("Product with given id does not exist in the table!");
                            System.out.println("NO PRODUCT FOUND WITH GIVEN ID");
                        }
                    }
                    catch(DaoException e) {
                        e.printStackTrace();
                    }
                }

                // ******************** SUPPLIER OPTIONS ********************

                // DISPLAY ALL SUPPLIERS
                else if(request.equals("9")) {
                    List<Supplier> suppliers = ISupplierDao.getAllSuppliers();
                    JSONArray jsonArray = Supplier.suppliersListToJsonString(suppliers);

                    socketWriter.println(jsonArray);
                }
                // FIND SUPPLIER BY ID
                else if(request.equals("10")) {
                    System.out.println("NOT IMPLEMENTED");
                }
                // DISPLAY SUPPLIER BY PRODUCT ID
                else if(request.equals("11")) {
                    Supplier supplier;

                    try {
                        supplier = ISupplierDao.getSupplierByProductId(socketReader.readLine());

                        // Turn product into single json object and pass it back as a string to the client
                        JSONObject message = Supplier.turnSupplierIntoJson(supplier);
                        socketWriter.println(message.toString());

                        socketWriter.println("Server message: ID has been passed to server passing back supplier");
                    }
                    catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // ADD SUPPLIER
                else if (request.equals("12")) {
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
                            supplier = Supplier.makeSupplierFromJSON(jsonObject);
                            rowsAffected = ISupplierDao.addSupplier(supplier);

                            if (rowsAffected > 0) {
                                socketWriter.println(successMessage);

                            }
                            else {
                                socketWriter.println(errorMessage);
                            }
                        } else {
                            socketWriter.println(errorMessage);
                            System.err.println("SUPPLIER ALREADY EXISTS IN THE TABLE WITH PROVIDED ID!");
                        }

                    } catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // UPDATE SUPPLIER
                else if (request.equals("13")) {
                    // initialize variables
                    Supplier supplier;
                    int supplierUpdated;
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
                        // check if Supplier doesn't exist
                        if (ISupplierDao.getSupplierById(id) != null) {

                            // get and update Supplier to database
                            supplier = Supplier.makeSupplierFromJSON(jsonObject);
                            supplierUpdated = ISupplierDao.updateSupplier(id, supplier);

                            // check if Supplier was updated
                            if (supplierUpdated == 1) {

                                socketWriter.println(successMessage);

                            }
                            else {

                                socketWriter.println(errorMessage);
                            }
                        }
                        else {

                            socketWriter.println(errorMessage);
                        }

                    }
                    catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // DELETE SUPPLIER BY ID
                else if(request.equals("14")) {
                    String id = socketReader.readLine();
                    int rowsAffected;

                    try {
                        if(ISupplierDao.getSupplierById(id) != null) {
                            rowsAffected = ISupplierDao.deleteSupplierById(id);

                            if(rowsAffected > 0) {
                                socketWriter.println("Supplier has successfully been deleted");
                                System.out.println("SUPPLIER HAS SUCCESSFULLY BEEN DELETED");
                            }
                            else {
                                socketWriter.println("Error: Supplier was not deleted!");
                                System.err.println("SUPPLIER WAS NOT DELETED! CHECK CODE FOR ERRORS!");
                            }
                        }
                        else {
                            socketWriter.println("Supplier with given id does not exist in the table!");
                            System.out.println("NO SUPPLIER FOUND WITH GIVEN ID");
                        }
                    }
                    catch(DaoException e) {
                        e.printStackTrace();
                    }
                }

                // ******************** CUSTOMER OPTIONS ********************

                // DISPLAY ALL CUSTOMERS
                else if(request.equals("15")) {
                    List<Customer> customers = ICustomerDao.getAllCustomers();
                    JSONArray jsonArray = Customer.customersListToJsonString(customers);

                    socketWriter.println(jsonArray);
                }
                // FIND CUSTOMER BY ID
                else if(request.equals("16")) {
                    System.out.println("NOT IMPLEMENTED");
                }
                // ADD CUSTOMER
                else if (request.equals("17")) {
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
                            customer = Customer.makeCustomerFromJSON(jsonObject);
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
                // UPDATE CUSTOMER
                else if (request.equals("18")) {
                    // initialize variables
                    Customer customer;
                    int customerUpdated;
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
                        // check if Customer doesn't exist
                        if (ICustomerDao.getCustomerById(Integer.parseInt(id)) != null) {

                            // get and update Customer to database
                            customer = Customer.makeCustomerFromJSON(jsonObject);
                            customerUpdated = ICustomerDao.updateCustomer(Integer.parseInt(id), customer);

                            // check if Customer was updated
                            if (customerUpdated == 1) {

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
                // DELETE CUSTOMER
                else if(request.equals("19")) {
                    System.out.println("NOT IMPLEMENTED");
                }

                // ******************** CUSTOMER PRODUCT OPTIONS ********************

                // DISPLAY ALL CUSTOMER'S PRODUCTS
                else if(request.equals("20")) {
                    System.out.println("NOT IMPLEMENTED");
                }
                // FIND CUSTOMER'S PRODUCT BY PRODUCT ID AND CUSTOMER ID
                else if(request.equals("21")) {
                    System.out.println("NOT IMPLEMENTED");
                }
                // ADD CUSTOMER PRODUCT
                else if (request.equals("22")) {
                     // initialize variables
                     CustomersProducts customerP;
                     int customerPAdded;
                     String productID = socketReader.readLine();
                     String customerID = socketReader.readLine();
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
                         // check if CustomersProduct doesn't exist
                         if (ICustomersProductsDao.getCustomersProductsByIds(Integer.parseInt(customerID), productID) == null) {
 
                             // get and add CustomersProduct to database
                             customerP = CustomersProducts.makeCustomersProductsFromJSON(jsonObject);
                             customerPAdded = ICustomersProductsDao.addCustomersProducts(customerP);
 
                             // check if CustomersProduct was added
                             if (customerPAdded == 1) {
 
                                 socketWriter.println(successMessage);
 
                             } else {
 
                                 socketWriter.println(errorMessage);
                             }
                         } else {
 
                             socketWriter.println(errorMessage);
                         }
 
                     }
                     catch (DaoException e) {
                         e.printStackTrace();
                     }
                }
                // UPDATE CUSTOMERS PRODUCTS
                else if (request.equals("23")) {
                    // initialize variables
                    CustomersProducts customerP;
                    int customerPUpdated;
                    String productID = socketReader.readLine();
                    String customerID = socketReader.readLine();
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
                        // check if CustomersProduct does exist
                        if (ICustomersProductsDao.getCustomersProductsByIds(Integer.parseInt(customerID), productID) != null) {

                            // get and update CustomersProduct to database
                            customerP = CustomersProducts.makeCustomersProductsFromJSON(jsonObject);
                            customerPUpdated = ICustomersProductsDao.updateCustomersProducts(Integer.parseInt(customerID), productID, customerP);

                            // check if CustomersProduct was updated
                            if (customerPUpdated == 1) {

                                socketWriter.println(successMessage);

                            } else {

                                socketWriter.println(errorMessage);
                            }
                        } else {

                            socketWriter.println(errorMessage);
                        }

                    }
                    catch (DaoException e) {
                        e.printStackTrace();
                    }
                }
                // DELETE CUSTOMER'S PRODUCT BY CUSTOMER AND PRODUCT ID
                else if(request.equals("24")) {
                    System.out.println("NOT IMPLEMENTED");
                }
                // INVALID COMMAND
                else {
                    socketWriter.println("Error! I'm sorry I don't understand your request");
                    System.err.println("INVALID REQUEST MADE BY THE CLIENT");
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
        System.out.println("Handler for Client " + clientNumber + " is terminating .....");
    }

    private void receiveFile(String fileName) throws Exception
    {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        // DataInputStream allows us to read Java primitive types from stream, such as readLong()
        long numberOfBytesRemaining = dataInputStream.readLong(); // bytes remaining to be read (initially equal to file size)
        System.out.println("file size in bytes = " + numberOfBytesRemaining);

        // create a buffer to receive the incoming image bytes from the socket
        // A buffer is an array that stores a certain amount of data temporarily.
        byte[] buffer = new byte[4 * 1024];

        System.out.println("Bytes remaining to be read from socket: ");
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
