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
    @FXML private Label errorSuccessMessage;

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
        this.customersProductsModel = new CustomersProductsModel(this);
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
        errorSuccessMessage.setVisible(false);
        errorSuccessMessage.setManaged(false);

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void showFindCustomersProductsByIdFields() {
        inputAreaText.setText("Please enter the customersProducts_id you wish to find below:");
        submitButton.setOnAction(e -> findCustomersProductsByIdClick());

        // Make all the fields and their labels invisible, not including the ID field
        for (HBox field : inputFields) {
            if (field == customerIdInputField || field == productIdInputField) {
                field.setVisible(true);
                field.setManaged(true);
            } else {
                field.setVisible(false);
                field.setManaged(false);
            }
        }
    }

    // Method that will send the text (if any) of the id input field and call the findCustomersProductsById method
    @FXML
    protected void findCustomersProductsByIdClick() {
        String productid = productIdField.getText();
        String customerid = customerIdField.getText();

        // Make sure that the user is entering something. Otherwise, tell them.
        if (productid.isEmpty() || customerid.isEmpty()) {
            setErrorSuccessMessage("Please enter values for both product ID and customer ID!", true);
            return;
        }
        // Otherwise fill the table with just the single customersProducts
        else {
            if (!validateInteger(customerid)) {
                setErrorSuccessMessage("customer ID must be a valid integer!", true);
                return;
            }

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


        if (!allFieldsFilled()) {
            setErrorSuccessMessage("Please enter values for all fields!", true);
            return;
        }

        // Make sure that the user is at the bare minimum entering an id to their new customersProducts
        if (productId.isEmpty() || customerId.isEmpty()) {
            setErrorSuccessMessage("Please enter values for both product ID and customer ID!", true);
            return;
        }

        if (!validateInteger(customerId)) {
            setErrorSuccessMessage("Customer ID must be a valid integer!", true);
            return;
        }

        if (!validateInteger(quantity)) {
            setErrorSuccessMessage("Quantity must be a valid integer!", true);
            return;
        } else {
            CustomersProducts customersProducts = new CustomersProducts(Integer.parseInt(customerId), productId, Integer.parseInt(quantity));
            customersProductsModel.addNewCustomersProducts(customersProducts);
            customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
        }
    }

    @FXML
    protected void showEditCustomersProductsFields() {
        inputAreaText.setText("Please enter the Customer ID, Product ID, and Quantity below to edit:");

        // Make the necessary fields visible for editing (customer ID, product ID, and quantity)
        for (HBox field : inputFields) {
            if (field == customerIdInputField || field == productIdInputField || field == quantityInputField) {
                field.setVisible(true);
                field.setManaged(true);
            } else {
                field.setVisible(false);
                field.setManaged(false);
            }
        }

        // Set the submit button to trigger the update action
        submitButton.setOnAction(e -> EditCustomersProductsSubmit());
    }


    @FXML
    protected void EditCustomersProductsSubmit() {
        String productId = productIdField.getText();
        String customerId = customerIdField.getText();
        String quantity = quantityField.getText();

        if (!allFieldsFilled()) {
            setErrorSuccessMessage("Please enter values for all fields!", true);
            return;
        }

        if (!validateInteger(quantity)) {
            setErrorSuccessMessage("Quantity must be a valid integer!", true);
            return;
        }

        if (!validateInteger(customerId)) {
            setErrorSuccessMessage("customer ID must be a valid integer!", true);
            return;
        }

        CustomersProducts customersProducts = new CustomersProducts(Integer.parseInt(customerId), productId, Integer.parseInt(quantity));
        customersProductsModel.updateCustomersProducts(customersProducts);
        customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
    }

    @FXML
    protected void showDeleteCustomersProductsFields() {
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
        submitButton.setOnAction(e -> deleteCustomersProductsClick());
    }

    // Below method sends a product id to be deleted by another method within CustomersModel
    @FXML
    protected void deleteCustomersProductsClick() {
        String productId = productIdField.getText();
        String customerId = customerIdField.getText();

        if (productId.isEmpty() || customerId.isEmpty()) {
            setErrorSuccessMessage("Please enter both product ID and customer ID to delete!", true);
            return;
        }

        if (!validateInteger(customerId)) {
            setErrorSuccessMessage("customer ID must be a valid integer!", true);
            return;
        }

        customersProductsModel.deleteCustomersProductsById(Integer.parseInt(customerId), productId);
        customersProductsTableView.setItems(customersProductsModel.getObservableCustomersProductsList());
    }

    @FXML
    protected void setErrorSuccessMessage(String message, boolean error) {
        if (error) {
            errorSuccessMessage.setStyle("-fx-text-fill: red;");
        } else {
            errorSuccessMessage.setStyle("-fx-text-fill: green;");
        }

        errorSuccessMessage.setText(message);
        errorSuccessMessage.setVisible(true);
        errorSuccessMessage.setManaged(true);
    }

    @FXML
    protected void setFieldsVisibilityTrue() {
        for (HBox field : inputFields) {
            field.setVisible(true);
            field.setManaged(true);
        }
    }

    @FXML
    protected boolean allFieldsFilled() {
        return !productIdField.getText().isEmpty() && !customerIdField.getText().isEmpty() && !quantityField.getText().isEmpty();
    }

    @FXML
    protected boolean validateInteger(String quantity) {
        try {
            Integer.parseInt(quantity);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
