package com.tama.datasharing.controller;

import com.tama.datasharing.entity.Citizen;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstController implements Initializable {
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TableView<Citizen> tableCitizen;
    @FXML
    private TableColumn<Citizen, String> colId;
    @FXML
    private TableColumn<Citizen, String> colFirstName;
    @FXML
    private TableColumn<Citizen, String> colLastName;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        tableCitizen.setItems(mainController.getCitizens());
    }

    @FXML
    private void saveAction(ActionEvent actionEvent) {
        if (txtId.getText().trim().isEmpty() || txtFirstName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty()) {
            // Show alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Data");
            alert.setContentText("Please fill all fields");
            alert.showAndWait();
            return;
        }

        Citizen citizen = new Citizen();
        citizen.setId(txtId.getText().trim());
        citizen.setFirstName(txtFirstName.getText().trim());
        citizen.setLastName(txtLastName.getText().trim());
        mainController.getCitizens().add(citizen);

        // Clear fields
        txtId.clear();
        txtFirstName.clear();
        txtLastName.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colFirstName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName()));
        colLastName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLastName()));
    }
}
