package com.example.oopca5project;

import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainApp {
    static ProductDaoInterface IProductDao = new MySqlProductDao();

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("OOP-CA5 PROJECT");

        menu();
    }

    public static void menu() {
        String[] options = {
                "Display all products",
                "Find product by ID",
                "Delete product by ID",
                "Add new product",
                "Update product by ID",
                "Filter products",
                "End application"
        };

        Methods.menuOptions(options);

        int choice = Methods.validateRange(1, 7);

        switch (choice) {
            case 1:
                getAllProducts();
                break;
            case 2:
                getProductById();
                break;
            case 3:
                deleteProductById();
                break;
            case 4:
                addProduct();
                break;
            case 5:
                updateProduct();
                break;
            case 6:
                filterProducts();
                break;
            case 7:
                System.out.println("Ending application. Goodbye!");
                break;
        }
    }

    // Question 1
    public static void getAllProducts() {
        try {
            List<Product> products = IProductDao.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            } else {
                for (Product product : products) {
                    System.out.println("{" + product.toString() + "}");
                }
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 2
    public static void getProductById() {
        try {
            System.out.println("Please enter the id of the product you wish to delete:");
            String id = sc.next();

            Product product = IProductDao.getProductById(id);
            if(product != null) {
                System.out.println("Product found!\n{" + product.toString() + "}");
            }
            else {
                System.out.println("No product found with given id!");
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }

        System.out.println();
        menu();
    }

    // Question 3
    public static void deleteProductById() {
        try {
            System.out.println("Please enter the id of the product you wish to delete:");
            String id = sc.next();

            IProductDao.deleteProductById(id);
        }
        catch(DaoException e) {
            e.printStackTrace();
        }

        System.out.println();
        menu();
    }

    // Question 4
    public static void addProduct() {
        try {
            System.out.println("Enter product id (e.g. 'product1', 'product2'): ");
            String id = sc.nextLine();

            IProductDao.addProduct(Methods.getProduct(id));
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 5
    public static void updateProduct() {
        try {
            System.out.println("Enter product id you wish to update (e.g. 'product1', 'product2'): ");
            String id = sc.nextLine();

            IProductDao.updateProduct(id, Methods.getProduct(id));
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 6
    public static void filterProducts() {
        try {
            // Initially get all products first
            List<Product> products = IProductDao.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            }
            else {
                // Reference: https://stackoverflow.com/questions/66532091/java-8-streams-filter-by-a-property-of-an-object
                System.out.println("Please enter a price (e.g. 10.00) to filter products below that price");
                double price = sc.nextDouble();

                List<Product> productsBelowCertainPrice = products.stream()
                        .filter(p -> p.getPrice() < price)
                        .collect(Collectors.toList());

                if(productsBelowCertainPrice.isEmpty()) {
                    System.out.println("No product found that is below given price!");
                }
                else {
                    for(Product product : productsBelowCertainPrice) {
                        System.out.println("{" + product.toString() + "}");
                    }
                }
            }

            System.out.println();
            menu();

        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
