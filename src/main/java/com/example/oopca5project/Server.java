package com.example.oopca5project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try (
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            System.out.println("Server: Server successfully started! Waiting on client to connect...");

            String message = in.readLine();
            System.out.println("Server: Client message - " +message+ ". Replying back to client.");
            out.println("Server: Hello client!");

            System.out.println("Server: Finishing up session... Done! Goodbye.");
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }
}
