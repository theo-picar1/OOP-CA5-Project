package com.example.oopca5project.JavaFX.CustomersMC;

import com.example.oopca5project.DAOs.MySqlCustomerDao;
import com.example.oopca5project.DAOs.CustomerDaoInterface;
import com.example.oopca5project.DTOs.Customer;

import com.example.oopca5project.Exceptions.DaoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CustomerModel {
    private List<Customer> productsList;

    ObservableList<Customer> observableCustomerList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao(); // To access the DAO methods of course

    // Constructor - populates model with data from DAO
    public CustomerModel() {
        this.observableCustomerList = FXCollections.observableArrayList();
    }

    // Below method is responsible for populating both the regular and observable Customer list using the DAO we have already defined.
    private void populateCustomerList() {
        try {
            // The reason we populate the regular list is to keep a copy of the original products for filtering purposes
            this.productsList = ICustomerDao.getAllCustomers();

            if(this.productsList != null) {
                this.observableCustomerList.addAll(productsList); // Then we populate the observable list for JavaFX
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method is responsible for getting a product that matches the user's passed in product_id
    public void getSingleCustomerById(int customerId) {
        try {
            Customer customer = ICustomerDao.getCustomerById(customerId);

            // If a matching product is found, clear the observable list and add only the matching product
            if(customer != null) {
                this.observableCustomerList.clear();
                this.observableCustomerList.add(customer);
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method will call the DAO method to add a new product to the database and show the new product list in JavaFX
    public void addNewCustomer(Customer product) {
        try {
            int rowsAffected = ICustomerDao.addCustomer(product);

            // If the new product was added, refresh the product database
            if(rowsAffected == 1) {
                reloadCustomerListModel();
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a product id and deletes the corresponding product with that id using a DAO method
    public void deleteCustomerById(int id) {
        try {
            int rowsAffected = ICustomerDao.deleteCustomerById(id);

            // If the product was deleted, refresh the product database
            if(rowsAffected == 1) {
                reloadCustomerListModel();
            }
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a product object and overwrites an existing product with a matching id
    public void updateCustomer(Customer customer) {
        try {
            int id = customer.getId();

            int rowsAffected = ICustomerDao.updateCustomer(id, customer);

            // If the product was successfully updated, refresh the product database
            if(rowsAffected == 1) {
                reloadCustomerListModel();
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Getter for list of products
    public ObservableList<Customer> getObservableCustomerList() {
        return this.observableCustomerList;
    }

    // This method practically restarts the DAO. I assume this is for when the user decides to clear all filters and wants the original list back
    public void reloadCustomerListModel() {
        this.observableCustomerList.clear();
        populateCustomerList();
    }
}
