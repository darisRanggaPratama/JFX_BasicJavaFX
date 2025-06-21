package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BorderPaneLayout extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BorderPane Layout Example");

        // Create BorderPane
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10));

        // Create components for each region
        Label topLabel = new Label("Top Region");
        topLabel.setStyle("-fx-font-size: 20px; -fx-background-color: #e6e6e6; -fx-padding: 10px;");

        Button leftButton = new Button("Left Region");
        leftButton.setMaxWidth(Double.MAX_VALUE);

        Label centerLabel = new Label("Center Region");
        centerLabel.setStyle("-fx-font-size: 24px; -fx-background-color: #f0f0f0;");

        Button rightButton = new Button("Right Region");
        rightButton.setMaxWidth(Double.MAX_VALUE);

        Label bottomLabel = new Label("Bottom Region");
        bottomLabel.setStyle("-fx-font-size: 20px; -fx-background-color: #e6e6e6; -fx-padding: 10px;");

        // Set components to their respective regions
        borderPane.setTop(topLabel);
        borderPane.setLeft(leftButton);
        borderPane.setCenter(centerLabel);
        borderPane.setRight(rightButton);
        borderPane.setBottom(bottomLabel);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}