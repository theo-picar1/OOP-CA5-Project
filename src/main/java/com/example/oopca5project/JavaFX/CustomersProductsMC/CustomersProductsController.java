package com.example.oopca5project.JavaFX.CustomersProductsMC;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.example.oopca5project.DTOs.CustomersProducts;

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

public class CustomersProductsController {
    private CustomersProductsModel customersProductsModel;

    @FXML private Label modelText;
    @FXML private Label inputAreaText;


    /// References: [...](https://openjfx.io/javadoc/22/javafx.controls/javafx/scene/control/TableView.html)
    ///             [...](https://www.tutorialspoint.com/javafx/javafx_tableview.htm)
    @FXML private TableView<CustomersProducts> customersProductsTableView; // This is where the customersProducts will show up

    // Below are the columns where the corresponding field data will go into
    @FXML private TableColumn<CustomersProducts, Integer> customerIdColumn;
    @FXML private TableColumn<CustomersProducts, String> productIdColumn;
    @FXML private TableColumn<CustomersProducts, String> quantityColumn;

    // These are the input field containers (HBox). This is for disappearing/appearing purposes depending on the button chosen
    @FXML private List<HBox> inputFields;
    @FXML private HBox customerIdInputField;
    @FXML private HBox productIdInputField;
    @FXML private HBox quantityInputField;

    @FXML private Button submitButton;

    // These are the actual input fields. We will get the data from them using getText();
    @FXML private TextField productIdField;
    @FXML private TextField customerIdField;
    @FXML private TextField quantityField;

    public CustomersProductsController() {
        this.customersProductsModel = new CustomersProductsModel();
    }

    @FXML
    protected void initialize() {
        modelText.setText("All customersProducts have been successfully loaded!");

        customersProductsModel.reloadCustomersProductsListModel();

        // I add all the fields to the inputFields list to be able to loop through all of them at once
        inputFields = Arrays.asList(customerIdInputField, productIdInputField, quantityInputField);
        // Then loop through the field so that I can set everything to initially be invisible
        for(HBox row : inputFields) {
            row.setVisible(false);
            row.setManaged(false); // Same as display: none. With just setVisible it will be like visibility = "hidden" in CSS
        }

        // Logic to put field data into their corresponding columns
        // For some reason, this needs to match the getter method and not the actual DTO fields. i.e, getId() would = "id"
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        /// Reference: https://stackoverflow.com/questions/12933918/tableview-has-more-columns-than-specified
        customersProductsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // This is to get rid of the extra columns that JavaFX decides to randomly add

        customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
    }

    // Method that displays all customersProducts again. Acts as a reset button if a user is searching or filtering
    @FXML
    protected void displayAllCustomersProductsClick() {
        customersProductsModel.reloadCustomersProductsListModel();
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
    protected void showFindCustomersProductsByIdFields() {
        inputAreaText.setText("Please enter the customersProducts_id you wish to find below:");
        submitButton.setOnAction(e -> findCustomersProductsByIdClick());

        // Make all the fields and their labels invisible, not including the ID field
        for(HBox field : inputFields) {
            if(field == customerIdInputField || field == productIdInputField) {
                field.setVisible(true);
                field.setManaged(true);
            }
            else {
                field.setVisible(false);
                field.setManaged(false);
            }
        }
    }

    // Method tha will send the text (if any) of the id input field and call the findCustomersProductsById method
    @FXML
    protected void findCustomersProductsByIdClick() {
        String productid = productIdField.getText();
        String customerid = customerIdField.getText();

        // Make sure that the user is entering something. Otherwise, tell them.
        if( productid==null || productid.isEmpty() || customerid==null || customerid.isEmpty() ) {
            return;
        }
        // Otherwise fill the table with just the single customersProducts
        else {
            customersProductsModel.getSingleCustomersProductsById(productid, Integer.parseInt(customerid));
            customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
        }
    }

    // Method to show the corresponding fields for adding a new customersProducts
    @FXML 
    protected void showAddCustomersProductsFields() {
        inputAreaText.setText("Please enter the details of your new customersProducts below:");
        submitButton.setOnAction(e -> addNewCustomersProductsClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new customersProducts with those details
    @FXML
    protected void addNewCustomersProductsClick() {
        String productId = productIdField.getText();
        String customerId = customerIdField.getText();
        String quantity = quantityField.getText();

        CustomersProducts customersProducts = new CustomersProducts(Integer.parseInt(customerId), productId, Integer.parseInt(quantity));

        // Make sure that the user is at the bare minimum entering an id to their new customersProducts
        if( productId==null || productId.isEmpty() || customerId==null || customerId.isEmpty() ) {
            return;
        }
        else {
            customersProductsModel.addNewCustomersProducts(customersProducts);
            customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
        }
    }

    @FXML
    protected void showEditCustomersProductsFields() {
        inputAreaText.setText("Please enter the id of the customersProducts you wish to update below:");
        submitButton.setOnAction(e -> editCustomersProductsClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new product with those details
    @FXML
    protected void editCustomersProductsClick() {
        String productId = productIdField.getText();
        String customerId = customerIdField.getText();
        String quantity = quantityField.getText();

        CustomersProducts customersProducts = new CustomersProducts(Integer.parseInt(customerId), productId, Integer.parseInt(quantity));

        customersProductsModel.updateCustomersProducts(customersProducts);
        customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
    }

    @FXML
    protected void deleteCustomersProductsClick() {
        inputAreaText.setText("Please enter the customersProducts_id you wish to delete below:");

        // Make all the fields and their labels visible (except quantity field)
        for (HBox field : inputFields) {
            if (field == customerIdInputField || field == productIdInputField) {
                field.setVisible(true);
                field.setManaged(true);
            } else {
                field.setVisible(false);
                field.setManaged(false);
            }
        }

        // Set the submit button to trigger the deletion
        submitButton.setOnAction(e -> deleteCustomerClick());
    }

    // Below method sends a product id to be deleted by another method within CustomersModel
    @FXML
    protected void deleteCustomerClick() {
        String productId = productIdField.getText();
        String customerId = customerIdField.getText();

        customersProductsModel.deleteCustomersProductsById(Integer.parseInt(customerId), productId);
        customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
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
