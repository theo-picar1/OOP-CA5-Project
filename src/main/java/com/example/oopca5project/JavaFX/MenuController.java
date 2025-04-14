package com.example.oopca5project.JavaFX;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Label menuText;

    @FXML
    protected void onProductsClick() {
        menuText.setText("You chose the Products table!");
    }

    @FXML
    protected void onSuppliersClick() {
        menuText.setText("You chose the Suppliers table!");
    }

    @FXML
    protected void onCustomersClick() {
        menuText.setText("You chose the Customers table!");
    }

    @FXML
    protected void onCustomersProductsClick() {
        menuText.setText("You chose the CustomersProducts table!");
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
