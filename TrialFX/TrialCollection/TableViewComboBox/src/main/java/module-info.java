module com.tama.combotable {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    exports com.tama.combotable;
    exports com.tama.combotable.controller;

    opens com.tama.combotable to javafx.fxml;
    opens com.tama.combotable.controller to javafx.fxml;
}