package com.example.oopca5project.JavaFX.CustomersProductsMC;

import java.util.List;

import com.example.oopca5project.DAOs.CustomerDaoInterface;
import com.example.oopca5project.DAOs.CustomersProductsDaoInterface;
import com.example.oopca5project.DAOs.MySqlCustomerDao;
import com.example.oopca5project.DAOs.MySqlCustomersProductsDao;
import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.DTOs.CustomersProducts;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.Exceptions.DaoException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CustomersProductsModel {

    private List<CustomersProducts> customersProductsList;
    private CustomersProductsController customersProductsController;

    ObservableList<CustomersProducts> observableCustomersProductsList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static CustomersProductsDaoInterface ICustomersProductsDao = new MySqlCustomersProductsDao(); // To access the DAO methods of course
    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao();
    static ProductDaoInterface IProductDao = new MySqlProductDao();

    // Constructor - populates model with data from DAO
    public CustomersProductsModel(CustomersProductsController customersProductsController) {
        this.customersProductsController = customersProductsController;
        this.observableCustomersProductsList = FXCollections.observableArrayList();
    }

    // Below method is responsible for populating both the regular and observable CustomersProducts list using the DAO we have already defined.
    private void populateCustomersProductsList() {
        try {
            // The reason we populate the regular list is to keep a copy of the original customersProducts for filtering purposes
            this.customersProductsList = ICustomersProductsDao.getAllCustomerProducts();

            if (this.customersProductsList != null) {
                this.observableCustomersProductsList.addAll(customersProductsList); // Then we populate the observable list for JavaFX
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersProductsController.setErrorSuccessMessage("An error occurred while retrieving the data. Please try again.", true);
        }
    }

    // Below method is responsible for getting a customersProducts that matches the user's passed in supplier_id
    public void getSingleCustomersProductsById(String getProductId, int customerId) {
        try {
            CustomersProducts customersProducts = ICustomersProductsDao.getCustomersProductsByIds(customerId, getProductId);

            // If a matching customersProducts is found, clear the observable list and add only the matching customersProducts
            if (customersProducts != null) {
                this.observableCustomersProductsList.clear();
                this.observableCustomersProductsList.add(customersProducts);
                customersProductsController.setErrorSuccessMessage("SUCCESS! Found customersProducts with matching IDs!", false);
            } else {
                customersProductsController.setErrorSuccessMessage("No matching customersProducts found with customerId '" + customerId + "' and productId '" + getProductId + "'. Please try again!", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersProductsController.setErrorSuccessMessage("An error occurred while retrieving the data. Please try again.", true);
        }
    }

    // Below method will call the DAO method to add a new customersProducts to the database and show the new customersProducts list in JavaFX
    public void addNewCustomersProducts(CustomersProducts customersProducts) {
        try {
            // Check if customer-product pair already exists
            CustomersProducts existingProduct = ICustomersProductsDao.getCustomersProductsByIds(customersProducts.getCustomerId(), customersProducts.getProductId());
            if (existingProduct != null) {
                customersProductsController.setErrorSuccessMessage("This customer-product already exists. Please try again!", true);
                return;
            }

            Customer c = ICustomerDao.getCustomerById(customersProducts.getCustomerId());
            Product p = IProductDao.getProductById(customersProducts.getProductId());

            if (c == null) {
                customersProductsController.setErrorSuccessMessage("A valid Customer is required", true);
                return;
            }

            if (p == null) {
                customersProductsController.setErrorSuccessMessage("A valid Product is required", true);
                return;
            }

            // Add new customer-product
            int rowsAffected = ICustomersProductsDao.addCustomersProducts(customersProducts);

            // If the new customersProducts was added, refresh the customersProducts database
            if (rowsAffected == 1) {
                reloadCustomersProductsListModel();
                customersProductsController.setErrorSuccessMessage("SUCCESS! Added customersProducts to the table!", false);
            } else {
                customersProductsController.setErrorSuccessMessage("Error! Something went wrong when trying to add the customersProducts, please try again!", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersProductsController.setErrorSuccessMessage("An error occurred while adding the customersProducts. Please try again.", true);
        }
    }

    // Below method takes in a customersProducts object and overwrites an existing customersProducts with a matching id
    public void updateCustomersProducts(CustomersProducts customersProducts) {
        try {
            int customerId = customersProducts.getCustomerId();
            String productId = customersProducts.getProductId();
            CustomersProducts existingProduct = ICustomersProductsDao.getCustomersProductsByIds(customersProducts.getCustomerId(), customersProducts.getProductId());

            // If the product is found, update it
            if (existingProduct == null) {
                customersProductsController.setErrorSuccessMessage("Cannot update a non-existent customer-product. Please try again!", true);
                return;
            }

            int rowsAffected = ICustomersProductsDao.updateCustomersProducts(customerId, productId, customersProducts);
            if (rowsAffected == 1) {
                reloadCustomersProductsListModel();
                customersProductsController.setErrorSuccessMessage("SUCCESS! Updated matching customersProducts in the table", false);
            } else {
                customersProductsController.setErrorSuccessMessage("FATAL ERROR! Something went wrong when trying to update this customer-product", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersProductsController.setErrorSuccessMessage("An error occurred while updating the customersProducts. Please try again.", true);
        }
    }

    // Below method takes in a product id and deletes the corresponding product with that id using a DAO method
    public void deleteCustomersProductsById(int customerId, String productId) {
        try {
            CustomersProducts existingProduct = ICustomersProductsDao.getCustomersProductsByIds(customerId, productId);

            // If the product exists, delete it
            if (existingProduct != null) {
                int rowsAffected = ICustomersProductsDao.deleteCustomersProductsByIds(customerId, productId);
                reloadCustomersProductsListModel();
                customersProductsController.setErrorSuccessMessage("SUCCESS! Deleted matching customersProducts from the table", false);
            } else {
                customersProductsController.setErrorSuccessMessage("This customer-product does not exist. Please try again!", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersProductsController.setErrorSuccessMessage("An error occurred while deleting the customersProducts. Please try again.", true);
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
