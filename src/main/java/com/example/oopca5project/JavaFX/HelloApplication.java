package com.example.oopca5project.JavaFX;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// ***** ADD COMMENTS. THIS IS SO WE ALL UNDERSTAND EACH OTHER'S CODE *****

// This is where we actually run the GUI. This class sets up the stage and scene of the GUI
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/oopca5project/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 300); // Width and height of the GUI

        stage.setTitle("OOP CA5 PROJECT"); // Title of GUI
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
