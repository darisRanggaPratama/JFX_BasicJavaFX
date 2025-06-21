package com.tama.basicjfxmultistage.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class FirstController {

    @FXML
    public TextField text;
    @FXML
    public Button Second;
    @FXML
    private Label welcomeText;

    Stage stage, stage2;

    public void initialize() {
        stage = new Stage();
        stage2 = new Stage();

    }

    public void btnSecond(ActionEvent actionEvent) throws IOException {
        welcomeText.setText("Go to second stage");
        System.out.println("Go to second stage");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/basicjfxmultistage/second.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);

        stage2.setScene(scene);
        stage2.setTitle("Second Stage");
        stage2.show();

        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(stage2);
        stage.setTitle("Second Stage");
        stage.show();
    }
}