package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class StackPaneLayout2 extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("StackPane Layout Example");

        // Create a StackPane
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #3498db, #9b59b6);");

        // Create Background Rectangle
        Rectangle backgroundRect = new Rectangle(600, 400);
        backgroundRect.setFill(Color.rgb(52, 152, 219, 0.5)); // Semi-transparent blue
        backgroundRect.setOpacity(0.8);
        // Set rounded corners
       backgroundRect.setArcWidth(30);
        backgroundRect.setArcHeight(30);
        backgroundRect.setStroke(Color.WHITE);
        backgroundRect.setStrokeWidth(2);
        // Set glow effect
        backgroundRect.setEffect(new Glow(2)); // Uncomment if you want to add a glow effect

        // Create decorative circle
        Circle circle = new Circle(200);
        circle.setFill(Color.rgb(155, 89, 182, 0.7)); // Semi-transparent purple
        circle.setStroke(Color.WHITE);
        circle.setOpacity(1);

        // Create progress Indicator circle
        Circle progressCircle = new Circle(100);
        progressCircle.setFill(Color.TRANSPARENT);
        progressCircle.setStroke(Color.WHITE);
        progressCircle.setStrokeWidth(5);
        progressCircle.setOpacity(1);

        // Add change listener to the progress circle
        progressCircle.setOnMouseClicked(event -> {
            // Change the color of the circle on click
            if (progressCircle.getFill() == Color.TRANSPARENT) {
                progressCircle.setFill(Color.rgb(46, 204, 113, 0.6)); // Semi-transparent green
            } else {
                progressCircle.setFill(Color.TRANSPARENT);
            }
        });

        // Create progress indicator effect
        ProgressIndicator proIndicator = new ProgressIndicator(0.5);
        proIndicator.setPrefSize(100, 100);

        ProgressIndicator smallIndicator = new ProgressIndicator(-1);
        smallIndicator.setPrefSize(50, 50);
        StackPane.setAlignment(smallIndicator, Pos.TOP_RIGHT);
        StackPane.setMargin(smallIndicator, new Insets(20));

        // Create text with effect
        Text text = new Text("Interactive Loading...");
        text.setFont(Font.font("JetBrains Mono", 20));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(10, Color.BLACK)); // Add glow effect to the text

        // Create Slider
        Slider slider = new Slider(0, 100, 50);
        slider.setPrefWidth(200);
        StackPane.setAlignment(slider, Pos.BOTTOM_CENTER);
        StackPane.setMargin(slider, new Insets(0, 0, 40, 0));

        // Add change listener to the slider
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            circle.setRadius(newValue.doubleValue() * 0.5);
            proIndicator.setProgress(newValue.doubleValue() / 100);
        });



        // Add all components to the StackPane
        stackPane.getChildren().add(backgroundRect);
        stackPane.getChildren().add(circle);
        stackPane.getChildren().add(progressCircle);
        stackPane.getChildren().add(proIndicator);
        stackPane.getChildren().add(smallIndicator);
        stackPane.getChildren().add(text);
        stackPane.getChildren().add(slider);



        // Create Scene and set it to the Stage
        Scene scene = new Scene(stackPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
