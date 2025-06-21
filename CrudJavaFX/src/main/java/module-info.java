module com.tama.crudxmljavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    // Database and file processing
    requires java.sql;
    requires mysql.connector.j;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires com.opencsv;

    opens com.tama.crudxmljavafx to javafx.fxml;
    opens com.tama.crudxmljavafx.controller to javafx.fxml;
    opens com.tama.crudxmljavafx.model to javafx.base;

    exports com.tama.crudxmljavafx;
    exports com.tama.crudxmljavafx.controller;
    exports com.tama.crudxmljavafx.model;
}