module com.tama.otodidakjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires java.desktop;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    // Jasper Reports dependencies
    requires jasperreports;
    requires java.sql;
    requires java.xml;

    opens com.tama.otodidakjfx to javafx.fxml, jasperreports;
    exports com.tama.otodidakjfx;
}