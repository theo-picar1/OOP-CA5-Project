package com.example.oopca5project.JavaFX.CustomersProductsMC;

import java.util.List;

import com.example.oopca5project.DAOs.CustomersProductsDaoInterface;
import com.example.oopca5project.DAOs.MySqlCustomersProductsDao;
import com.example.oopca5project.DTOs.CustomersProducts;
import com.example.oopca5project.Exceptions.DaoException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomersProductsModel {
    private List<CustomersProducts> customersProductsList;

    ObservableList<CustomersProducts> observableCustomersProductsList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static CustomersProductsDaoInterface ICustomersProductsDao = new MySqlCustomersProductsDao(); // To access the DAO methods of course

    // Constructor - populates model with data from DAO
    public CustomersProductsModel() {
        this.observableCustomersProductsList = FXCollections.observableArrayList();
    }

    // Below method is responsible for populating both the regular and observable CustomersProducts list using the DAO we have already defined.
    private void populateCustomersProductsList() {
         try {
             // The reason we populate the regular list is to keep a copy of the original customersProducts for filtering purposes
             this.customersProductsList = ICustomersProductsDao.getAllCustomerProducts();

             if(this.customersProductsList != null) {
                 this.observableCustomersProductsList.addAll(customersProductsList); // Then we populate the observable list for JavaFX
             }
         }
         catch(DaoException e) {
             e.printStackTrace();
         }
    }

    // Below method is responsible for getting a customersProducts that matches the user's passed in supplier_id
    public void getSingleCustomersProductsById(String getProductId, int customerId) {
        try {
            CustomersProducts customersProducts = ICustomersProductsDao.getCustomersProductsByIds(customerId, getProductId);

            // If a matching customersProducts is found, clear the observable list and add only the matching customersProducts
            if(customersProducts != null) {
                this.observableCustomersProductsList.clear();
                this.observableCustomersProductsList.add(customersProducts);
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method will call the DAO method to add a new customersProducts to the database and show the new customersProducts list in JavaFX
    public void addNewCustomersProducts(CustomersProducts customersProducts) {
        try {
            int rowsAffected = ICustomersProductsDao.addCustomersProducts(customersProducts);

            // If the new customersProducts was added, refresh the customersProducts database
            if(rowsAffected == 1) {
                reloadCustomersProductsListModel();
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a customersProducts object and overwrites an existing customersProducts with a matching id
    public void updateCustomersProducts(CustomersProducts customersProducts) {
        try {
            int customerId = customersProducts.getCustomerId();
            String productId = customersProducts.getProductId();

            int rowsAffected = ICustomersProductsDao.updateCustomersProducts(customerId, productId, customersProducts);

            // If the product was successfully updated, refresh the product database
            if(rowsAffected == 1) {
                reloadCustomersProductsListModel();
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a product id and deletes the corresponding product with that id using a DAO method
    public void deleteCustomersProductsById(int customerId, String productId) {
        try {
            int rowsAffected = ICustomersProductsDao.deleteCustomersProductsByIds(customerId, productId);

            // If the product was deleted, refresh the product database
            if(rowsAffected == 1) {
                reloadCustomersProductsListModel();
            }
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Getter for list of customersProducts
    public ObservableList<CustomersProducts> getObservableCustomersProductsList() {
        return this.observableCustomersProductsList;
    }

    // This method practically restarts the DAO. I assume this is for when the user decides to clear all filters and wants the original list back
    public void reloadCustomersProductsListModel() {
        this.observableCustomersProductsList.clear();
        populateCustomersProductsList();
    }
}
