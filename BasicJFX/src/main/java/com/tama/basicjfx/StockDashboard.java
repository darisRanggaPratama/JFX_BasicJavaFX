package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class StockDashboard extends Application {

    private VBox mainLayout;

    // Modern color palette
    private final String BG_COLOR = "#0A0E17";
    private final String CARD_BG = "#141B2D";
    private final String ACCENT_COLOR = "#3B82F6";
    private final String TEXT_COLOR = "#FFFFFF";
    private final String SUCCESS_COLOR = "#22C55E";
    private final String DANGER_COLOR = "#EF4444";

    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNIFIED);
        stage.setTitle("Modern Stock Analytics");

        // Main container with dark theme
        mainLayout = new VBox(20);
        mainLayout.setStyle(
            "-fx-background-color: " + BG_COLOR + ";" +
            "-fx-font-family: 'SF Pro Display', 'Segoe UI', Arial;"
        );
        mainLayout.setPadding(new Insets(20));

        // Create dashboard sections
        VBox topSection = createTopSection();
        HBox mainContent = createMainContent();

        // Add all sections to main layout
        mainLayout.getChildren().addAll(topSection, mainContent);

        // Create scene
        Scene scene = new Scene(mainLayout, 1280, 800);
        stage.setScene(scene);

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), mainLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        stage.show();
    }

    private VBox createTopSection() {
        VBox topSection = new VBox(20);

        // Header with title and controls
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Stock Market Analytics");
        title.setStyle(
            "-fx-font-size: 28px; " +
            "-fx-text-fill: " + TEXT_COLOR + ";" +
            "-fx-font-weight: bold;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Market status indicators
        HBox statusBox = new HBox(15);
        statusBox.setAlignment(Pos.CENTER_RIGHT);

        Label marketStatus = new Label("Market Open");
        marketStatus.setStyle(
            "-fx-background-color: " + SUCCESS_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-padding: 5 15;" +
            "-fx-background-radius: 20;"
        );

        ComboBox<String> marketSelector = new ComboBox<>();
        marketSelector.getItems().addAll("NYSE", "NASDAQ", "LSE", "TSE");
        marketSelector.setValue("NYSE");
        marketSelector.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-text-fill: white;" +
            "-fx-mark-color: white;"
        );

        statusBox.getChildren().addAll(marketStatus, marketSelector);
        header.getChildren().addAll(title, spacer, statusBox);

        // Quick stats cards
        HBox statsCards = createStatsCards();

        topSection.getChildren().addAll(header, statsCards);
        return topSection;
    }

    private HBox createStatsCards() {
        HBox statsBox = new HBox(20);

        // Create four stat cards
        String[][] statsData = {
            {"S&P 500", "+1.2%", "$4,587.64", SUCCESS_COLOR},
            {"NASDAQ", "-0.8%", "$14,340.99", DANGER_COLOR},
            {"DOW JONES", "+0.5%", "$35,950.89", SUCCESS_COLOR},
            {"VIX", "+2.1%", "18.56", DANGER_COLOR}
        };

        for (String[] stat : statsData) {
            VBox card = new VBox(10);
            card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 15;" +
                "-fx-min-width: 200;"
            );

            Label name = new Label(stat[0]);
            name.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 14px;");

            Label change = new Label(stat[1]);
            change.setStyle("-fx-text-fill: " + stat[3] + "; -fx-font-size: 20px; -fx-font-weight: bold;");

            Label value = new Label(stat[2]);
            value.setStyle("-fx-text-fill: " + TEXT_COLOR + "; -fx-font-size: 16px;");

            card.getChildren().addAll(name, change, value);
            statsBox.getChildren().add(card);
        }

        return statsBox;
    }

    private HBox createMainContent() {
        HBox content = new HBox(20);

        // Charts section (left side)
        VBox chartsSection = createChartsSection();
        HBox.setHgrow(chartsSection, Priority.ALWAYS);

        // Trading panel (right side)
        VBox tradingPanel = createTradingPanel();
        tradingPanel.setMinWidth(300);
        tradingPanel.setMaxWidth(300);

        content.getChildren().addAll(chartsSection, tradingPanel);
        return content;
    }

    private VBox createChartsSection() {
        VBox charts = new VBox(20);

        // Price chart
        VBox priceChart = createPriceChart();
        VBox.setVgrow(priceChart, Priority.ALWAYS);

        // Bottom section with pie and bar charts
        HBox bottomCharts = new HBox(20);
        bottomCharts.setMinHeight(300);

        VBox sectorAllocation = createSectorAllocationChart();
        VBox volumeChart = createVolumeChart();

        HBox.setHgrow(sectorAllocation, Priority.ALWAYS);
        HBox.setHgrow(volumeChart, Priority.ALWAYS);

        bottomCharts.getChildren().addAll(sectorAllocation, volumeChart);

        charts.getChildren().addAll(priceChart, bottomCharts);
        return charts;
    }

    private VBox createPriceChart() {
        VBox chartContainer = new VBox(15);
        chartContainer.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        // Chart header
        HBox chartHeader = new HBox(10);
        chartHeader.setAlignment(Pos.CENTER_LEFT);

        Label chartTitle = new Label("AAPL Stock Price");
        chartTitle.setStyle("-fx-font-size: 18px; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-weight: bold;");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        // Time period toggles
        ToggleGroup timeGroup = new ToggleGroup();
        HBox timeToggles = new HBox(5);
        String[] periods = {"1D", "1W", "1M", "3M", "6M", "1Y", "ALL"};

        for (String period : periods) {
            ToggleButton tb = new ToggleButton(period);
            tb.setToggleGroup(timeGroup);
            tb.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXT_COLOR + ";" +
                "-fx-padding: 5 10;" +
                "-fx-cursor: hand;"
            );

            tb.setOnMouseEntered(e -> tb.setStyle(
                "-fx-background-color: " + ACCENT_COLOR + ";" +
                "-fx-text-fill: white;" +
                "-fx-padding: 5 10;"
            ));

            tb.setOnMouseExited(e -> {
                if (!tb.isSelected()) {
                    tb.setStyle(
                        "-fx-background-color: transparent;" +
                        "-fx-text-fill: " + TEXT_COLOR + ";" +
                        "-fx-padding: 5 10;"
                    );
                }
            });

            timeToggles.getChildren().add(tb);
        }

        // Select default period (1M)
        ((ToggleButton)timeToggles.getChildren().get(2)).setSelected(true);

        chartHeader.getChildren().addAll(chartTitle, headerSpacer, timeToggles);

        // Create the chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.web(TEXT_COLOR));
        yAxis.setTickLabelFill(Color.web(TEXT_COLOR));

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);
        lineChart.setStyle("-fx-background-color: transparent;");

        // Add data
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("AAPL");

        // Sample data points
        double[] prices = {150.25, 152.35, 151.80, 153.65, 155.90, 154.75, 156.25,
                          158.40, 157.85, 159.20, 161.45, 160.90, 162.30, 164.55};

        for (int i = 0; i < prices.length; i++) {
            series.getData().add(new XYChart.Data<>(i, prices[i]));
        }

        lineChart.getData().add(series);

        chartContainer.getChildren().addAll(chartHeader, lineChart);
        return chartContainer;
    }

    private VBox createSectorAllocationChart() {
        VBox chartContainer = new VBox(10);
        chartContainer.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        Label title = new Label("Sector Allocation");
        title.setStyle("-fx-font-size: 16px; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-weight: bold;");

        PieChart pieChart = new PieChart();
        pieChart.setLabelsVisible(true);
        pieChart.setStyle("-fx-background-color: transparent;");

        // Add data
        pieChart.getData().addAll(
            new PieChart.Data("Technology", 35),
            new PieChart.Data("Healthcare", 20),
            new PieChart.Data("Finance", 18),
            new PieChart.Data("Consumer", 15),
            new PieChart.Data("Energy", 12)
        );

        chartContainer.getChildren().addAll(title, pieChart);
        return chartContainer;
    }

    private VBox createVolumeChart() {
        VBox chartContainer = new VBox(10);
        chartContainer.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        Label title = new Label("Trading Volume");
        title.setStyle("-fx-font-size: 16px; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-weight: bold;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.web(TEXT_COLOR));
        yAxis.setTickLabelFill(Color.web(TEXT_COLOR));

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
            new XYChart.Data<>("Mon", 28.5),
            new XYChart.Data<>("Tue", 32.2),
            new XYChart.Data<>("Wed", 25.8),
            new XYChart.Data<>("Thu", 35.4),
            new XYChart.Data<>("Fri", 30.1)
        );

        barChart.getData().add(series);

        chartContainer.getChildren().addAll(title, barChart);
        return chartContainer;
    }

    private VBox createTradingPanel() {
        VBox panel = new VBox(20);
        panel.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        // Panel title
        Label title = new Label("Trading Panel");
        title.setStyle("-fx-font-size: 18px; -fx-text-fill: " + TEXT_COLOR + "; -fx-font-weight: bold;");

        // Stock selector
        Label stockLabel = new Label("Select Stock");
        stockLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + ";");

        ComboBox<String> stockCombo = new ComboBox<>();
        stockCombo.getItems().addAll("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "META", "NVDA");
        stockCombo.setValue("AAPL");
        stockCombo.setMaxWidth(Double.MAX_VALUE);
        stockCombo.setStyle(
            "-fx-background-color: " + BG_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-mark-color: white;"
        );

        // Investment amount
        Label amountLabel = new Label("Investment Amount: $10,000");
        amountLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + ";");

        Slider amountSlider = new Slider(1000, 100000, 10000);
        amountSlider.setShowTickLabels(true);
        amountSlider.setShowTickMarks(true);
        amountSlider.setMajorTickUnit(20000);
        amountSlider.setBlockIncrement(1000);

        amountSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            amountLabel.setText(String.format("Investment Amount: $%,.0f", newVal.doubleValue()))
        );

        // Risk level
        Label riskLabel = new Label("Risk Level");
        riskLabel.setStyle("-fx-text-fill: " + TEXT_COLOR + ";");

        ProgressBar riskBar = new ProgressBar(0.6);
        riskBar.setMaxWidth(Double.MAX_VALUE);
        riskBar.setStyle("-fx-accent: " + ACCENT_COLOR + ";");

        // Risk tolerance selector
        HBox riskButtons = new HBox(10);
        riskButtons.setAlignment(Pos.CENTER);

        ToggleGroup riskGroup = new ToggleGroup();
        String[] riskLevels = {"Low", "Medium", "High"};

        for (int i = 0; i < riskLevels.length; i++) {
            ToggleButton btn = new ToggleButton(riskLevels[i]);
            btn.setToggleGroup(riskGroup);
            btn.setUserData((i + 1) * 0.3); // 0.3, 0.6, 0.9
            btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXT_COLOR + ";" +
                "-fx-border-color: " + ACCENT_COLOR + ";" +
                "-fx-border-radius: 5;"
            );

            final int index = i;
            btn.setOnAction(e -> {
                riskBar.setProgress((double)btn.getUserData());
                riskLabel.setText("Risk Level: " + riskLevels[index]);
            });

            riskButtons.getChildren().add(btn);
        }

        // Select default risk level (Medium)
        ((ToggleButton)riskButtons.getChildren().get(1)).setSelected(true);

        // Trade type
        ToggleGroup tradeType = new ToggleGroup();
        HBox tradeButtons = new HBox(10);
        tradeButtons.setAlignment(Pos.CENTER);

        ToggleButton buyBtn = new ToggleButton("BUY");
        buyBtn.setToggleGroup(tradeType);
        buyBtn.setSelected(true);
        buyBtn.setMaxWidth(Double.MAX_VALUE);
        buyBtn.setStyle(
            "-fx-background-color: " + SUCCESS_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        HBox.setHgrow(buyBtn, Priority.ALWAYS);

        ToggleButton sellBtn = new ToggleButton("SELL");
        sellBtn.setToggleGroup(tradeType);
        sellBtn.setMaxWidth(Double.MAX_VALUE);
        sellBtn.setStyle(
            "-fx-background-color: " + DANGER_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;"
        );
        HBox.setHgrow(sellBtn, Priority.ALWAYS);

        tradeButtons.getChildren().addAll(buyBtn, sellBtn);

        // Submit button
        Button submitButton = new Button("Place Order");
        submitButton.setMaxWidth(Double.MAX_VALUE);
        submitButton.setStyle(
            "-fx-background-color: " + ACCENT_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;"
        );

        panel.getChildren().addAll(
            title,
            new Separator(),
            stockLabel,
            stockCombo,
            amountLabel,
            amountSlider,
            riskLabel,
            riskBar,
            riskButtons,
            new Separator(),
            tradeButtons,
            submitButton
        );

        return panel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
