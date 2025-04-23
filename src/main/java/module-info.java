module com.example.oopca5project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.json;

    opens com.example.oopca5project to javafx.fxml;
    opens com.example.oopca5project.JavaFX to javafx.fxml;
    opens com.example.oopca5project.DTOs to javafx.base;
    opens com.example.oopca5project.JavaFX.Controllers to javafx.fxml;
    opens com.example.oopca5project.JavaFX.ProductsMC to javafx.fxml;
    opens com.example.oopca5project.JavaFX.SuppliersMC to javafx.fxml;
    opens com.example.oopca5project.JavaFX.CustomersMC to javafx.fxml;

    exports com.example.oopca5project;
    exports com.example.oopca5project.JavaFX;
    exports com.example.oopca5project.DTOs;
    exports com.example.oopca5project.JavaFX.Controllers;
    exports com.example.oopca5project.JavaFX.ProductsMC;
    exports com.example.oopca5project.JavaFX.SuppliersMC;
    exports com.example.oopca5project.JavaFX.CustomersMC;
}