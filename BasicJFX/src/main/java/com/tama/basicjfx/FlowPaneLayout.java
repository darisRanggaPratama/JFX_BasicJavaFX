package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FlowPaneLayout extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Photo Gallery - FlowPane Layout Example");

        // Create a ScrollPane to handle overflow
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: #f0f0f0;");

        // Create a FlowPane
        FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL);
        flowPane.setPadding(new Insets(10));
        flowPane.setHgap(10); // Horizontal gap between items
        flowPane.setVgap(10); // Vertical gap between items
        flowPane.setStyle("-fx-background-color: #ffffff;");
        flowPane.setAlignment(Pos.CENTER);

        // Create photo thumbnails
        String[] photoTitles = {
                "Sunset", "Mountains", "Beach", "Cityscape",
                "Forest", "Desert", "River", "Lake",
                "Flowers", "Wildlife", "Night Sky", "Rainforest",
                "Snowy Peaks", "Canyon", "Waterfall", "Volcano"
        };

        for (String title : photoTitles) {
            StackPane thumbnail = createPhotoThumbnail(title);
            flowPane.getChildren().add(thumbnail);
        }

        scrollPane.setContent(flowPane);
        Scene scene = new Scene(scrollPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createPhotoThumbnail(String title) {
        StackPane thumbnail = new StackPane();

        // Create thumbnail rectangle
        Rectangle rect = new Rectangle(150, 100);
        rect.setFill(Color.valueOf("#3498db")); // Blue color
        rect.setStroke(Color.WHITE);
        rect.setStrokeWidth(2);
        rect.setArcWidth(10);
        rect.setArcHeight(10);

        // Create title text
        Text text = new Text(title);
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Add hover effect
        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.DODGERBLUE);

        thumbnail.setOnMouseExited(event -> {
            rect.setEffect(null);
            rect.setFill(Color.valueOf("#3498db"));
            thumbnail.setScaleX(1);
            thumbnail.setScaleY(1);
        });

        // Add click handler
        thumbnail.setOnMouseClicked(event -> {
            rect.setEffect(shadow);
            rect.setFill(Color.valueOf("#2980b9")); // Darker blue on click
            thumbnail.setScaleX(1.1);
            thumbnail.setScaleY(1.1);
            System.out.println("Clicked on: " + title);
        });

        // Add tooltip
        Tooltip tooltip = new Tooltip("Click to view " + title);
        Tooltip.install(thumbnail, tooltip);

        thumbnail.getChildren().addAll(rect, text);

        return thumbnail;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
