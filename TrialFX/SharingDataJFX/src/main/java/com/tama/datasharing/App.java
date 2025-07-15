package com.tama.datasharing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(App.class.getResource("/com/tama/datasharing/main_view.fxml")));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Main View JFX");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error load FXML: " + e.getMessage());
            throw e;
        }
    }

    public static void main(String[] args) {
        launch();
    }


}