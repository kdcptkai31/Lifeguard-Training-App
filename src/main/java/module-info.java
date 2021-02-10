module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires commons.csv;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}