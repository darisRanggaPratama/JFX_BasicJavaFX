module com.tama.mysqljfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires javafx.base;
    requires net.sf.jasperreports.core;


    opens com.tama.mysqljfx to javafx.fxml;
    exports com.tama.mysqljfx;
    exports com.tama.mysqljfx.controller;
    opens com.tama.mysqljfx.controller to javafx.fxml;
    opens com.tama.mysqljfx.entity to javafx.base;
}