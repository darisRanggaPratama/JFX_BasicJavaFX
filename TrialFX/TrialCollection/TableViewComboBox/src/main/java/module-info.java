module com.tama.tablecombo {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports com.tama.tablecombo;
    exports com.tama.tablecombo.controller;

    opens com.tama.tablecombo to javafx.fxml;
    opens com.tama.tablecombo.controller to javafx.fxml;
}