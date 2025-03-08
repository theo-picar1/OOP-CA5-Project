package com.example.oopca5project;

import java.util.Scanner;

public class Methods {
    static Scanner sc = new Scanner(System.in);
    
    public static void menuOptions(String[] options) {
        for(int i = 1; i <= options.length; i++) {
            System.out.println(i + ". " + options[i-1]);
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
}
