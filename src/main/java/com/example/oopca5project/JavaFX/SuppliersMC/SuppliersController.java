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
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField phoneNoField;
    @FXML private TextField emailField;

    public SuppliersController() {
        this.supplierModel = new SupplierModel();
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
        String id = idField.getText();

        // Make sure that the user is entering something. Otherwise, tell them.
        if( id==null || id.isEmpty() ) {
            return;
        }
        // Otherwise fill the table with just the single supplier
        else {
            supplierModel.getSingleSupplierById(id);
            supplierTableView.setItems(supplierModel.getObservableSupplierList());
        }
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

        Supplier supplier = new Supplier(id, name, phoneNo, email);

        // Make sure that the user is at the bare minimum entering an id to their new supplier
        if( (id==null || id.isEmpty())) {
            return;
        }
        else {
            supplierModel.addNewSupplier(supplier);
            supplierTableView.setItems(supplierModel.getObservableSupplierList());
        }
    }

    @FXML
    protected void editSupplierClick() {
        inputAreaText.setText("Please enter the details of your edited supplier below:");

        setFieldsVisibilityTrue();
    }

    @FXML
    protected void deleteSupplierClick() {
        inputAreaText.setText("Please enter the supplier_id you wish to delete below:");

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
