package com.tama.basicjfxmultistage.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SecondController {
    public Button First;
    public Label otherText;

    Stage stage;

    public void initialize() {
        stage = new Stage();
    }

    public void btnFirst(ActionEvent actionEvent) {
        otherText.setText("Go to first stage");
        System.out.println("Go to first stage");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/basicjfxmultistage/first.fxml"));

        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("First Stage");
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
