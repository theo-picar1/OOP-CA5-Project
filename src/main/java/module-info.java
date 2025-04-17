module com.example.oopca5project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.json;

    opens com.example.oopca5project to javafx.fxml;
    opens com.example.oopca5project.JavaFX to javafx.fxml;
    opens com.example.oopca5project.DTOs to javafx.base;

    exports com.example.oopca5project;
    exports com.example.oopca5project.JavaFX;
    exports com.example.oopca5project.DTOs;
}