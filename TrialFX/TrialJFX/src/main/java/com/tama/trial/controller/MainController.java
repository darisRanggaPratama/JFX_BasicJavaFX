package com.tama.trial.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    private Label lblInput;
    @FXML
    private TextField txtInput;
    @FXML
    private Label lblOutput;

    @FXML
    private void sendText(ActionEvent actionEvent) {
        if (!txtInput.getText().trim().isEmpty()) {
            lblOutput.setText(txtInput.getText().trim());
        } else {
            lblOutput.setText("Empty input");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Empty input");
            alert.showAndWait();
        }
    }
}