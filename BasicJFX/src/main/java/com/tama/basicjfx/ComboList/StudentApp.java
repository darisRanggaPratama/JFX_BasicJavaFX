package com.tama.basicjfx.ComboList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StudentApp extends Application {
    public StudentApp() {}

    @Override
    public void start(Stage primaryStage) throws Exception {
        var resource = getClass().getResource("/com/tama/basicjfx/ComboList/Student.fxml");
        if (resource == null) {
            throw new IllegalStateException("Cannot find " + resource.getPath());
        }

        Parent root = FXMLLoader.load(resource);

        Scene scene = new Scene(root);

        // Set the title of the primary stage
        primaryStage.setTitle("Student Application");

        // PrimaryStage Height and Width
        primaryStage.setWidth(400);
        primaryStage.setHeight(600);

        // Center the stage on the screen
        primaryStage.centerOnScreen();

        primaryStage.setScene(scene);

        // Show the primary stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    }

