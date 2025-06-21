module com.tama.basicjfxmultistage {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;


    exports com.tama.basicjfxmultistage.Controller;
    opens com.tama.basicjfxmultistage.Controller to javafx.fxml;
    exports com.tama.basicjfxmultistage.Modality;
    opens com.tama.basicjfxmultistage.Modality to javafx.fxml;
}