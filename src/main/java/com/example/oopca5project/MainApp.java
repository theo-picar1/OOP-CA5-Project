package com.example.oopca5project;

import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

import java.util.List;
import java.util.Scanner;

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
                System.out.println("Finding product by ID...");
                break;
            case 3:
                System.out.println("Deleting product by ID...");
                break;
            case 4:
                addProduct();
                break;
            case 5:
                System.out.println("Updating product by ID...");
                break;
            case 6:
                System.out.println("Filtering products...");
                break;
            case 7:
                System.out.println("Ending application. Goodbye!");
                break;
        }
    }

    public static void getAllProducts() {
        try {
            List<Product> products = IProductDao.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            }
            else {
                for (Product product : products) {
                    System.out.println("{" + product.toString() + "}");
                }
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    public static void addProduct() {
        try {
            String id, product_description, size, supplier_id;
            double unit_price;
            Product p;

            // validation of ID not added yet need Q2 done
            System.out.println("Enter product id: (e.g. 'product1', 'product2')");
            id = sc.nextLine();

            System.out.println("Enter product description: ");
            product_description = sc.nextLine();

            System.out.println("Enter product size: ");
            size = sc.nextLine();

            System.out.println("Enter product unit_price: ");
            unit_price = sc.nextDouble();

            System.out.println("Enter product suppiler_id: ");
            supplier_id = sc.next();

            p = new Product(id, product_description, size, unit_price, supplier_id);
            IProductDao.addProduct(p);
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }
}
