package com.tama.jfxcrud;

import com.tama.jfxcrud.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class CrudApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CrudApp.class.getResource("/com/tama/jfxcrud/main-view.fxml"));
        Parent root = fxmlLoader.load();
        
        MainViewController controller = fxmlLoader.getController();
        controller.setPrimaryStage(stage);
        
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/tama/jfxcrud/styles.css")).toExternalForm());
        
        stage.setTitle("Customer Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}