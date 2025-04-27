package com.example.oopca5project.JavaFX.CustomersMC;

import com.example.oopca5project.DAOs.CustomerDaoInterface;
import com.example.oopca5project.DAOs.MySqlCustomerDao;
import com.example.oopca5project.DTOs.Customer;
import com.example.oopca5project.Exceptions.DaoException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CustomerModel {

    private List<Customer> customerList;
    private CustomersController customersController;

    ObservableList<Customer> observableCustomerList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static CustomerDaoInterface ICustomerDao = new MySqlCustomerDao(); // To access the DAO methods of course

    public CustomerModel(CustomersController customersController) {
        this.customersController = customersController;
        this.observableCustomerList = FXCollections.observableArrayList();
    }

    // Below method is responsible for populating both the regular and observable Customer list using the DAO we have already defined.
    private void populateCustomerList() {
        try {
            // The reason we populate the regular list is to keep a copy of the original customers for filtering purposes
            this.customerList = ICustomerDao.getAllCustomers();

            if (this.customerList != null) {
                this.observableCustomerList.addAll(customerList); // Then we populate the observable list for JavaFX
            }
        } catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method is responsible for getting a customer that matches the user's passed in customer_id
    public void getSingleCustomerById(int customerId) {
        try {
            Customer customer = ICustomerDao.getCustomerById(customerId);

            // If a matching customer is found, clear the observable list and add only the matching customer
            if (customer != null) {
                this.observableCustomerList.clear();
                this.observableCustomerList.add(customer);
                customersController.setErrorSuccessMessage("SUCCESS! Found customer with matching ID!", false);
            } else {
                customersController.setErrorSuccessMessage("No customer has been found with an ID of '" + customerId + "'. Please try again!", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersController.setErrorSuccessMessage("An error occurred while retrieving the customer. Please try again.", true);
        }
    }

    // Below method will call the DAO method to add a new customer to the database and show the new customer list in JavaFX
    public void addNewCustomer(Customer customer) {
        try {
            // If a customer with the provided id already exists, don't proceed with the adding logic and let the user know
            Customer checkCustomer = ICustomerDao.getCustomerById(customer.getId());

            if (checkCustomer != null) {
                customersController.setErrorSuccessMessage("A customer with this id already exists. Please try again!", true);
                return;
            }

            int rowsAffected = ICustomerDao.addCustomer(customer);

            // If the new customer was added, refresh the customer database
            if (rowsAffected == 1) {
                reloadCustomerListModel();
                customersController.setErrorSuccessMessage("SUCCESS! Added customer to the table!", false);
            } else {
                customersController.setErrorSuccessMessage("Error! Something went wrong when trying to add the customer, please try again!", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersController.setErrorSuccessMessage("An error occurred while adding the customer. Please try again.", true);
        }
    }

    // Below method takes in a customer id and deletes the corresponding customer with that id using a DAO method
    public void deleteCustomerById(int id) {
        try {
            // If a customer with the provided id does not exist, don't proceed with the deleting logic and let the user know
            Customer checkCustomer = ICustomerDao.getCustomerById(id);

            if (checkCustomer == null) {
                customersController.setErrorSuccessMessage("This id does not exist in the table. Please enter a customer id that exists!", true);
                return;
            }

            int rowsAffected = ICustomerDao.deleteCustomerById(id);

            // If the customer was deleted, refresh the customer database
            if (rowsAffected == 1) {
                customersController.setErrorSuccessMessage("SUCCESS! Deleted matching customer from the table", false);
                reloadCustomerListModel();
            } else {
                customersController.setErrorSuccessMessage("No customers were deleted. Please first check the table to see if a customer with your id exists!", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersController.setErrorSuccessMessage("An error occurred while deleting the customer. Please try again.", true);
        }
    }

    // Below method takes in a customer object and overwrites an existing customer with a matching id
    public void updateCustomer(Customer customer) {
        try {
            // If a customer with the provided id does not exist, don't proceed with the updating logic and let the user know
            Customer checkCustomer = ICustomerDao.getCustomerById(customer.getId());

            if (checkCustomer == null) {
                customersController.setErrorSuccessMessage("Cannot update a customer that does not exist. Please try again!", true);
                return;
            }

            int rowsAffected = ICustomerDao.updateCustomer(customer.getId(), customer);

            // If the customer was successfully updated, refresh the customer database
            if (rowsAffected == 1) {
                customersController.setErrorSuccessMessage("SUCCESS! Updated matching customer in the table", false);
                reloadCustomerListModel();
            } else {
                customersController.setErrorSuccessMessage("FATAL ERROR! Something went wrong when trying to update this customer", true);
            }
        } catch (DaoException e) {
            e.printStackTrace();
            customersController.setErrorSuccessMessage("An error occurred while updating the customer. Please try again.", true);
        }
    }

    // Getter for list of customers
    public ObservableList<Customer> getObservableCustomerList() {
        return this.observableCustomerList;
    }

    // This method practically restarts the DAO. I assume this is for when the user decides to clear all filters and wants the original list back
    public void reloadCustomerListModel() {
        this.observableCustomerList.clear();
        populateCustomerList();
    }
}
