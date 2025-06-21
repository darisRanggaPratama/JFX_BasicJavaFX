package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Contoh 2: Berbagai Jenis Layout Container
 * 
 * Konsep yang dipelajari:
 * - VBox (Vertical Box)
 * - HBox (Horizontal Box) 
 * - BorderPane
 * - GridPane
 * - TabPane untuk menampilkan berbagai layout
 */
public class LayoutExampleApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();
        
        // Tab 1: VBox Example
        Tab vboxTab = new Tab("VBox Layout");
        vboxTab.setContent(createVBoxExample());
        vboxTab.setClosable(false);
        
        // Tab 2: HBox Example
        Tab hboxTab = new Tab("HBox Layout");
        hboxTab.setContent(createHBoxExample());
        hboxTab.setClosable(false);
        
        // Tab 3: BorderPane Example
        Tab borderTab = new Tab("BorderPane Layout");
        borderTab.setContent(createBorderPaneExample());
        borderTab.setClosable(false);
        
        // Tab 4: GridPane Example
        Tab gridTab = new Tab("GridPane Layout");
        gridTab.setContent(createGridPaneExample());
        gridTab.setClosable(false);
        
        tabPane.getTabs().addAll(vboxTab, hboxTab, borderTab, gridTab);
        
        Scene scene = new Scene(tabPane, 600, 400);
        primaryStage.setTitle("Belajar JavaFX - Layout Containers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    private VBox createVBoxExample() {
        VBox vbox = new VBox(15); // spacing 15 pixels
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);
        
        Label title = new Label("VBox - Vertical Layout");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Button btn1 = new Button("Button 1");
        Button btn2 = new Button("Button 2");
        Button btn3 = new Button("Button 3");
        
        vbox.getChildren().addAll(title, btn1, btn2, btn3);
        return vbox;
    }
    
    private HBox createHBoxExample() {
        HBox container = new HBox(15);
        container.setPadding(new Insets(20));
        container.setAlignment(Pos.CENTER);
        
        Label title = new Label("HBox - Horizontal Layout");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        HBox hbox = new HBox(10); // spacing 10 pixels
        hbox.setAlignment(Pos.CENTER);
        
        Button btn1 = new Button("Button 1");
        Button btn2 = new Button("Button 2");
        Button btn3 = new Button("Button 3");
        
        hbox.getChildren().addAll(btn1, btn2, btn3);
        container.getChildren().addAll(title, hbox);
        
        return container;
    }
    
    private BorderPane createBorderPaneExample() {
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20));
        
        // Top
        Label top = new Label("TOP");
        top.setStyle("-fx-background-color: lightblue; -fx-padding: 10px;");
        borderPane.setTop(top);
        
        // Left
        Label left = new Label("LEFT");
        left.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px;");
        borderPane.setLeft(left);
        
        // Center
        Label center = new Label("CENTER\n(Main Content Area)");
        center.setStyle("-fx-background-color: lightyellow; -fx-padding: 20px; -fx-alignment: center;");
        borderPane.setCenter(center);
        
        // Right
        Label right = new Label("RIGHT");
        right.setStyle("-fx-background-color: lightcoral; -fx-padding: 10px;");
        borderPane.setRight(right);
        
        // Bottom
        Label bottom = new Label("BOTTOM");
        bottom.setStyle("-fx-background-color: lightgray; -fx-padding: 10px;");
        borderPane.setBottom(bottom);
        
        return borderPane;
    }
    
    private GridPane createGridPaneExample() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10); // horizontal gap
        grid.setVgap(10); // vertical gap
        grid.setAlignment(Pos.CENTER);
        
        // Add title
        Label title = new Label("GridPane - Grid Layout");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        grid.add(title, 0, 0, 3, 1); // column 0, row 0, colspan 3, rowspan 1
        
        // Add buttons in grid
        for (int row = 1; row <= 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button btn = new Button("Btn " + row + "," + col);
                btn.setPrefWidth(80);
                grid.add(btn, col, row);
            }
        }
        
        return grid;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
