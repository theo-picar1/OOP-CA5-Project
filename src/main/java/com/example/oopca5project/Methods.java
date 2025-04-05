package com.example.oopca5project;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.oopca5project.DTOs.Product;

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
}