module com.tama.trial {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.tama.trial to javafx.fxml;
    exports com.tama.trial;
    exports com.tama.trial.controller;
    opens com.tama.trial.controller to javafx.fxml;
}