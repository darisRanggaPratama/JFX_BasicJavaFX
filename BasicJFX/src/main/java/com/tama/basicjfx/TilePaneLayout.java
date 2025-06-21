package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TilePaneLayout extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Color Palette - TilePane Example");

        // Create main VBox container
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2c3e50;");

        // Create controls
        Label titleLabel = new Label("Interactive Color Palette");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

        // Create TilePane
        TilePane tilePane = new TilePane();
        tilePane.setPrefColumns(4); // Set preferred columns
        tilePane.setHgap(10);
        tilePane.setVgap(10);
        tilePane.setPadding(new Insets(10));
        tilePane.setStyle("-fx-background-color: #34495e; -fx-background-radius: 10;");
        tilePane.setAlignment(Pos.CENTER);

        // Create ScrollPane to handle overflow
        ScrollPane scrollPane = new ScrollPane(tilePane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Controls for tile customization
        ColorPicker colorPicker = new ColorPicker(Color.DODGERBLUE);
        colorPicker.setStyle("-fx-color-label-visible: false;");

        Slider sizeSlider = new Slider(50, 150, 100);
        sizeSlider.setShowTickLabels(true);
        sizeSlider.setShowTickMarks(true);

        Label sizeLabel = new Label("Tile Size: 100px");
        sizeLabel.setStyle("-fx-text-fill: white;");

        // Update size label when slider changes
        sizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            sizeLabel.setText(String.format("Tile Size: %.0fpx", newVal.doubleValue()));
            updateTileSizes(tilePane, newVal.doubleValue());
        });

        // Create color tiles
        String[] colorNames = {
                "Ruby", "Sapphire", "Emerald", "Topaz",
                "Amethyst", "Pearl", "Jade", "Amber",
                "Garnet", "Opal", "Aquamarine", "Peridot",
                "Diamond", "Tanzanite", "Morganite", "Citrine"
        };

        for (String colorName : colorNames) {
            StackPane tile = createColorTile(colorName, 100);
            tilePane.getChildren().add(tile);

            // Update tile color when color picker changes
            colorPicker.setOnAction(e -> {
                if (tile.isHover()) {
                    ((Rectangle) tile.getChildren().get(0)).setFill(colorPicker.getValue());
                }
            });
        }

        // Add all components to root
        root.getChildren().addAll(titleLabel, colorPicker, sizeLabel, sizeSlider, scrollPane
        );

        Scene scene = new Scene(root, 600, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private StackPane createColorTile(String name, double size) {
        StackPane tile = new StackPane();

        // Create colored rectangle
        Rectangle rect = new Rectangle(size, size);
        rect.setFill(Color.color(Math.random(), Math.random(), Math.random()));
        rect.setArcWidth(15);
        rect.setArcHeight(15);

        // Create label
        Label label = new Label(name);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        // Add hover effect
        DropShadow shadow = new DropShadow(10, Color.WHITE);
        tile.setOnMouseEntered(e -> {
            rect.setEffect(shadow);
            tile.setScaleX(1.1);
            tile.setScaleY(1.1);
        });

        tile.setOnMouseExited(e -> {
            rect.setEffect(null);
            tile.setScaleX(1);
            tile.setScaleY(1);
        });

        tile.getChildren().addAll(rect, label);
        return tile;
    }

    private void updateTileSizes(TilePane tilePane, double size) {
        for (Node node : tilePane.getChildren()) {
            if (node instanceof StackPane) {
                StackPane tile = (StackPane) node;
                Rectangle rect = (Rectangle) tile.getChildren().get(0);
                rect.setWidth(size);
                rect.setHeight(size);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}