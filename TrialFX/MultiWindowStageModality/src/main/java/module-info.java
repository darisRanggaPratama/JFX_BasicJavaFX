module com.tama.stagemodal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jdk.compiler;

    opens com.tama.stagemodal to javafx.fxml;
    exports com.tama.stagemodal;
    exports com.tama.stagemodal.controller;
    opens com.tama.stagemodal.controller to javafx.fxml;
}