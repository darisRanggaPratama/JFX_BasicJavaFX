module com.tama.customer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.sql;
    requires jasperreports;
    requires java.desktop;
    requires org.apache.poi.ooxml;
    requires org.apache.xmlbeans;
    requires org.apache.logging.log4j;

    // Required modules for JasperReports and PDF generation
    requires java.compiler;
    requires java.xml;
    requires jdk.xml.dom;

    opens com.tama.customer to javafx.fxml;
    exports com.tama.customer;
    opens com.tama.customer.controller to javafx.fxml;
    exports com.tama.customer.controller;
    opens com.tama.customer.model to javafx.base, jasperreports;
    exports com.tama.customer.model;
}