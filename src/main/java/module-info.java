module com.example.chessfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires javafx.graphics;
    requires java.desktop;


    opens com.example.chessfx to javafx.fxml;
    exports com.example.chessfx;
    exports com.example.chessfx.Controller;

}