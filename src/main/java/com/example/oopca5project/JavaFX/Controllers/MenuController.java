package com.example.oopca5project.JavaFX.Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MenuController {
    @FXML
    private Label menuText;

    @FXML
    private Label menuOptions;

    @FXML
    protected void onProductsClick() {
        // Below is code logic to change the current view of the GUI when the user clicks the corresponding button
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/oopca5project/product-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1340, 620);

            Stage stage = (Stage) menuText.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onSuppliersClick() {
        // menuText.setText("You chose the Suppliers table! Choose one of the following");

        // menuOptions.setText(
        //     "1. Display all suppliers\n" +
        //     "2. Find supplier by ID\n" +
        //     "3. Find supplier by product ID\n" +
        //     "4. Add new supplier\n" +
        //     "5. Update existing supplier by ID\n" +
        //     "6. Delete supplier by ID\n"
        // );
        // Below is code logic to change the current view of the GUI when the user clicks the corresponding button
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/oopca5project/supplier-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1340, 620);

            Stage stage = (Stage) menuText.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onCustomersClick() {
        menuText.setText("You chose the Customers table! Choose one of the following");

        menuOptions.setText(
            "1. Display all customers\n" +
            "2. Find customer by ID\n" +
            "3. Add new customer\n" +
            "4. Update existing customer by ID\n" +
            "5. Delete customer by ID\n"
        );
    }

    @FXML
    protected void onCustomersProductsClick() {
        menuText.setText("You chose the CustomersProducts table! Choose one of the following");

        menuOptions.setText(
            "1. Display all customer's products\n" +
            "2. Find customer's product by customer and product ID\n" +
            "3. Add new customer product\n" +
            "4. Update existing customer by customer and product ID\n" +
            "5. Delete customer by customer and product ID\n"
        );
    }

    @FXML
    protected void onGoBackClick() {
        // Below is code logic to change the current view of the GUI when the user clicks the corresponding button
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/oopca5project/hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 320, 300);

            Stage stage = (Stage) menuText.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
