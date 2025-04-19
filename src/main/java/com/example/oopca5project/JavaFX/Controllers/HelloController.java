package com.example.oopca5project.JavaFX.Controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

// This class handles interaction logic like button clicks. i.e what will the button do when clicked
public class HelloController {
    @FXML
    private Label welcomeText; // This is found in hello-view.fxml. It's content will be replaced depending on what's clicked

    @FXML
    protected void onStartClick() {
        // Below is code logic to change the current view of the GUI when the user clicks the corresponding button
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/oopca5project/menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 720, 480);

            Stage stage = (Stage) welcomeText.getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onExitClick() {
        System.exit(0);
    }
}
