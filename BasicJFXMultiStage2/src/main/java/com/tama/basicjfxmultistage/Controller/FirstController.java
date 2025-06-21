package com.tama.basicjfxmultistage.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstController {

    @FXML
    public TextField text;
    @FXML
    public Button Second;
    @FXML
    private Label welcomeText;

    Stage stage;

    public void initialize() {
        stage = new Stage();
    }

    public void btnSecond(ActionEvent actionEvent) throws IOException {
        welcomeText.setText("Go to second stage");
        System.out.println("Go to second stage");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/basicjfxmultistage/second.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("Second Stage");

        SecondController secondCtrl = loader.getController();
        secondCtrl.insertToLabel(text.getText());

        stage.showAndWait();

        System.out.println(secondCtrl.getFromLabel());
    }
}