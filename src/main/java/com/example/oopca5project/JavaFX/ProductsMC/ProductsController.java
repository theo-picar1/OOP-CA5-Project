package com.example.oopca5project.JavaFX.ProductsMC;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.example.oopca5project.DTOs.Product;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ProductsController {
    private ProductModel productModel;

    @FXML private Label modelText;
    @FXML private Label inputAreaText;
    @FXML private Label errorSuccessMessage;

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

    // These are the actual input fields. We will get the data from them using getText();
    @FXML private List<TextField> fields; // Error checking purposes
    @FXML private TextField idField;
    @FXML private TextField descriptionField;
    @FXML private TextField sizeField;
    @FXML private TextField priceField;
    @FXML private TextField supplierIdField;

    public ProductsController() {
        this.productModel = new ProductModel(this);
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

        errorSuccessMessage.setVisible(false);
        errorSuccessMessage.setManaged(false);

        // Adding textFields to List. This is for error checking to see if user is filling in all textFields
        fields = Arrays.asList(idField, descriptionField, sizeField, priceField, supplierIdField);

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
        submitButton.setOnAction(e -> findProductByIdClick());

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
        // Make sure that the user is entering something. Otherwise, tell them.
        if( idField.getText().isEmpty() ) {
            setErrorSuccessMessage("Please enter an id in the provided field!", true);
            return;
        }

        String id = idField.getText();

        productModel.getSingleProductById(id);
        productTableView.setItems(productModel.getObservableProductList());
    }

    // Method to show the corresponding fields for adding a new product
    @FXML 
    protected void showAddProductFields() {
        inputAreaText.setText("Please enter the details of your new product below:");
        submitButton.setOnAction(e -> addNewProductClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new product with those details
    @FXML
    protected void addNewProductClick() {
        String id = idField.getText();
        String description = descriptionField.getText();
        String size = sizeField.getText();
        String price = priceField.getText();
        String supplierId = supplierIdField.getText();
        
        if(!allFieldsValid(id, description, size, supplierId)) {
            return;
        }
        
        if(description.length() > 30) {
            setErrorSuccessMessage("Description cannot be longer than 30 characters!", true);
        }

        if(!allFieldsFilled()) {
            setErrorSuccessMessage("Please enter values for all the fields provided!", true);
            return;
        }

        if(!validateDouble(price)) {
            setErrorSuccessMessage("You did not enter a valid price number. Please try again!", true);
            return;
        }

        double convertedPrice = Double.parseDouble(price);
        Product product = new Product(id, description, size, convertedPrice, supplierId);

        productModel.addNewProduct(product);
        productTableView.setItems(productModel.getObservableProductList());
    }

    @FXML
    protected void editProductClick() {
        inputAreaText.setText("Please enter the changes of your new product below:");
        submitButton.setOnAction(e -> updateProductClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new product with those details
    @FXML
    protected void updateProductClick() {
        String id = idField.getText();
        String description = descriptionField.getText();
        String size = sizeField.getText();
        String price = priceField.getText();
        String supplierId = supplierIdField.getText();

        if(!allFieldsValid(id, description, size, supplierId)) {
            return;
        }

        if(!allFieldsFilled()) {
            setErrorSuccessMessage("Please enter values for all the fields provided!", true);
            return;
        }

        if(!validateDouble(price)) {
            setErrorSuccessMessage("You did not enter a valid price number. Please try again!", true
            );
            return;
        }

        double convertedPrice = Double.parseDouble(price);
        Product product = new Product(id, description, size, convertedPrice, supplierId);

        productModel.updateProduct(product);
        productTableView.setItems(productModel.getObservableProductList());
    }

    @FXML
    protected void showDeleteProductFields() {
        inputAreaText.setText("Please enter the product_id you wish to delete below:");
        submitButton.setOnAction(e -> deleteProductClick());

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

    // Below method sends a product id to be deleted by another method within ProductsModel
    @FXML
    protected void deleteProductClick() {
        if(idField.getText().isEmpty()) {
            setErrorSuccessMessage("Please enter an id to delete!", true);
            return;
        }

        String id = idField.getText();

        productModel.deleteProductById(id);
        productTableView.setItems(productModel.getObservableProductList());
    }
    
    // Method that makes all the fields and their labels visible
    @FXML
    protected void setFieldsVisibilityTrue() {
        for(HBox field : inputFields) {
            field.setVisible(true);
            field.setManaged(true);
        }
    }

    // Below 4 methods will hide and show an error/success message to the user in the GUI
    @FXML
    protected void setErrorSuccessMessage(String message, boolean error) {
        if(error) {
            errorSuccessMessage.setStyle("-fx-text-fill: red;");
        }
        else {
            errorSuccessMessage.setStyle("-fx-text-fill: green;");
        }

        errorSuccessMessage.setText(message);
        errorSuccessMessage.setVisible(true);
        errorSuccessMessage.setManaged(true);
    }

    @FXML
    protected void hideErrorSuccessMessage() {
        errorSuccessMessage.setText("");
        errorSuccessMessage.setVisible(false);
        errorSuccessMessage.setManaged(false);
    }

    // Helper function that user enters a valid double, since text fields allows strings
    /// Reference: [...](https://stackoverflow.com/questions/3543729/how-to-check-that-a-string-is-parseable-to-a-double)
    @FXML
    protected boolean validateDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    // Helper method to see if user entered all the provided fields
    @FXML
    protected boolean allFieldsFilled() {
        for(TextField input : fields) {
            if(input.getText().isEmpty()) {
                return false;
            }
        }

        return true;
    }
    
    // Helper mthod to check if all the values provided, besides double,  are formatted correctly before proceeding
    @FXML 
    protected boolean allFieldsValid(String id, String description, String size, String supplierId) {
        if(id.length() > 20) {
            setErrorSuccessMessage("Product id cannot be longer than 20 characters!", true);
            return false;
        }
        else if(description.length() > 30) {
            setErrorSuccessMessage("Description cannot be longer than 30 characters!", true);
            return false;
        }
        else if(!size.matches("\\d+cmx\\d+cm")) {
            setErrorSuccessMessage("Size must follow the format '10cmx10cm', with cm only being allowed as dimensions!", true);
            return false;
        }
        else if(supplierId.length() > 25) {
            setErrorSuccessMessage("Product id cannot be longer than 25 characters!", true);
            return false;
        }

        return true;
    }
}
