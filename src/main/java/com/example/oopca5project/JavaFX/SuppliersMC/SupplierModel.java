package com.example.oopca5project.JavaFX.SuppliersMC;

import java.util.List;

import com.example.oopca5project.DAOs.MySqlSupplierDao;
import com.example.oopca5project.DAOs.SupplierDaoInterface;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierModel {
    private List<Supplier> suppliersList;
    private SuppliersController suppliersController;

    ObservableList<Supplier> observableSupplierList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao(); // To access the DAO methods of course

    // Constructor - populates model with data from DAO
    public SupplierModel(SuppliersController suppliersController) {
        this.suppliersController = suppliersController;
        this.observableSupplierList = FXCollections.observableArrayList();
    }

    // Below method is responsible for populating both the regular and observable Supplier list using the DAO we have already defined.
    private void populateSupplierList() {
        try {
            // The reason we populate the regular list is to keep a copy of the original suppliers for filtering purposes
            this.suppliersList = ISupplierDao.getAllSuppliers();

            if(this.suppliersList != null) {
                this.observableSupplierList.addAll(suppliersList); // Then we populate the observable list for JavaFX
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method is responsible for getting a supplier that matches the user's passed in supplier_id
    public void getSingleSupplierById(String supplierId) {
        try {
            Supplier supplier = ISupplierDao.getSupplierById(supplierId);

            // If a matching supplier is found, clear the observable list and add only the matching supplier
            if(supplier != null) {
                this.observableSupplierList.clear();
                this.observableSupplierList.add(supplier);
                suppliersController.setErrorSuccessMessage("SUCCESS! Found supplier with matching supplier_id!", false);
            }
            else { suppliersController.setErrorSuccessMessage("ERROR! No supplier found with the provided id!", true); }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method will call the DAO method to add a new supplier to the database and show the new supplier list in JavaFX
    public void addNewSupplier(Supplier supplier) {
        try {
            // If a product with the provided id already exists, don't proceed with the adding logic and let the user know
            Supplier checkSupplier = ISupplierDao.getSupplierById(supplier.getId());

            if(checkSupplier != null) {
                suppliersController.setErrorSuccessMessage("This id already exists in the table. Please enter a supplier id that doesn't currently exist!", true);
                return;
            }

            int rowsAffected = ISupplierDao.addSupplier(supplier);

            // If the new supplier was added, refresh the supplier database
            if(rowsAffected == 1) {
                reloadSupplierListModel();
                suppliersController.setErrorSuccessMessage("SUCCESS! Supplier has been successfully added!", false);
            }
            else { suppliersController.setErrorSuccessMessage("ERROR! Supplier was not added successfully!", true); }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a supplier object and overwrites an existing supplier with a matching id
    public void updateSupplier(Supplier supplier) {
        try {
            // If a product with the provided id does not exist, don't proceed with the updating logic and let the user know
            Supplier checkSupplier = ISupplierDao.getSupplierById(supplier.getId());

            if(checkSupplier == null) {
                suppliersController.setErrorSuccessMessage("This id does not exist in the table. Please enter a supplier id that exists!", true);
                return;
            }

            int rowsAffected = ISupplierDao.updateSupplier(supplier.getId(), supplier);

            // If the product was successfully updated, refresh the product database
            if(rowsAffected == 1) {
                reloadSupplierListModel();
                suppliersController.setErrorSuccessMessage("SUCCESS! Supplier has been successfully updated.", false);
            }
            else {suppliersController.setErrorSuccessMessage("ERROR! Unable to update the supplier!", true); }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a product id and deletes the corresponding product with that id using a DAO method
    public void deleteSupplierById(String id) {
        try {
            // If a product with the provided id does not exist, don't proceed with the deleting logic and let the user know
            Supplier checkSupplier = ISupplierDao.getSupplierById(id);

            if(checkSupplier == null) {
                suppliersController.setErrorSuccessMessage("This id does not exist in the table. Please enter a supplier id that exists!", true);
                return;
            }

            int rowsAffected = ISupplierDao.deleteSupplierById(id);

            // If the product was deleted, refresh the product database
            if(rowsAffected == 1) {
                suppliersController.setErrorSuccessMessage("SUCCESS! Deleted matching supplier from the table", false);
                reloadSupplierListModel();
            }
            else {
                suppliersController.setErrorSuccessMessage("No suppliers were deleted. Please first check the table to see if a supplier  with your id exists!", true);
            }
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Getter for list of suppliers
    public ObservableList<Supplier> getObservableSupplierList() {
        return this.observableSupplierList;
    }

    // This method practically restarts the DAO. I assume this is for when the user decides to clear all filters and wants the original list back
    public void reloadSupplierListModel() {
        this.observableSupplierList.clear();
        populateSupplierList();
    }
}
