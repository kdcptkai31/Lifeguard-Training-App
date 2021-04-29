module org.openjfx {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires commons.csv;
    requires commons.logging;
    requires sqlite.jdbc;
    requires xstream;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}