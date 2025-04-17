package com.example.oopca5project.JavaFX;

import com.example.oopca5project.DTOs.Product;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProductsController {
    private ProductModel productModel;

    @FXML private Label modelText;
    @FXML private Label inputAreaText;


    /// References: [...](https://openjfx.io/javadoc/22/javafx.controls/javafx/scene/control/TableView.html)
    ///             [...](https://www.tutorialspoint.com/javafx/javafx_tableview.htm)
    @FXML private TableView<Product> productTableView; // This is where the products will show up

    // Below are the columns where the corresponding field data will go into
    @FXML private TableColumn<Product, String> idColumn;
    @FXML private TableColumn<Product, String> descriptionColumn;
    @FXML private TableColumn<Product, String> sizeColumn;
    @FXML private TableColumn<Product, Double> priceColumn;
    @FXML private TableColumn<Product, String> supplierIdColumn;

    // These are the input field containers (HBox). This is for disappearing/appearing purposes depending on the button chosen
    @FXML private List<HBox> inputFields;
    @FXML private HBox idInputField;
    @FXML private HBox descriptionInputField;
    @FXML private HBox sizeInputField;
    @FXML private HBox priceInputField;
    @FXML private HBox supplierIdInputField;

    @FXML private Button submitButton;

    // These are the actually input fields. We will get the data from them using getText();
    @FXML
    private TextField idField;

    public ProductsController() {
        this.productModel = new ProductModel();
    }

    @FXML
    protected void initialize() {
        modelText.setText("All products have been successfully loaded!");

        productModel.reloadProductListModel();

        // I add all the fields to the inputFields list to be able to loop through all of them at once
        inputFields = Arrays.asList(idInputField, descriptionInputField, sizeInputField, priceInputField, supplierIdInputField);
        // Then loop through the field so that I can set everything to initially be invisible
        for(HBox row : inputFields) {
            row.setVisible(false);
            row.setManaged(false); // Same as display: none. With just setVisible it will be like visibility = "hidden" in CSS
        }

        // Logic to put field data into their corresponding columns
        // For some reason, this needs to match the getter method and not the actual DTO fields. i.e, getId() would = "id", getSupplierId = "supplierId"
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        /// Reference: https://stackoverflow.com/questions/12933918/tableview-has-more-columns-than-specified
        productTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // This is to get rid of the extra columns that JavaFX decides to randomly add

        productTableView.setItems(productModel.getObservableProductList());
    }

    // Method that displays all products again. Acts as a reset button if a user is searching or filtering
    @FXML
    protected void displayAllProductsClick() {
        productModel.reloadProductListModel();
    }

    // Method that will return the user to the main menu (where they can choose table)
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
    
    @FXML 
    protected void showFindProductByIdFields() {
        inputAreaText.setText("Please enter the product_id you wish to find below:");
        submitButton.setOnAction(e -> sendFindProductByIdClick());

        // Make all the fields and their labels invisible, not including the ID field
        for(HBox field : inputFields) {
            if(field == idInputField) {
                field.setVisible(true);
                field.setManaged(true);
            }
            else {
                field.setVisible(false);
                field.setManaged(false);
            }
        }
    }

    // Method tha will send the text (if any) of the id input field and call the findProductById method
    @FXML
    protected void findProductByIdClick() {
        String id = idField.getText();

        // Make sure that the user is entering something. Otherwise, tell them.
        if( id==null || id.isEmpty() ) {
            return;
        }
        // Otherwise fill the table with just the single product
        else {
            productModel.getSingleProductById(id);
            productTableView.setItems(productModel.getObservableProductList());
        }
    }
    
    @FXML 
    protected void addProductClick() {
        inputAreaText.setText("Please enter the details of your new product below:");

        setFieldsVisibilityTrue();
    }

    @FXML
    protected void editProductClick() {
        inputAreaText.setText("Please enter the details of your edited product below:");

        setFieldsVisibilityTrue();
    }

    @FXML
    protected void deleteProductClick() {
        inputAreaText.setText("Please enter the product_id you wish to delete below:");

        // Make all the fields and their labels invisible, not including the ID field
        for(HBox field : inputFields) {
            if(field == idInputField) {
                field.setVisible(true);
                field.setManaged(true);
            }
            else {
                field.setVisible(false);
                field.setManaged(false);
            }
        }
    }
    
    // Method that makes all the fields and their labels visible
    @FXML
    protected void setFieldsVisibilityTrue() {
        for(HBox field : inputFields) {
            field.setVisible(true);
            field.setManaged(true);
        }
    }
}
