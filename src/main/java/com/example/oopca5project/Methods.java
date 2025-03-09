package com.example.oopca5project;

import com.example.oopca5project.DTOs.Product;

import java.util.Scanner;

public class Methods {
    static Scanner sc = new Scanner(System.in);

    public static void menuOptions(String[] options) {
        System.out.println();
        for(int i = 1; i <= options.length; i++) {
            System.out.println(i + ". " + options[i-1]);
        }
        System.out.println();
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

    public static Product getProduct(String id) {

        // initializing variables
        String product_description, size, supplier_id;
        double unit_price;

        // getting rid of stray \n (new line) character
        sc.nextLine();

        // getting product description
        System.out.println("Enter product description: ");
        product_description = sc.nextLine();

        // getting product size
        System.out.println("Enter product size: ");
        size = sc.nextLine();

        // getting product unit price
        System.out.println("Enter product unit price: ");
        unit_price = validateDoubleRange(0);
        sc.nextLine();

        // getting product supplier id
        System.out.println("Enter product supplier id (e.g. 'supplier1', 'supplier2'): ");
        supplier_id = sc.nextLine();

        // returns newly made product object
        return new Product(id,product_description,size,unit_price,supplier_id);
        
    }
}
