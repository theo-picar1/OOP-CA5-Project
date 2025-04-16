package com.example.oopca5project.JavaFX;

import com.example.oopca5project.DTOs.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.scene.control.Label;

import java.io.IOException;

public class ModelsController {
    private ProductModel productModel;

    @FXML
    private Label modelText;

    @FXML
    private ListView<Product> productListView; // This is where the products will show up

    public ModelsController() {
        this.productModel = new ProductModel();
    }

    @FXML protected void displayAllProductsClick() {
        modelText.setText("displayAllProducts() was called...");

        productModel.reloadProductListModel();
        productListView.setItems(productModel.getObservableProductList());

        // This converts the productListView into a string that we can format ourselves. It will appear in the VBox with the id of productsListView
        productListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);

                if (empty || product == null) {
                    setText(null); // Set text to be empty / null since there are no products
                }
                else {
                    // Append all the rows with the product fields
                    setText(
                        product.getId() + ", " +
                        product.getDescription() + ", " +
                        product.getSize() + ", " +
                        product.getPrice() + ", " +
                        product.getSupplierId()
                    );
                }
            }
        });
    }

    @FXML
    protected void onGoBackClick() {
        // Below is code logic to change the current view of the GUI when the user clicks the corresponding button
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/oopca5project/menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 300);

            Stage stage = (Stage) modelText.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
