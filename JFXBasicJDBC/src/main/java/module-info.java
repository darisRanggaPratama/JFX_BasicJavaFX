module com.tama.basicjdbc {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    exports com.tama.basicjdbc.controller;
    opens com.tama.basicjdbc.controller to javafx.fxml;
    exports com.tama.basicjdbc.jdbc;
    opens com.tama.basicjdbc.jdbc to javafx.fxml;
    exports com.tama.basicjdbc.dao;
    opens com.tama.basicjdbc.dao to javafx.fxml;
    exports com.tama.basicjdbc.model;
    opens com.tama.basicjdbc.model to javafx.fxml;
    exports com.tama.basicjdbc.utility;
    opens com.tama.basicjdbc.utility to javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires mysql.connector.j;
    requires net.sf.jasperreports.core;
}