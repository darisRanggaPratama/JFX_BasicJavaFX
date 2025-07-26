module com.tama.customers {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.tama.customers to javafx.fxml;
    exports com.tama.customers;
}