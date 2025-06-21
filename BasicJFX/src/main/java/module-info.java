module com.tama.basicjfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    exports com.tama.basicjfx.ComboList;
    opens com.tama.basicjfx.ComboList to javafx.fxml;

    exports com.tama.basicjfx;
    opens com.tama.basicjfx to javafx.fxml;

}