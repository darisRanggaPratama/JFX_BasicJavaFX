package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class AnchorPaneLayout extends Application {
    @Override
    public void start(javafx.stage.Stage primaryStage) {
        primaryStage.setTitle("AnchorPane Layout Example");

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();

        // Create an AnchorPane
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #f0f0f0;");
        anchorPane.setPadding(new Insets(10));

        // Create Header
        ToolBar header = new ToolBar();
        header.setStyle("-fx-background-color: #333;");
        header.setPrefHeight(50);

        // Add components here
        Label title = new Label("Dashboard Overview AnchorPane Layout Example");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #fff;");
        Circle userAvatar = new Circle(20, Color.BLUE);
        MenuButton userMenu = new MenuButton("John Wick", userAvatar);
        userMenu.getItems().addAll(
                new MenuItem("Logout"),
                new MenuItem("Settings"),
                new SeparatorMenuItem(),
                new MenuItem("Logout")
        );

        userMenu.setStyle("-fx-font-size: 16px; -fx-text-fill: #fff;");

        header.getItems().addAll(title, new Separator(), userMenu);

        // Create Sidebar
        VBox sidebar = new VBox(10);
        sidebar.setStyle("-fx-background-color: #555; -fx-padding: 10;");
        sidebar.setPrefWidth(200);

        // Sidebar buttons
        String[] menuItems = {"Dashboard", "Analytics", "Reports", "Settings"};
        for (String item : menuItems) {
            Button menuButton = new Button(item);
            menuButton.setMaxWidth(Double.MAX_VALUE);
            menuButton.setStyle("-fx-background-color: #777; -fx-text-fill: #fff; -fx-font-size: 16px; -fx-padding: 10;");
            sidebar.getChildren().add(menuButton);
        }

        // Create chart
        NumberAxis xAxis = new NumberAxis("Hours", 0, 24, 3);
        NumberAxis yAxis = new NumberAxis("Tasks", 0, 100, 10);
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Tasks Completed Over Time");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Tasks");
        series.getData().add(new XYChart.Data<>(0, 10));
        series.getData().add(new XYChart.Data<>(1, 20));
        series.getData().add(new XYChart.Data<>(2, 30));
        series.getData().add(new XYChart.Data<>(3, 25));
        series.getData().add(new XYChart.Data<>(4, 50));
        series.getData().add(new XYChart.Data<>(5, 60));
        series.getData().add(new XYChart.Data<>(6, 40));
        lineChart.getData().add(series);

        // Create Status Panel
        VBox statusPanel = new VBox(10);
        statusPanel.setStyle("-fx-background-color: #eee; -fx-padding: 10; -fx-background-radius: 5;");
        statusPanel.setEffect(new DropShadow(10, Color.gray(0.5, 0.5)));

        Text statusTitle = new Text("Status");
        statusTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #333;");
        ProgressBar memoryBar = new ProgressBar(0.5);
        memoryBar.setPrefWidth(200);
        Text memoryText = new Text("Memory Usage: 50%");
        ProgressIndicator cpuIndicator = new ProgressIndicator(0.75);
        Text cpuText = new Text("CPU Usage: 75%");

        statusPanel.getChildren().addAll(statusTitle, new Separator(), memoryBar, memoryText, cpuIndicator, cpuText);

        // Position elements using AnchorPane constraints
        AnchorPane.setTopAnchor(header, 0.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);

        AnchorPane.setTopAnchor(sidebar, 50.0);
        AnchorPane.setLeftAnchor(sidebar, 0.0);
        AnchorPane.setBottomAnchor(sidebar, 0.0);

        AnchorPane.setTopAnchor(lineChart, 70.0);
        AnchorPane.setLeftAnchor(lineChart, 220.0);
        AnchorPane.setRightAnchor(lineChart, 220.0);
        AnchorPane.setBottomAnchor(lineChart, 20.0);

        AnchorPane.setTopAnchor(statusPanel, 70.0);
        AnchorPane.setRightAnchor(statusPanel, 10.0);
        AnchorPane.setBottomAnchor(statusPanel, 20.0);

        // Add all components to the AnchorPane
        anchorPane.getChildren().addAll(header, sidebar, lineChart, statusPanel);

        Scene scene = new Scene(anchorPane, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
