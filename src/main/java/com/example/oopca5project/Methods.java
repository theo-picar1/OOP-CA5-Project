package com.example.oopca5project;

import java.util.ArrayList;
import java.util.Scanner;

import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;

public class Methods {
    static Scanner sc = new Scanner(System.in);

    public static void menuOptions(String[] options) {
        System.out.println();
        for(int i = 0; i < options.length; i++) {
            System.out.println(i+1 + ". " + options[i]);
        }
    }

    public static int validateRange(int min, int max) {
        int input = 0;

        while(true) {
            if(sc.hasNextInt()) {
                input = sc.nextInt();

                if(input < min || input > max) {
                    System.out.println("Please enter a valid option (" +min+ "-" +max+ ")");
                }
                else {
                    break;
                }
            }
            else {
                System.out.println("Please enter only integer values");
                sc.next();
            }
        }

        return input;
    }

    public static double validateDoubleRange(double min) {

        // initializing variable
        double input = 0;

        // loop to go on forever until correct input is achieved
        while(true) {

            // checking if next input is a double
            if(sc.hasNextDouble()) {

                // when input is a double assign it to a variable
                input = sc.nextDouble();

                // check if input is within range
                if(input < min) {

                    // input is wrong -> print out error message
                    System.out.println("Please enter a valid option (" +min+ "or higher)");

                }
                else {

                    // input is correct -> break out of loop
                    break;

                }
            } else {

                // input was not a double -> print out error message
                System.out.println("Please enter a valid number");

                // get rid of stray \n (new line) character
                sc.next();
            }
        }

        // input is a double + in range -> input is returned
        return input;

    }

    public static int validateInt() {
        int input;

        while(true) {
            if(sc.hasNextInt()) {
                input = sc.nextInt();

                break;
            }
            else {
                System.out.println("Please enter only integer values");
                sc.next();
            }
        }

        return input;
    }

    // Method that handles user input for
    public static Product getProduct(String id) {
        // initializing variables
        String product_description, size, supplier_id;
        double unit_price;

        // getting product description
        System.out.println("Enter product description: ");
        // Clear the leftover line if there was any. Stops it from printing out product size prompt
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
        product_description = sc.nextLine();

        // getting product size
        System.out.println("Enter product size: ");
        size = sc.next();

        // getting product unit price
        System.out.println("Enter product unit price: ");
        unit_price = Methods.validateDoubleRange(0);

        // getting product supplier id
        System.out.println("Enter product supplier id (e.g. 'supplier1', 'supplier2'): ");
        supplier_id = sc.next();

        // returns newly made product object
        return new Product(id, product_description, size, unit_price, supplier_id);
    }

    // Method that will print an object that was passed in. Method also checks to see if the object passed in is null or not.
    public static void printObject(Object obj){

        // checks if object is null
        if(obj != null){

            // Print object if not null
            System.out.println(obj);

        }else{

            // Print error message
            System.out.println("Object was not found");

        }
    }

    // Method that handles user input for getting Customer object
    public static Customer getCustomer() {

        // Get rid of '\n' character
        sc.nextLine();

        // initializing variables
        String name, email, phoneNo;

        // Get Customer variables
        System.out.println("Enter customer name: ");
        name = sc.nextLine();

        System.out.println("Enter customer email: ");
        email = sc.next();

        System.out.println("Enter customer phone number: ");
        phoneNo = sc.next();

        // Return Customer object
        return new Customer(name, email, phoneNo);
    }

    public static Supplier createSupplier(String id) {
        System.out.println("Enter supplier's name:");
        String name = sc.next();

        System.out.println("Enter supplier's telephone number");
        String phone = sc.next();

        System.out.println("Enter supplier's email");
        String email = sc.next();

        return new Supplier(id, name, phone, email);
    }

    // <T> is a Generic type. i.e. it creates (lists) without casting types.
    // This allows for a list of Products, Suppliers, and Customers to be
    // Passed into this method without the need for overloading.
    public static <T> void printListOfObjects(ArrayList<T> objList) {

        // check if passed list is empty
        if(objList != null && !objList.isEmpty()) {

            // print each Object in turn
            for (Object obj : objList) {
                System.out.println(obj);
            }

        }else{

            // Print error message if list is empty
            System.out.println("List is empty");
        }
    }
}