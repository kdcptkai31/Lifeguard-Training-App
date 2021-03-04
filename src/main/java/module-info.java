module org.openjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires commons.csv;
    requires sqlite.jdbc;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}