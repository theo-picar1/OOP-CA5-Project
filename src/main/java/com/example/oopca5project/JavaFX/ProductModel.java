package com.example.oopca5project.JavaFX;

import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Product;

import com.example.oopca5project.Exceptions.DaoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProductModel {
    private List<Product> productsList;

    ObservableList<Product> observableProductList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static ProductDaoInterface IProductDao = new MySqlProductDao(); // To access the DAO methods of course

    // Constructor - populates model with data from DAO
    public ProductModel() {
        this.observableProductList = FXCollections.observableArrayList();
    }

    // Below method is responsible for populating both the regular and observable Product list using the DAO we have already defined.
    private void populateProductList() {
        try {
            // The reason we populate the regular list is to keep a copy of the original products for filtering purposes
            this.productsList = IProductDao.getAllProducts();

            if(this.productsList != null) {
                this.observableProductList.addAll(productsList); // Then we populate the observable list for JavaFX
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method is responsible for getting a product that matches the user's passed in product_id
    public void getSingleProductById(String productId) {
        try {
            Product product = IProductDao.getProductById(productId);

            // If a matching product is found, clear the observable list and add only the matching product
            if(product != null) {
                this.observableProductList.clear();
                this.observableProductList.add(product);
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method will call the DAO method to add a new product to the database and show the new product list in JavaFX
    public void addNewProduct(Product product) {
        try {
            int rowsAffected = IProductDao.addProduct(product);

            // If the new product was added, refresh the product database
            if(rowsAffected == 1) {
                reloadProductListModel();
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Getter for list of products
    public ObservableList<Product> getObservableProductList() {
        return this.observableProductList;
    }

    // This method practically restarts the DAO. I assume this is for when the user decides to clear all filters and wants the original list back
    public void reloadProductListModel() {
        this.observableProductList.clear();
        populateProductList();
    }
}
