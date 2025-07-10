package com.tama.trial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApps extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Approach 1 (Alternative): Using static load method
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_layout.fxml")));

        stage.setTitle("Test Input");
        stage.setScene(new Scene(root, 300, 275));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
