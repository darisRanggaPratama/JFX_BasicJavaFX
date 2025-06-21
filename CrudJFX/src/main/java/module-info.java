module com.tama.jfxcrud {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires java.sql;
    requires java.desktop;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.tama.jfxcrud to javafx.fxml;
    opens com.tama.jfxcrud.controller to javafx.fxml;
    opens com.tama.jfxcrud.view to javafx.fxml;

    exports com.tama.jfxcrud;
    exports com.tama.jfxcrud.model;
    exports com.tama.jfxcrud.controller;
    exports com.tama.jfxcrud.view;
}
