package com.example.oopca5project;

import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;
import org.json.JSONArray;
import org.json.JSONObject;

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
                "End application",
                "Display all products",
                "Find product by ID",
                "Delete product by ID",
                "Add new product",
                "Update product by ID",
                "Filter products",
                "Print entities as JSON"
        };

        Methods.menuOptions(options);

        int choice = Methods.validateRange(0, 7);

        try {
            switch (choice) {
                case 0:
                    System.out.println("Ending application. Goodbye!");
                    break;
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
                    List<Product> products = IProductDao.getAllProducts();
                    ProductsListToJsonString(products);
                    break;
                case 8:
                    System.out.println("Question 8");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Question 1
    public static void getAllProducts() {
        try {
            List<Product> products = IProductDao.getAllProducts();

            System.out.println("Retrieving all products...");
            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            } else {
                for (Product product : products) {
                    System.out.println("{" + product.toString() + "}");
                }
            }

            System.out.println();
            menu();
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 2
    public static void getProductById() {
        try {
            System.out.println("Please enter the id of the product you wish to delete:");
            String id = sc.next();

            System.out.println("Finding product with given id...");
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

            System.out.println("Deleting product with given id...");
            int rowsAffected = IProductDao.deleteProductById(id);

            if(rowsAffected > 0) {
                System.out.println("Successfully deleted product with id " +id);
            }
            else {
                System.out.println("An error occurred. Please check if your id exists in the database!");
            }
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
            String id = sc.next();

            int rowsAffected = IProductDao.addProduct(Methods.getProduct(id));

            System.out.println("Adding product...");
            if(rowsAffected > 0) {
                System.out.println("Product has been successfully added!");
            }
            else {
                System.out.println("An error occurred. Product could not be added!");
            }

            System.out.println();
            menu();
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 5
    public static void updateProduct() {
        try {
            System.out.println("Enter product id you wish to update (e.g. 'product1', 'product2'): ");
            String id = sc.next();

            int rowsAffected = IProductDao.updateProduct(id, Methods.getProduct(id));

            System.out.println("Updating product with given id...");
            if (rowsAffected > 0) {
                System.out.println("Product with id " +id+ " has been successfully added");
            }
            else {
                System.out.println("Error in updating product. Check if your product id exists in the database!");
            }

            System.out.println();
            menu();
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Question 6
    public static void filterProducts() {
        try {
            // Initially get all products first so we can have something to filter
            List<Product> products = IProductDao.getAllProducts();

            if (products.isEmpty()) {
                System.out.println("Products table is empty! Please add some data first.");
            }
            else {
                System.out.println("Please enter a price (e.g. 10.00) to filter products below that price");
                double price = sc.nextDouble();

                // Reference: https://stackoverflow.com/questions/66532091/java-8-streams-filter-by-a-property-of-an-object
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

    // Question 7
    public static void ProductsListToJsonString(List<Product> list) {
        JSONArray jsonArray = new JSONArray();
        for (Product product : list) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("product_id", product.getId());
            jsonObject.put("product_description", product.getDescription());
            jsonObject.put("size", product.getSize());
            jsonObject.put("unit_price", product.getPrice());
            jsonObject.put("supplier_id", product.getSupplierId());
            jsonArray.put(jsonObject);
        }
        System.out.println(jsonArray.toString());
        menu();
    }

    // Question 8
    public static  String ProductsToJsonString(Product p){
        return null;
    }


}
