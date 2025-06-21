package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CovidDashboardModern extends Application {

    // Color scheme
    private final String BG_COLOR = "#FFFFFF";
    private final String CARD_BG = "#F8F9FA";
    private final String PRIMARY = "#0D6EFD";
    private final String DANGER = "#DC3545";
    private final String SUCCESS = "#198754";
    private final String WARNING = "#FFC107";
    private final String INFO = "#0DCAF0";

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        // Create main sections
        VBox header = createHeader();
        HBox stats = createStatCards();
        VBox mainContent = createMainContent();

        // Add padding to main content
        VBox.setMargin(stats, new Insets(20));
        VBox.setMargin(mainContent, new Insets(20));

        // Combine all sections
        VBox centerContent = new VBox(20);
        centerContent.getChildren().addAll(stats, mainContent);

        root.setTop(header);
        root.setCenter(centerContent);

        // Create scene with animation
        Scene scene = new Scene(root, 1280, 800);
        stage.setTitle("COVID-19 Dashboard Indonesia");
        stage.setScene(scene);

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        stage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        header.setPadding(new Insets(20));

        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER_LEFT);

        // Load COVID icon
        try {
            ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream("covid.png")));
            iconView.setFitHeight(40);
            iconView.setFitWidth(40);
            topRow.getChildren().add(iconView);
        } catch (Exception e) {
            System.out.println("Error loading icon: " + e.getMessage());
        }

        // Title
        Label title = new Label("COVID-19 Dashboard Indonesia");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #2C3E50;"
        );

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Controls
        ComboBox<String> provinceSelect = new ComboBox<>();
        provinceSelect.getItems().addAll(
            "DKI Jakarta", "Jawa Barat", "Jawa Tengah",
            "Jawa Timur", "Bali", "Semua Provinsi"
        );
        provinceSelect.setValue("Semua Provinsi");
        provinceSelect.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DEE2E6;" +
            "-fx-border-radius: 5;"
        );

        DatePicker datePicker = new DatePicker();
        datePicker.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DEE2E6;" +
            "-fx-border-radius: 5;"
        );

        topRow.getChildren().addAll(title, spacer, provinceSelect, datePicker);
        header.getChildren().add(topRow);

        return header;
    }

    private HBox createStatCards() {
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER);

        // Create stat cards with different colors
        createStatCard(stats, "Total Kasus", "5,427,890", "↑2,154", DANGER);
        createStatCard(stats, "Sembuh", "5,265,231", "↑1,879", SUCCESS);
        createStatCard(stats, "Dalam Perawatan", "25,234", "↑275", WARNING);
        createStatCard(stats, "Meninggal", "137,425", "↑31", INFO);

        return stats;
    }

    private void createStatCard(HBox parent, String title, String value, String change, String color) {
        VBox card = new VBox(10);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        card.setPrefWidth(250);

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #6C757D;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + color + ";"
        );

        Label changeLabel = new Label(change);
        changeLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + color + ";"
        );

        card.getChildren().addAll(titleLabel, valueLabel, changeLabel);
        parent.getChildren().add(card);
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);

        // Upper section: Charts
        HBox upperSection = new HBox(20);
        upperSection.getChildren().addAll(
            createCasesChart(),
            createVaccinationProgress()
        );

        // Lower section: Table and Pie Chart
        HBox lowerSection = new HBox(20);
        lowerSection.getChildren().addAll(
            createDataTable(),
            createDistributionChart()
        );

        content.getChildren().addAll(upperSection, lowerSection);
        return content;
    }

    private VBox createCasesChart() {
        VBox container = new VBox(10);
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        HBox.setHgrow(container, Priority.ALWAYS);

        Label title = new Label("Trend Kasus Harian");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create area chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setCreateSymbols(true); // Enable data points
        areaChart.setAnimated(true);

        // Customize chart appearance
        areaChart.setStyle(
            ".chart-series-area-fill { -fx-opacity: 0.3; }" +
            ".chart-series-area-line { -fx-stroke-width: 2px; }"
        );

        // Add data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Kasus Positif");

        // Sample data
        String[][] chartData = {
            {"Sen", "2154"}, {"Sel", "1987"}, {"Rab", "2356"},
            {"Kam", "2089"}, {"Jum", "1876"}, {"Sab", "1654"},
            {"Min", "1432"}
        };

        for (String[] data : chartData) {
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(
                data[0], Integer.parseInt(data[1])
            );

            // Add tooltip to each data point
            dataPoint.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    // Create tooltip
                    Tooltip tooltip = new Tooltip(
                        data[0] + "\nKasus: " + data[1]
                    );
                    tooltip.setStyle(
                        "-fx-background-color: #2C3E50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8px;"
                    );

                    // Add hover effect and tooltip
                    newNode.setOnMouseEntered(e -> {
                        newNode.setStyle(
                            "-fx-background-color: #2C3E50;" +
                            "-fx-background-radius: 5px;" +
                            "-fx-padding: 5px;"
                        );
                        Tooltip.install(newNode, tooltip);
                    });

                    newNode.setOnMouseExited(e -> {
                        newNode.setStyle("");
                        Tooltip.uninstall(newNode, tooltip);
                    });
                }
            });

            series.getData().add(dataPoint);
        }

        areaChart.getData().add(series);
        container.getChildren().addAll(title, areaChart);

        return container;
    }

    private VBox createVaccinationProgress() {
        VBox container = new VBox(15);
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setPrefWidth(400);

        Label title = new Label("Progress Vaksinasi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Vaccination progress bars
        VBox progressContainer = new VBox(15);

        // Dosis 1
        addVaccinationProgress(progressContainer, "Dosis 1", 0.85, PRIMARY);

        // Dosis 2
        addVaccinationProgress(progressContainer, "Dosis 2", 0.72, SUCCESS);

        // Booster
        addVaccinationProgress(progressContainer, "Booster", 0.45, WARNING);

        // Age group selector
        Label ageLabel = new Label("Kelompok Usia:");
        ComboBox<String> ageSelect = new ComboBox<>();
        ageSelect.getItems().addAll(
            "Semua Usia",
            "12-17 Tahun",
            "18-59 Tahun",
            "60+ Tahun"
        );
        ageSelect.setValue("Semua Usia");
        ageSelect.setMaxWidth(Double.MAX_VALUE);

        container.getChildren().addAll(
            title,
            new Separator(),
            progressContainer,
            new Separator(),
            ageLabel,
            ageSelect
        );

        return container;
    }

    private void addVaccinationProgress(VBox container, String label, double progress, String color) {
        Label titleLabel = new Label(label + ": " + (int)(progress * 100) + "%");
        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setStyle("-fx-accent: " + color + ";");
        progressBar.setMaxWidth(Double.MAX_VALUE);

        container.getChildren().addAll(titleLabel, progressBar);
    }

    private VBox createDataTable() {
        VBox container = new VBox(10);
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        HBox.setHgrow(container, Priority.ALWAYS);

        Label title = new Label("Data per Provinsi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<CovidData> table = new TableView<>();

        // Create columns
        TableColumn<CovidData, String> provinceCol = new TableColumn<>("Provinsi");
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        TableColumn<CovidData, String> totalCol = new TableColumn<>("Total Kasus");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("totalCases"));

        TableColumn<CovidData, String> activeCol = new TableColumn<>("Kasus Aktif");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("activeCases"));

        TableColumn<CovidData, String> recoveredCol = new TableColumn<>("Sembuh");
        recoveredCol.setCellValueFactory(new PropertyValueFactory<>("recovered"));

        table.getColumns().addAll(provinceCol, totalCol, activeCol, recoveredCol);

        // Add sample data
        ObservableList<CovidData> data = FXCollections.observableArrayList(
            new CovidData("DKI Jakarta", "1,234,567", "2,345", "1,220,123"),
            new CovidData("Jawa Barat", "987,654", "1,876", "975,432"),
            new CovidData("Jawa Tengah", "765,432", "1,234", "756,789"),
            new CovidData("Jawa Timur", "654,321", "987", "645,678"),
            new CovidData("Bali", "234,567", "456", "230,987")
        );

        table.setItems(data);
        container.getChildren().addAll(title, table);

        return container;
    }

    private VBox createDistributionChart() {
        VBox container = new VBox(10);
        container.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setPrefWidth(400);

        Label title = new Label("Distribusi Kasus");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        PieChart pieChart = new PieChart();
        pieChart.setData(FXCollections.observableArrayList(
            new PieChart.Data("DKI Jakarta", 30),
            new PieChart.Data("Jawa Barat", 25),
            new PieChart.Data("Jawa Tengah", 20),
            new PieChart.Data("Jawa Timur", 15),
            new PieChart.Data("Lainnya", 10)
        ));

        container.getChildren().addAll(title, pieChart);
        return container;
    }

    // Data class untuk tabel
    public static class CovidData {
        private final SimpleStringProperty province;
        private final SimpleStringProperty totalCases;
        private final SimpleStringProperty activeCases;
        private final SimpleStringProperty recovered;

        public CovidData(String province, String totalCases, String activeCases, String recovered) {
            this.province = new SimpleStringProperty(province);
            this.totalCases = new SimpleStringProperty(totalCases);
            this.activeCases = new SimpleStringProperty(activeCases);
            this.recovered = new SimpleStringProperty(recovered);
        }

        // Getters
        public String getProvince() { return province.get(); }
        public String getTotalCases() { return totalCases.get(); }
        public String getActiveCases() { return activeCases.get(); }
        public String getRecovered() { return recovered.get(); }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
