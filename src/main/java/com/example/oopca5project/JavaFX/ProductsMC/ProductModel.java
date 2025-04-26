package com.example.oopca5project.JavaFX.ProductsMC;

import com.example.oopca5project.DAOs.MySqlProductDao;
import com.example.oopca5project.DAOs.ProductDaoInterface;
import com.example.oopca5project.DTOs.Product;

import com.example.oopca5project.DAOs.MySqlSupplierDao;
import com.example.oopca5project.DAOs.SupplierDaoInterface;
import com.example.oopca5project.DTOs.Supplier;

import com.example.oopca5project.Exceptions.DaoException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class ProductModel {
    private List<Product> productsList;
    private ProductsController productsController;

    ObservableList<Product> observableProductList; // JavaFX will only listen to changes from this. A regular List won't cut it apparently

    static ProductDaoInterface IProductDao = new MySqlProductDao(); // To access the DAO methods, of course
    static SupplierDaoInterface ISupplierDao= new MySqlSupplierDao(); // For error checking purposes...

    public ProductModel(ProductsController productsController) {
        this.productsController = productsController;
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
                productsController.setErrorSuccessMessage("SUCCESS! Found product with matching ID!", false);
            }
            // Otherwise calla method in the ProductsController to let the user know that this productId does not exist!
            else {
                productsController.setErrorSuccessMessage("No product has been found with an ID of '" +productId+ "'. Please try again!", true);
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method will call the DAO method to add a new product to the database and show the new product list in JavaFX
    public void addNewProduct(Product product) {
        try {
            // If a product with the provided already exists, don't proceed with the adding logic and let the user know
            Product checkProduct = IProductDao.getProductById(product.getId());

            if(checkProduct != null) {
                productsController.setErrorSuccessMessage("A product with this id already exists. Please try again!", true);
                return;
            }

            // Check if the id of the provided supplier exists in the supplier table before moving on
            Supplier checkSupplier = ISupplierDao.getSupplierById(product.getSupplierId());

            if(checkSupplier == null) {
                productsController.setErrorSuccessMessage("The supplier id you provided does not exist in the Supplier's table. Please add it before proceeding!", true);
                return;
            }

            // Continue on with the logic if none of the if statements above were true
            int rowsAffected = IProductDao.addProduct(product);

            // If the new product was added, refresh the product database
            if(rowsAffected == 1) {
                reloadProductListModel();
                productsController.setErrorSuccessMessage("SUCCESS! Added product to the table!", false);
            }
            else {
                productsController.setErrorSuccessMessage("Error! Something went wrong when trying to add the product, please try again!", true);
            }
        }
        catch(DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a product id and deletes the corresponding product with that id using a DAO method
    public void deleteProductById(String id) {
        try {
            // If a product with the provided id does not exist, don't proceed with the deleting logic and let the user know
            Product checkProduct = IProductDao.getProductById(id);

            if(checkProduct == null) {
                productsController.setErrorSuccessMessage("This id does not exist in the table. Please enter a product id that exists!", true);
                return;
            }

            int rowsAffected = IProductDao.deleteProductById(id);

            // If the product was deleted, refresh the product database
            if(rowsAffected == 1) {
                productsController.setErrorSuccessMessage("SUCCESS! Deleted matching product from the table", false);
                reloadProductListModel();
            }
            else {
                productsController.setErrorSuccessMessage("No products were deleted. Please first check the table to see if a product with your id exists!", true);
            }
        }
        catch (DaoException e) {
            e.printStackTrace();
        }
    }

    // Below method takes in a product object and overwrites an existing product with a matching id
    public void updateProduct(Product product) {
        try {
            // If a product with the provided id does not exist, don't proceed with the updating logic and let the user know
            Product checkProduct = IProductDao.getProductById(product.getId());

            if(checkProduct == null) {
                productsController.setErrorSuccessMessage("Cannot update a product that does not exist. Please try again!", true);
                return;
            }

            String id = product.getId();

            int rowsAffected = IProductDao.updateProduct(id, product);

            // If the product was successfully updated, refresh the product database
            if(rowsAffected == 1) {
                productsController.setErrorSuccessMessage("SUCCESS! Updated matching product in the table", false);
                reloadProductListModel();
            }
            else {
                productsController.setErrorSuccessMessage("FATAL ERROR! Something went wrong when trying to update this product", true);
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
