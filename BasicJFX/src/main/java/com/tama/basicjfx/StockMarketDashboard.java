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
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class StockMarketDashboard extends Application {

    private VBox mainLayout;

    // Modern color palette
    private final String BG_COLOR = "#0F172A";      // Dark blue background
    private final String CARD_BG = "#1E293B";       // Slightly lighter blue for cards
    private final String PRIMARY = "#3B82F6";       // Bright blue for primary actions
    private final String SUCCESS = "#10B981";       // Green for positive values
    private final String DANGER = "#EF4444";        // Red for negative values
    private final String TEXT_PRIMARY = "#F8FAFC";  // Almost white for primary text
    private final String TEXT_SECONDARY = "#94A3B8"; // Lighter gray for secondary text

    @Override
    public void start(Stage stage) {
        stage.setTitle("Stock Market Analytics");

        // Main container
        mainLayout = new VBox(20);
        mainLayout.setStyle(
            "-fx-background-color: " + BG_COLOR + ";" +
            "-fx-font-family: 'SF Pro Display', 'Segoe UI', Arial;"
        );
        mainLayout.setPadding(new Insets(20));

        // Create dashboard sections
        VBox headerSection = createHeaderSection();
        HBox statsSection = createStatsSection();
        HBox mainSection = createMainSection();

        mainLayout.getChildren().addAll(headerSection, statsSection, mainSection);

        // Create scene
        Scene scene = new Scene(mainLayout, 1400, 900);
        stage.setScene(scene);

        // Add startup animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), mainLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        stage.show();
    }

    private VBox createHeaderSection() {
        VBox header = new VBox(15);

        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Stock Market Analytics");
        title.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Market selector with custom styling
        ComboBox<String> marketSelect = new ComboBox<>();
        marketSelect.getItems().addAll("NYSE", "NASDAQ", "LSE", "TSE");
        marketSelect.setValue("NYSE");
        marketSelect.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-size: 14px;"
        );

        // Market status indicator
        Label marketStatus = new Label("Market Open");
        marketStatus.setStyle(
            "-fx-background-color: " + SUCCESS + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-padding: 5 15;" +
            "-fx-background-radius: 15;"
        );

        topBar.getChildren().addAll(title, spacer, marketSelect, marketStatus);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search stocks...");
        searchField.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-prompt-text-fill: " + TEXT_SECONDARY + ";" +
            "-fx-padding: 10;" +
            "-fx-background-radius: 8;"
        );
        searchField.setMaxWidth(300);

        header.getChildren().addAll(topBar, searchField);
        return header;
    }

    private HBox createStatsSection() {
        HBox stats = new HBox(20);

        String[][] statsData = {
            {"S&P 500", "+1.2%", "4,587.64", SUCCESS},
            {"NASDAQ", "-0.8%", "14,340.99", DANGER},
            {"DOW JONES", "+0.5%", "35,950.89", SUCCESS},
            {"VIX", "+2.1%", "18.56", DANGER}
        };

        for (String[] data : statsData) {
            VBox card = new VBox(8);
            card.setStyle(
                "-fx-background-color: " + CARD_BG + ";" +
                "-fx-background-radius: 10;" +
                "-fx-padding: 20;" +
                "-fx-min-width: 220;"
            );

            Label name = new Label(data[0]);
            name.setStyle(
                "-fx-text-fill: " + TEXT_SECONDARY + ";" +
                "-fx-font-size: 14px;"
            );

            Label change = new Label(data[1]);
            change.setStyle(
                "-fx-text-fill: " + data[3] + ";" +
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;"
            );

            Label value = new Label(data[2]);
            value.setStyle(
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-font-size: 16px;"
            );

            card.getChildren().addAll(name, change, value);
            stats.getChildren().add(card);
        }

        return stats;
    }

    private HBox createMainSection() {
        HBox main = new HBox(20);
        VBox.setVgrow(main, Priority.ALWAYS);

        // Charts section
        VBox chartsSection = createChartsSection();
        HBox.setHgrow(chartsSection, Priority.ALWAYS);

        // Trading panel
        VBox tradingPanel = createTradingPanel();
        tradingPanel.setMinWidth(320);
        tradingPanel.setMaxWidth(320);

        main.getChildren().addAll(chartsSection, tradingPanel);
        return main;
    }

    private VBox createChartsSection() {
        VBox charts = new VBox(20);

        // Stock price chart
        VBox priceChart = createPriceChart();
        VBox.setVgrow(priceChart, Priority.ALWAYS);

        // Bottom section with distribution and volume
        HBox bottomCharts = new HBox(20);
        bottomCharts.setMinHeight(300);

        VBox distribution = createDistributionChart();
        VBox volume = createVolumeChart();

        HBox.setHgrow(distribution, Priority.ALWAYS);
        HBox.setHgrow(volume, Priority.ALWAYS);

        bottomCharts.getChildren().addAll(distribution, volume);

        charts.getChildren().addAll(priceChart, bottomCharts);
        return charts;
    }

    private VBox createPriceChart() {
        VBox container = new VBox(15);
        container.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        // Chart header
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("AAPL Stock Price");
        title.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Time period toggles
        ToggleGroup timeGroup = new ToggleGroup();
        HBox timeToggles = new HBox(5);
        String[] periods = {"1D", "1W", "1M", "3M", "6M", "1Y", "ALL"};

        for (String period : periods) {
            ToggleButton tb = new ToggleButton(period);
            tb.setToggleGroup(timeGroup);
            tb.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-padding: 5 10;" +
                "-fx-cursor: hand;"
            );
            timeToggles.getChildren().add(tb);
        }

        ((ToggleButton)timeToggles.getChildren().get(2)).setSelected(true);

        header.getChildren().addAll(title, spacer, timeToggles);

        // Create chart
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.web(TEXT_PRIMARY));
        yAxis.setTickLabelFill(Color.web(TEXT_PRIMARY));

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setStyle("-fx-background-color: transparent;");
        lineChart.setCreateSymbols(false);
        lineChart.setAnimated(false);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("AAPL");

        double[] prices = {150.25, 152.35, 151.80, 153.65, 155.90, 154.75, 156.25,
                          158.40, 157.85, 159.20, 161.45, 160.90, 162.30, 164.55};

        for (int i = 0; i < prices.length; i++) {
            series.getData().add(new XYChart.Data<>(i, prices[i]));
        }

        lineChart.getData().add(series);

        container.getChildren().addAll(header, lineChart);
        return container;
    }

    private VBox createDistributionChart() {
        VBox container = new VBox(10);
        container.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        Label title = new Label("Portfolio Distribution");
        title.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;"
        );

        PieChart pieChart = new PieChart();
        pieChart.setStyle("-fx-background-color: transparent;");
        pieChart.setLabelsVisible(true);

        pieChart.getData().addAll(
            new PieChart.Data("Technology", 35),
            new PieChart.Data("Healthcare", 25),
            new PieChart.Data("Finance", 20),
            new PieChart.Data("Consumer", 12),
            new PieChart.Data("Energy", 8)
        );

        container.getChildren().addAll(title, pieChart);
        return container;
    }

    private VBox createVolumeChart() {
        VBox container = new VBox(10);
        container.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        Label title = new Label("Trading Volume");
        title.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;"
        );

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(Color.web(TEXT_PRIMARY));
        yAxis.setTickLabelFill(Color.web(TEXT_PRIMARY));

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().addAll(
            new XYChart.Data<>("Mon", 45.2),
            new XYChart.Data<>("Tue", 38.7),
            new XYChart.Data<>("Wed", 52.3),
            new XYChart.Data<>("Thu", 41.8),
            new XYChart.Data<>("Fri", 48.9)
        );

        barChart.getData().add(series);

        container.getChildren().addAll(title, barChart);
        return container;
    }

    private VBox createTradingPanel() {
        VBox panel = new VBox(20);
        panel.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;"
        );

        Label title = new Label("Trading Panel");
        title.setStyle(
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;"
        );

        // Stock selector
        Label stockLabel = new Label("Select Stock");
        stockLabel.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

        ComboBox<String> stockCombo = new ComboBox<>();
        stockCombo.getItems().addAll("AAPL", "GOOGL", "MSFT", "AMZN", "TSLA", "META", "NVDA");
        stockCombo.setValue("AAPL");
        stockCombo.setMaxWidth(Double.MAX_VALUE);
        stockCombo.setStyle(
            "-fx-background-color: " + BG_COLOR + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";"
        );

        // Amount controls
        Label amountLabel = new Label("Investment Amount: $10,000");
        amountLabel.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

        Slider amountSlider = new Slider(1000, 100000, 10000);
        amountSlider.setShowTickLabels(true);
        amountSlider.setShowTickMarks(true);
        amountSlider.setMajorTickUnit(20000);

        amountSlider.valueProperty().addListener((obs, oldVal, newVal) ->
            amountLabel.setText(String.format("Investment Amount: $%,.0f", newVal.doubleValue()))
        );

        // Risk level
        Label riskLabel = new Label("Risk Level");
        riskLabel.setStyle("-fx-text-fill: " + TEXT_SECONDARY + ";");

        ProgressBar riskBar = new ProgressBar(0.6);
        riskBar.setMaxWidth(Double.MAX_VALUE);
        riskBar.setStyle("-fx-accent: " + PRIMARY + ";");

        // Risk selector buttons
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
                "-fx-text-fill: " + TEXT_PRIMARY + ";" +
                "-fx-border-color: " + PRIMARY + ";" +
                "-fx-border-radius: 5;"
            );

            final int index = i;
            btn.setOnAction(e -> {
                riskBar.setProgress((double)btn.getUserData());
                riskLabel.setText("Risk Level: " + riskLevels[index]);
            });

            riskButtons.getChildren().add(btn);
        }

        ((ToggleButton)riskButtons.getChildren().get(1)).setSelected(true);

        // Buy/Sell buttons
        HBox tradeButtons = new HBox(10);
        tradeButtons.setAlignment(Pos.CENTER);

        Button buyButton = new Button("BUY");
        buyButton.setMaxWidth(Double.MAX_VALUE);
        buyButton.setStyle(
            "-fx-background-color: " + SUCCESS + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;"
        );
        HBox.setHgrow(buyButton, Priority.ALWAYS);

        Button sellButton = new Button("SELL");
        sellButton.setMaxWidth(Double.MAX_VALUE);
        sellButton.setStyle(
            "-fx-background-color: " + DANGER + ";" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 10;"
        );
        HBox.setHgrow(sellButton, Priority.ALWAYS);

        tradeButtons.getChildren().addAll(buyButton, sellButton);

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
            tradeButtons
        );

        return panel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
