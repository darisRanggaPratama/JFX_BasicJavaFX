package com.tama.datasharing.controller;

import com.tama.datasharing.entity.Citizen;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class SecondController implements Initializable {
    @FXML
    private TableView<Citizen> tableCitizen;
    @FXML
    private TableColumn<Citizen, String> colId;
    @FXML
    private TableColumn<Citizen, String> colName;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        tableCitizen.setItems(mainController.getCitizens());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFirstName() + " " + data.getValue().getLastName()));
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
    }
}
