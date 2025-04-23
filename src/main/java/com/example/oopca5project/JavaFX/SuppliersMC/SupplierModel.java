package com.example.oopca5project.JavaFX.SuppliersMC;

import java.util.List;

import com.example.oopca5project.DAOs.MySqlSupplierDao;
import com.example.oopca5project.DAOs.SupplierDaoInterface;
import com.example.oopca5project.DTOs.Product;
import com.example.oopca5project.DTOs.Supplier;
import com.example.oopca5project.Exceptions.DaoException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SupplierModel {
    private List<Supplier> suppliersList;

    ObservableList<Supplier> observableSupplierList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static SupplierDaoInterface ISupplierDao = new MySqlSupplierDao(); // To access the DAO methods of course

    // Constructor - populates model with data from DAO
    public SupplierModel() {
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
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method will call the DAO method to add a new supplier to the database and show the new supplier list in JavaFX
    public void addNewSupplier(Supplier supplier) {
        try {
            int rowsAffected = ISupplierDao.addSupplier(supplier);

            // If the new supplier was added, refresh the supplier database
            if(rowsAffected == 1) {
                reloadSupplierListModel();
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a supplier object and overwrites an existing supplier with a matching id
    public void updateSupplier(Supplier supplier) {
        try {
            String id = supplier.getId();

            int rowsAffected = ISupplierDao.updateSupplier(id, supplier);

            // If the product was successfully updated, refresh the product database
            if(rowsAffected == 1) {
                reloadSupplierListModel();
            }
        }
        catch(DaoException e) {
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
