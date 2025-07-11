package com.tama.stagemodal.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class FirstWindowController {
    @FXML
    private VBox rootVBox;

    @FXML
    private void openAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "/com/tama/stagemodal/second_window.fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Second Window");
        stage.initOwner(rootVBox.getScene().getWindow());
        stage.show();
    }

    @FXML
    private void openThirdWindow(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "/com/tama/stagemodal/third_window.fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Third Window");
        stage.initOwner(rootVBox.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.show();
    }

    @FXML
    private void openFourthWindow(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(
                "/com/tama/stagemodal/fourth_window.fxml")));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Fourth Window");
        stage.initOwner(rootVBox.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
