module com.example.oopca5project {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires org.json;

    opens com.example.oopca5project to javafx.fxml;
    exports com.example.oopca5project;
}