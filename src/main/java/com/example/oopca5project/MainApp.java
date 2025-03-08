package com.example.oopca5project;

import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

import java.util.Scanner;

public class MainApp {
    static ProductDaoInterface IProductDao = new MySqlProductDao();

    static Scanner kb = new Scanner(System.in);

    public static void main(String[] args) throws DaoException {
        int choice = 0;
        String id = "", product_description = "", size, suppiler_id;
        double unit_price = 0;
        Product p;

        String[] array = {"1. Get all products", "2. Get product by id", "3. Delete product by id", "4. Add product", "5. Update product", "6. Find product apply filter", "0. Exit"};
        do {
            for(String s : array) {
                System.out.println(s);
            }
            choice = kb.nextInt();
            kb.nextLine();
            switch (choice) {
                case 1:
                    IProductDao.getAllProducts();
                    break;
                case 2:
                    id = kb.next();
                    IProductDao.getProductById(id);
                    break;
                case 3:
                    id = kb.next();
                    IProductDao.deleteProductById(id);
                    break;
                case 4:
                    // validation of ID not added yet need Q2 done
                    System.out.println("Enter product id: (e.g. 'product1', 'product2')");
                    id = kb.nextLine();

                    System.out.println("Enter product description: ");
                    product_description = kb.nextLine();

                    System.out.println("Enter product size: ");
                    size = kb.nextLine();

                    System.out.println("Enter product unit_price: ");
                    unit_price = kb.nextDouble();

                    System.out.println("Enter product suppiler_id: ");
                    suppiler_id = kb.next();

                    p = new Product(id,product_description,size,unit_price,suppiler_id);
                    IProductDao.addProduct(p);
                    break;
                case 5:
                    p = new Product();
                    id = kb.next();
                    IProductDao.updateProduct(id,p);
                    break;
                case 6:
//                  findProductApplyFilter()
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    break;
            }
        }while(choice != 0);
    }
}
