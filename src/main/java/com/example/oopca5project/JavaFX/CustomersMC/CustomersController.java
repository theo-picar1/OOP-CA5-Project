package com.example.oopca5project.JavaFX.CustomersMC;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.example.oopca5project.DTOs.Customer;

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

public class CustomersController {
    private CustomerModel customerModel;

    @FXML private Label modelText;
    @FXML private Label inputAreaText;


    /// References: [...](https://openjfx.io/javadoc/22/javafx.controls/javafx/scene/control/TableView.html)
    ///             [...](https://www.tutorialspoint.com/javafx/javafx_tableview.htm)
    @FXML private TableView<Customer> customerTableView; // This is where the products will show up

    // Below are the columns where the corresponding field data will go into
    @FXML private TableColumn<Customer, Integer> idColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> emailColumn;
    @FXML private TableColumn<Customer, Double> addressColumn;

    // These are the input field containers (HBox). This is for disappearing/appearing purposes depending on the button chosen
    @FXML private List<HBox> inputFields;
    @FXML private HBox idInputField;
    @FXML private HBox nameInputField;
    @FXML private HBox emailInputField;
    @FXML private HBox addressInputField;

    @FXML private Button submitButton;

    // These are the actual input fields. We will get the data from them using getText();
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;

    public CustomersController() {
        this.customerModel = new CustomerModel();
    }

    @FXML
    protected void initialize() {
        modelText.setText("All products have been successfully loaded!");

        customerModel.reloadCustomerListModel();

        // I add all the fields to the inputFields list to be able to loop through all of them at once
        inputFields = Arrays.asList(idInputField, nameInputField, emailInputField, addressInputField);
        // Then loop through the field so that I can set everything to initially be invisible
        for(HBox row : inputFields) {
            row.setVisible(false);
            row.setManaged(false); // Same as display: none. With just setVisible it will be like visibility = "hidden" in CSS
        }

        // Logic to put field data into their corresponding columns
        // For some reason, this needs to match the getter method and not the actual DTO fields. i.e, getId() would = "id", getSupplierId = "supplierId"
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        /// Reference: https://stackoverflow.com/questions/12933918/tableview-has-more-columns-than-specified
        customerTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // This is to get rid of the extra columns that JavaFX decides to randomly add

        customerTableView.setItems(customerModel.getObservableCustomerList());
    }

    // Method that displays all products again. Acts as a reset button if a user is searching or filtering
    @FXML
    protected void displayAllCustomersClick() {
        customerModel.reloadCustomerListModel();
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
    protected void showFindCustomerByIdFields() {
        inputAreaText.setText("Please enter the id of the customer you wish to find below:");
        submitButton.setOnAction(e -> findCustomerByIdClick());

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

    // Method tha will send the text (if any) of the id input field and call the findCustomerById method
    @FXML
    protected void findCustomerByIdClick() {
        String id = idField.getText();

        int convertedId = Integer.parseInt(id);

        // Make sure that the user is entering something. Otherwise, tell them.
        if( id==null || id.isEmpty() ) {
            return;
        }
        // Otherwise fill the table with just the single product
        else {
            customerModel.getSingleCustomerById(convertedId);
            customerTableView.setItems(customerModel.getObservableCustomerList());
        }
    }

    // Method to show the corresponding fields for adding a new product
    @FXML
    protected void showAddCustomerFields() {
        inputAreaText.setText("Please enter the details of your new customer below:");
        submitButton.setOnAction(e -> addNewCustomerClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new product with those details
    @FXML
    protected void addNewCustomerClick() {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailColumn.getText();
        String address = addressField.getText();

        int convertedId = Integer.parseInt(id);
        Customer customer = new Customer(convertedId, name, email, address);

        customerModel.addNewCustomer(customer);
        customerTableView.setItems(customerModel.getObservableCustomerList());
    }

    @FXML
    protected void editCustomerClick() {
        inputAreaText.setText("Please enter the changes of your new customer below:");
        submitButton.setOnAction(e -> updateCustomerClick());

        setFieldsVisibilityTrue();
    }

    // Method that will send the details from the TextFields and add the new product with those details
    @FXML
    protected void updateCustomerClick() {
        String id = idField.getText();
        String name = nameField.getText();
        String email = emailColumn.getText();
        String address = addressField.getText();

        int convertedId = Integer.parseInt(id);
        Customer customer = new Customer(convertedId, name, email, address);

        customerModel.updateCustomer(customer);
        customerTableView.setItems(customerModel.getObservableCustomerList());
    }

    @FXML
    protected void showDeleteCustomerFields() {
        inputAreaText.setText("Please enter the id of the customer you wish to delete below:");
        submitButton.setOnAction(e -> deleteCustomerClick());

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

    // Below method sends a product id to be deleted by another method within CustomersModel
    @FXML
    protected void deleteCustomerClick() {
        String id = idField.getText();
        int convertedId = Integer.parseInt(id);

        customerModel.deleteCustomerById(convertedId);
        customerTableView.setItems(customerModel.getObservableCustomerList());
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
