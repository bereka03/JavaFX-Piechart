module com.example.quiz {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.quiz5 to javafx.fxml;
    exports com.example.quiz5;
}