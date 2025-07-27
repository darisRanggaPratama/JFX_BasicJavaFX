module com.tama.customer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires com.opencsv;
    requires jasperreports;

    opens com.tama.customer to javafx.fxml;
    opens com.tama.customer.model to jasperreports;
    exports com.tama.customer;
    exports com.tama.customer.model;
    exports com.tama.customer.view;
    exports com.tama.customer.service;
    exports com.tama.customer.dao;
    exports com.tama.customer.config;
}
