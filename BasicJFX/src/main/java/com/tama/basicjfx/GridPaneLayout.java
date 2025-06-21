package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GridPaneLayout extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GridPane Layout Example");

        // Create a GridPane
        GridPane gPane = new GridPane();
        gPane.setPadding(new Insets(10));
        gPane.setHgap(10); // Horizontal gap between columns
        gPane.setVgap(10); // Vertical gap between rows

        // Create components
        Label lblName = new Label("Name");
        TextField txtName = new TextField();
        txtName.setPromptText("Enter your name");

        Label lblEmail = new Label("Email");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter your email");

        Button btnSubmit = new Button("Submit");
        btnSubmit.setOnAction(event -> {
            System.out.println("\nName: " + txtName.getText());
            System.out.println("Email: " + txtEmail.getText());
        });

        // Add components to the GridPane
        gPane.add(lblName, 0, 0); // Column 0, Row 0
        gPane.add(txtName, 1, 0); // Column 1, Row 0
        gPane.add(lblEmail, 0, 1); // Column 0, Row 1
        gPane.add(txtEmail, 1, 1); // Column 1, Row 1
        gPane.add(btnSubmit, 1, 2); // Column 1, Row 2

        // Create a Scene and set it to the Stage
        Scene scene = new Scene(gPane, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}
