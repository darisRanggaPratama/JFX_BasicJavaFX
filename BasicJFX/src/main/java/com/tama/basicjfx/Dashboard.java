package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.*;

public class Dashboard extends Application {
    // Color constants
    private static final String SIDEBAR_BG = "#2D3250";
    private static final String MAIN_BG = "#F5F5F5";
    private static final String TEXT_PRIMARY = "#333333";
    private static final String ACCENT_COLOR = "#677BC4";

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Create sidebar
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        // Create main content
        VBox mainContent = createMainContent();
        root.setCenter(mainContent);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Modern Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(20));
        sidebar.setPrefWidth(250);
        sidebar.setStyle("-fx-background-color: " + SIDEBAR_BG + ";");

        // Logo/Brand
        Label brand = new Label("Dashboard");
        brand.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-font-weight: bold;");

        // Navigation buttons
        Button[] navButtons = {
            createNavButton("Dashboard", true),
            createNavButton("Analytics", false),
            createNavButton("Reports", false),
            createNavButton("Settings", false)
        };

        sidebar.getChildren().add(brand);
        sidebar.getChildren().addAll(navButtons);
        return sidebar;
    }

    private Button createNavButton(String text, boolean isActive) {
        Button button = new Button(text);
        button.setPrefWidth(210);
        button.setStyle(
            "-fx-background-color: " + (isActive ? ACCENT_COLOR : "transparent") + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14px;" +
            "-fx-alignment: CENTER-LEFT;" +
            "-fx-padding: 10 15;"
        );
        return button;
    }

    private VBox createMainContent() {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: " + MAIN_BG + ";");

        // Header
        HBox header = createHeader();

        // Stats cards
        HBox statsCards = createStatsCards();

        // Chart section
        VBox chartSection = createChartSection();

        // Activity table
        VBox activitySection = createActivitySection();

        mainContent.getChildren().addAll(header, statsCards, chartSection, activitySection);
        return mainContent;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        header.setSpacing(20);

        Label title = new Label("Dashboard Overview");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        header.getChildren().add(title);
        return header;
    }

    private HBox createStatsCards() {
        HBox container = new HBox(20);
        String[] titles = {"Total Users", "Revenue", "Orders", "Growth"};
        String[] values = {"1,485", "$17,482", "305", "+16.24%"};

        for (int i = 0; i < titles.length; i++) {
            VBox card = new VBox(10);
            card.setPadding(new Insets(20));
            card.setPrefWidth(200);
            card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 10;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);"
            );

            Label title = new Label(titles[i]);
            title.setStyle("-fx-text-fill: #666666; -fx-font-size: 14px;");

            Label value = new Label(values[i]);
            value.setStyle("-fx-font-weight: bold; -fx-font-size: 24px;");

            card.getChildren().addAll(title, value);
            container.getChildren().add(card);
        }

        return container;
    }

    private VBox createChartSection() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20, 0, 0, 0));

        Label title = new Label("Performance Overview");
        title.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        // Create chart
        LineChart<String, Number> lineChart = createPerformanceChart();
        lineChart.setPrefHeight(300);

        container.getChildren().addAll(title, lineChart);
        return container;
    }

    private LineChart<String, Number> createPerformanceChart() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

        lineChart.setStyle("-fx-background-color: white;");
        lineChart.setCreateSymbols(true);
        lineChart.setAnimated(true);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Jan", 23));
        series.getData().add(new XYChart.Data<>("Feb", 45));
        series.getData().add(new XYChart.Data<>("Mar", 37));
        series.getData().add(new XYChart.Data<>("Apr", 58));
        series.getData().add(new XYChart.Data<>("May", 42));

        lineChart.getData().add(series);
        return lineChart;
    }

    private VBox createActivitySection() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(20, 0, 0, 0));

        Label title = new Label("Recent Activities");
        title.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        // Create table
        TableView<Activity> table = new TableView<>();
        table.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 0);"
        );

        // Configure table columns
        TableColumn<Activity, String> typeCol = new TableColumn<>("Type");
        TableColumn<Activity, String> descCol = new TableColumn<>("Description");
        TableColumn<Activity, String> dateCol = new TableColumn<>("Date");
        TableColumn<Activity, String> statusCol = new TableColumn<>("Status");

        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));
        descCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
        dateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        statusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        table.getColumns().addAll(typeCol, descCol, dateCol, statusCol);

        // Add sample data
        table.getItems().addAll(
            new Activity("Purchase", "New order #123", "2025-06-08", "Completed"),
            new Activity("Sale", "Product sale #456", "2025-06-08", "Pending"),
            new Activity("Update", "System update", "2025-06-07", "In Progress")
        );

        container.getChildren().addAll(title, table);
        return container;
    }

    // Data class for activity table
    private static class Activity {
        private String type;
        private String description;
        private String date;
        private String status;

        public Activity(String type, String description, String date, String status) {
            this.type = type;
            this.description = description;
            this.date = date;
            this.status = status;
        }

        public String getType() { return type; }
        public String getDescription() { return description; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
