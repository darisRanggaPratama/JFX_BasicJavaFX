package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class HBoxLayout extends Application {
    @Override
    public void start(Stage PrimaryStage) throws Exception {
        PrimaryStage.setTitle("HBox Layout Example");

        // Create HBox with 15 pixels of spacing
        HBox hbox = new HBox(15);
        hbox.setPadding(new Insets(20));
        hbox.setAlignment(Pos.CENTER);
        hbox.setStyle("-fx-background-color: #f0f0f0;");

        // Add components
        Label labelMenu = new Label("Select the Menu");
        labelMenu.setStyle("-fx-font-size: 20px;");

        Button btnBurger = new Button("Burger");
        Button btnPizza = new Button("Pizza");
        Button btnSalad = new Button("Salad");
        Button btnDrink = new Button("Drink");

        // Add all buttons click event
        btnBurger.setOnAction(event -> System.out.println("Burger is selected"));
        btnPizza.setOnAction(event -> System.out.println("Pizza is selected"));
        btnSalad.setOnAction(event -> System.out.println("Salad is selected"));
        btnDrink.setOnAction(event -> System.out.println("Drink is selected"));

        // All componentss to HBox
        hbox.getChildren().addAll(labelMenu, btnBurger, btnPizza, btnSalad, btnDrink);

        // Create Scene and set itu to the Stage
        Scene scene = new Scene(hbox, 800, 600);
        PrimaryStage.setScene(scene);
        PrimaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}
