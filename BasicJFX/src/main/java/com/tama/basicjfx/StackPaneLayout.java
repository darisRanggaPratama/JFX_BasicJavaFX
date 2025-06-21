package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneLayout extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StackPane Layout Example");

        // Create a StackPane
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(20));
        stackPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #3498db, #9b59b6);");

        // Create a large background rectangle
        Rectangle backgroundRect = new Rectangle(600, 400);
        backgroundRect.setFill(Color.rgb(52, 152, 219, 0.5)); // Semi-transparent blue
        backgroundRect.setArcWidth(30);
        backgroundRect.setArcHeight(30);
        backgroundRect.setStroke(Color.WHITE);
        backgroundRect.setStrokeWidth(2);

        // Create a circle in the middle
        Circle circle = new Circle(150);
        circle.setFill(Color.rgb(155, 89, 182, 0.7)); // Semi-transparent purple
        circle.setStroke(Color.WHITE);
        circle.setStrokeWidth(3);

        // Create a smaller rectangle on top
        Rectangle foregroundRect = new Rectangle(300, 200);
        foregroundRect.setFill(Color.rgb(46, 204, 113, 0.6)); // Semi-transparent green
        foregroundRect.setArcWidth(20);
        foregroundRect.setArcHeight(20);
        foregroundRect.setStroke(Color.WHITE);
        foregroundRect.setStrokeWidth(2);

        // Create a title text
        Text titleText = new Text("StackPane Demo");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        titleText.setFill(Color.WHITE);

        // Add a reflection effect to the title
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);
        titleText.setEffect(reflection);

        // Create a description label
        Label descriptionLabel = new Label("Components are stacked on top of each other");
        descriptionLabel.setFont(Font.font("Arial", 18));
        descriptionLabel.setTextFill(Color.WHITE);
        descriptionLabel.setTranslateY(50);

        // Create an interactive button
        Button interactButton = new Button("Click Me!");
        interactButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px;");
        interactButton.setPrefSize(120, 40);
        interactButton.setTranslateY(100);

        // Add a shadow effect to the button
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.5));
        shadow.setOffsetX(3);
        shadow.setOffsetY(3);
        interactButton.setEffect(shadow);

        // Add button click event
        interactButton.setOnAction(event -> {
            // Toggle visibility of some elements when clicked
            if (circle.isVisible()) {
                circle.setVisible(false);
                interactButton.setText("Show Circle");
            } else {
                circle.setVisible(true);
                interactButton.setText("Hide Circle");
            }
        });

        // Add all components to the StackPane
        // The order matters - first added appears at the bottom
        stackPane.getChildren().addAll(
                backgroundRect,
                circle,
                foregroundRect,
                titleText,
                descriptionLabel,
                interactButton
        );

        // Set alignment for all children
        StackPane.setAlignment(titleText, Pos.TOP_CENTER);
        StackPane.setMargin(titleText, new Insets(20, 0, 0, 0));

        // Create Scene and set it to the Stage
        Scene scene = new Scene(stackPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
