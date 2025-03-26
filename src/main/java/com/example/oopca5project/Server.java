package com.example.oopca5project;

import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;

import static com.example.oopca5project.MainApp.IProductDao;

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

            while(true) {
                System.out.println("Server: Waiting for any connections on port " +SERVER_PORT+ "...");
                clientSocket = serverSocket.accept();
                clientNumber++;

                System.out.println("Server: Client " +clientNumber+ " has connected with port number " +clientSocket.getPort());
                System.out.println("Server: Port number " +clientSocket.getLocalPort()+ " is currently used to talk with the client");

                Thread t = new Thread(new ClientHandler(clientSocket, clientNumber));
                t.start();

                System.out.println("Server: ClientHandler started in thread " +t.getName()+ " for client " +clientNumber+ ".");
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
        finally {
            try {
                if(clientSocket!=null)
                    clientSocket.close();
            }
            catch (IOException e) {
                System.out.println(e);
            }
            try {
                if(serverSocket!=null)
                    serverSocket.close();
            }
            catch (IOException e) {
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
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String request;

        try {
            while((request = socketReader.readLine()) != null) {
                System.out.println("Server: (ClientHandler): Read command from client " +clientNumber+ ": " +request);

                if (request.equals("1")) {
                    // Get all products the exact same way you would in the MainApp class
                    List<Product> products = IProductDao.getAllProducts();

                    System.out.println("Retrieving all products...");
                    if (products.isEmpty()) {
                        // Difference now is we use socketWriter to print it out in the client
                        socketWriter.println("Products table is empty! Please add some data first.");
                    }
                    else {
                        for (Product product : products) {
                            // Same here
                            socketWriter.println("{" + product.toString() + "}");
                        }
                    }

                    // Lets the client know that Server is not sending any more data from products
                    socketWriter.println("Done!");
                    System.out.println("Ending displayAllProducts() menu...");
                }
                else if (request.equals("2")) {
                    socketWriter.println("Sorry to see you leaving. Goodbye.");
                    System.out.println("Server message: Client has notified us that it is quitting.");
                }
                else {
                    socketWriter.println("Error! I'm sorry I don't understand your request");
                    System.out.println("Server message: Invalid request from client.");
                }
            }
        }
        catch(IOException | DaoException e) {
            e.printStackTrace();
        }
        finally {
            this.socketWriter.close();

            try {
                this.socketReader.close();
                this.clientSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("Server: (ClientHandler): Handler for Client " + clientNumber + " is terminating .....");
    }
}
