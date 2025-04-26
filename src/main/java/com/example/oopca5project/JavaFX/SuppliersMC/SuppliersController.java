package com.example.oopca5project.JavaFX.SuppliersMC;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.example.oopca5project.DTOs.Supplier;

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

public class SuppliersController {
    private SupplierModel supplierModel;

    @FXML private Label modelText;
    @FXML private Label inputAreaText;
    @FXML private Label errorSuccessMessage;

    /// References: [...](https://openjfx.io/javadoc/22/javafx.controls/javafx/scene/control/TableView.html)
    ///             [...](https://www.tutorialspoint.com/javafx/javafx_tableview.htm)
    @FXML private TableView<Supplier> supplierTableView; // This is where the suppliers will show up

    // Below are the columns where the corresponding field data will go into
    @FXML private TableColumn<Supplier, String> idColumn;
    @FXML private TableColumn<Supplier, String> nameColumn;
    @FXML private TableColumn<Supplier, String> phoneNoColumn;
    @FXML private TableColumn<Supplier, String> emailColumn;

    // These are the input field containers (HBox). This is for disappearing/appearing purposes depending on the button chosen
    @FXML private List<HBox> inputFields;
    @FXML private HBox idInputField;
    @FXML private HBox nameInputField;
    @FXML private HBox phoneNoInputField;
    @FXML private HBox emailInputField;

    @FXML private Button submitButton;

    // These are the actual input fields. We will get the data from them using getText();
    @FXML private List<TextField> fields;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField phoneNoField;
    @FXML private TextField emailField;

    public SuppliersController() {
        this.supplierModel = new SupplierModel(this);
    }

    @FXML
    protected void initialize() {
        modelText.setText("All suppliers have been successfully loaded!");

        supplierModel.reloadSupplierListModel();

        // I add all the fields to the inputFields list to be able to loop through all of them at once
        inputFields = Arrays.asList(idInputField, nameInputField, phoneNoInputField, emailInputField);
        // Then loop through the field so that I can set everything to initially be invisible
        for(HBox row : inputFields) {
            row.setVisible(false);
            row.setManaged(false); // Same as display: none. With just setVisible it will be like visibility = "hidden" in CSS
        }

        fields = Arrays.asList(idField, nameField, phoneNoField, emailField);

        errorSuccessMessage.setVisible(false);
        errorSuccessMessage.setManaged(false);

        // Logic to put field data into their corresponding columns
        // For some reason, this needs to match the getter method and not the actual DTO fields. i.e, getId() would = "id"
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneNoColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNo"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        /// Reference: https://stackoverflow.com/questions/12933918/tableview-has-more-columns-than-specified
        supplierTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // This is to get rid of the extra columns that JavaFX decides to randomly add

        supplierTableView.setItems(supplierModel.getObservableSupplierList());
    }

    // Method that displays all suppliers again. Acts as a reset button if a user is searching or filtering
    @FXML
    protected void displayAllSuppliersClick() {
        supplierModel.reloadSupplierListModel();
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
    protected void showFindSupplierByIdFields() {
        inputAreaText.setText("Please enter the supplier_id you wish to find below:");
        submitButton.setOnAction(e -> findSupplierByIdClick());

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

    // Method tha will send the text (if any) of the id input field and call the findSupplierById method
    @FXML
    protected void findSupplierByIdClick() {
        if(idField.getText().isEmpty()) {
            setErrorSuccessMessage("Please enter an id in the provided field!", true);
            return;
        }

        String id = idField.getText();

        supplierModel.getSingleSupplierById(id);
        supplierTableView.setItems(supplierModel.getObservableSupplierList());

    }

    // Method to show the corresponding fields for adding a new supplier
    @FXML 
    protected void showAddSupplierFields() {
        inputAreaText.setText("Please enter the details of your new supplier below:");
        submitButton.setOnAction(e -> addNewSupplierClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new supplier with those details
    @FXML
    protected void addNewSupplierClick() {
        String id = idField.getText();
        String name = nameField.getText();
        String phoneNo = phoneNoField.getText();
        String email = emailField.getText();

        // Check if every field has a value
        if(!allFieldsFilled()) {
            setErrorSuccessMessage("Please provide values for all the fields provided!", true);
            return;
        }

        // Check if the values are formatted correctly
        if(!allFieldsValid(id, name, phoneNo, email)) { return; }

        Supplier supplier = new Supplier(id, name, phoneNo, email);

        supplierModel.addNewSupplier(supplier);
        supplierTableView.setItems(supplierModel.getObservableSupplierList());
    }

    @FXML
    protected void showEditSupplierFields() {
        inputAreaText.setText("Please enter the id of the supplier you wish to update below:");
        submitButton.setOnAction(e -> editSupplierClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new product with those details
    @FXML
    protected void editSupplierClick() {
        String id = idField.getText();
        String name = nameField.getText();
        String phoneNo = phoneNoField.getText();
        String email = emailField.getText();

        // Check if every field has a value
        if(!allFieldsFilled()) {
            setErrorSuccessMessage("Please provide values for all the fields provided!", true);
            return;
        }

        // Check if the values are formatted correctly
        if(!allFieldsValid(id, name, phoneNo, email)) { return; }

        Supplier supplier = new Supplier(id, name, phoneNo, email);

        supplierModel.updateSupplier(supplier);
        supplierTableView.setItems(supplierModel.getObservableSupplierList());
    }

    @FXML
    protected void showDeleteSupplierFields() {
        inputAreaText.setText("Please enter the supplier_id you wish to delete below:");
        submitButton.setOnAction(e -> deleteSupplierClick());

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
    protected void deleteSupplierClick() {
        if(idField.getText().isEmpty()) {
            setErrorSuccessMessage("Please enter an id to delete!", true);
            return;
        }

        String id = idField.getText();

        supplierModel.deleteSupplierById(id);
        supplierTableView.setItems(supplierModel.getObservableSupplierList());
    }
    
    // Method that makes all the fields and their labels visible
    @FXML
    protected void setFieldsVisibilityTrue() {
        for(HBox field : inputFields) {
            field.setVisible(true);
            field.setManaged(true);
        }
    }

    // Helper method to set the label errorSuccessMessage with either a success or error message
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
    protected boolean allFieldsValid(String id, String name, String phone, String email) {
        if(id.length() > 25) {
            setErrorSuccessMessage("Supplier id cannot be longer than 25 characters!", true);
            return false;
        }
        else if(!id.matches("^Supplier\\d+$")) {
            setErrorSuccessMessage("Supplier id must follow default format!", true);
            return false;
        }
        else if(name.length() > 20) {
            setErrorSuccessMessage("Name cannot be longer than 20 characters!", true);
            return false;
        }
        /// Reference: https://www.sent.dm/resources/IE
        else if(!phone.matches("^08[3-9]\\d{7}$")) {
            setErrorSuccessMessage("Your phone number must follow the format of a valid Irish phone number!", true);
            return false;
        }
        /// Reference: https://www.baeldung.com/java-email-validation-regex
        else if(!email.matches("^(?=.{1,64}@)[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            setErrorSuccessMessage("Invalid email! Please try again!", true);
            return false;
        }

        return true;
    }
}
