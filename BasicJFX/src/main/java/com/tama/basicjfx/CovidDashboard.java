package com.tama.basicjfx;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class CovidDashboard extends Application {

    // Warna tema dashboard
    private final String BG_COLOR = "#F8F9FA";
    private final String CARD_BG = "#FFFFFF";
    private final String PRIMARY = "#0D6EFD";
    private final String DANGER = "#DC3545";
    private final String SUCCESS = "#198754";
    private final String WARNING = "#FFC107";
    private final String INFO = "#0DCAF0";
    private final String TEXT_DARK = "#212529";
    private final String TEXT_MUTED = "#6C757D";
    
    // Komponen untuk bagian vaksinasi
    private Slider vaccinationSlider;
    private Label vaccinationPercentLabel;
    private ProgressBar vaccinationProgress;
    private BorderPane mainLayout;
    
    @Override
    public void start(Stage stage) {
        // Membuat layout utama
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: " + BG_COLOR + ";");
        
        // Membuat komponen-komponen dashboard
        VBox header = createHeader();
        HBox statsCards = createStatCards();
        VBox mainContent = createMainContent();
        HBox footer = createFooter();
        
        // Menambahkan margin pada komponen
        VBox.setMargin(statsCards, new Insets(20, 20, 10, 20));
        VBox.setMargin(mainContent, new Insets(10, 20, 20, 20));
        
        // Menggabungkan komponen ke dalam layout utama
        VBox centerContent = new VBox(15);
        centerContent.getChildren().addAll(statsCards, mainContent);
        
        mainLayout.setTop(header);
        mainLayout.setCenter(centerContent);
        mainLayout.setBottom(footer);
        
        // Membuat scene dengan animasi
        Scene scene = new Scene(mainLayout, 1280, 800);
        stage.setTitle("Dashboard Pemantauan COVID-19");
        stage.setScene(scene);
        
        // Menambahkan animasi fade-in
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), mainLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
        
        stage.show();
    }
    
    private VBox createHeader() {
        VBox header = new VBox(15);
        header.setStyle(
            "-fx-background-color: " + PRIMARY + ";" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
        header.setPadding(new Insets(20));
        
        HBox topRow = new HBox(20);
        topRow.setAlignment(Pos.CENTER_LEFT);
        
        // Icon COVID-19
        try {
            ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream("covid.png")));
            iconView.setFitHeight(40);
            iconView.setFitWidth(40);
            iconView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(255,255,255,0.2), 10, 0, 0, 0);");
            topRow.getChildren().add(iconView);
        } catch (Exception e) {
            System.out.println("Error loading icon: " + e.getMessage());
            // Fallback jika gambar tidak ditemukan
            Label iconFallback = new Label("🦠");
            iconFallback.setStyle("-fx-font-size: 30px; -fx-text-fill: white;");
            topRow.getChildren().add(iconFallback);
        }
        
        // Judul dashboard
        Label title = new Label("Dashboard Pemantauan COVID-19");
        title.setStyle(
            "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );
        
        // Tanggal update
        Label updateLabel = new Label("Terakhir diupdate: 30/03/2023 15:35");
        updateLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: rgba(255,255,255,0.8);"
        );
        
        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Filter dan kontrol
        ComboBox<String> regionSelect = new ComboBox<>();
        regionSelect.getItems().addAll(
            "Semua Wilayah", "DKI Jakarta", "Jawa Barat", "Jawa Tengah",
            "Jawa Timur", "Banten", "Sulawesi Selatan"
        );
        regionSelect.setValue("Semua Wilayah");
        regionSelect.setStyle(
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
        
        topRow.getChildren().addAll(title, spacer, regionSelect, datePicker);
        header.getChildren().add(topRow);
        
        // Menambahkan menu navigasi
        HBox navMenu = new HBox(20);
        navMenu.setAlignment(Pos.CENTER_LEFT);
        navMenu.setPadding(new Insets(10, 0, 0, 0));
        
        String menuStyle = "-fx-text-fill: white; -fx-font-size: 14px; -fx-cursor: hand;";
        Label dataMenu = new Label("Data");
        dataMenu.setStyle(menuStyle + "-fx-font-weight: bold; -fx-underline: true;");
        
        Label dataNasionalMenu = new Label("Data Nasional");
        dataNasionalMenu.setStyle(menuStyle);
        
        Label dataProvinsiMenu = new Label("Data Provinsi");
        dataProvinsiMenu.setStyle(menuStyle);
        
        Label fasilitasMenu = new Label("Fasilitas Kesehatan");
        fasilitasMenu.setStyle(menuStyle);
        
        Label edukasiMenu = new Label("Edukasi");
        edukasiMenu.setStyle(menuStyle);
        
        Label cekKondisiMenu = new Label("Cek Kondisi");
        cekKondisiMenu.setStyle(menuStyle);
        
        Label tanyaJawabMenu = new Label("Tanya Jawab");
        tanyaJawabMenu.setStyle(menuStyle);
        
        navMenu.getChildren().addAll(
            dataMenu, dataNasionalMenu, dataProvinsiMenu, 
            fasilitasMenu, edukasiMenu, cekKondisiMenu, tanyaJawabMenu
        );
        
        header.getChildren().add(navMenu);
        
        return header;
    }
    
    private HBox createStatCards() {
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER);
        
        // Membuat kartu statistik dengan warna berbeda
        createStatCard(stats, "Terkonfirmasi", "1,135,345", "+1,532 Kasus", DANGER);
        createStatCard(stats, "Dalam Perawatan", "95,123", "8.4% dari terkonfirmasi", WARNING);
        createStatCard(stats, "Sembuh", "986,324", "86.9% dari terkonfirmasi", SUCCESS);
        createStatCard(stats, "Meninggal", "53,898", "4.7% dari terkonfirmasi", INFO);
        
        return stats;
    }
    
    private void createStatCard(HBox parent, String title, String value, String subtext, String color) {
        VBox card = new VBox(10);
        card.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        card.setPrefWidth(250);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";"
        );
        
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + color + ";"
        );
        
        Label subtextLabel = new Label(subtext);
        subtextLabel.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";"
        );
        
        card.getChildren().addAll(titleLabel, valueLabel, subtextLabel);
        parent.getChildren().add(card);
    }
    
    private VBox createMainContent() {
        VBox content = new VBox(20);
        
        // Bagian atas: Peta dan Tabel Provinsi
        HBox upperSection = new HBox(20);
        upperSection.getChildren().addAll(
            createMapSection(),
            createProvinceTable()
        );
        
        // Bagian bawah: Grafik Trend dan Progress Vaksinasi
        HBox lowerSection = new HBox(20);
        lowerSection.getChildren().addAll(
            createTrendCharts(),
            createVaccinationSection()
        );
        
        content.getChildren().addAll(upperSection, lowerSection);
        return content;
    }
    
    private VBox createMapSection() {
        VBox container = new VBox(15);
        container.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setPrefWidth(600);
        
        Label title = new Label("Peta Persebaran COVID-19");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Membuat container untuk peta
        StackPane mapContainer = new StackPane();
        mapContainer.setId("mapContainer");
        mapContainer.setStyle(
            "-fx-background-color: #E9ECEF;" +
            "-fx-background-radius: 5;"
        );
        mapContainer.setPrefHeight(300);
    
        try {
            // Load gambar peta Indonesia
            Image mapImage = new Image(getClass().getResourceAsStream("indonesia.jpg"));
            ImageView mapView = new ImageView(mapImage);
    
            // Mengatur ukuran gambar agar sesuai dengan container
            mapView.setFitWidth(580);  // Sedikit lebih kecil dari container untuk padding
            mapView.setFitHeight(280);
            mapView.setPreserveRatio(true);
            mapView.setSmooth(true);
    
            // Menambahkan efek hover dan tooltip pada wilayah-wilayah tertentu
            addRegionTooltip(mapView, "DKI Jakarta: 598,234 Kasus", DANGER);
            addRegionTooltip(mapView, "Jawa Barat: 398,765 Kasus", DANGER);
            addRegionTooltip(mapView, "Jawa Timur: 266,789 Kasus", WARNING);
            addRegionTooltip(mapView, "Jawa Tengah: 243,567 Kasus", WARNING);
            addRegionTooltip(mapView, "Banten: 184,532 Kasus", INFO);
    
            mapContainer.getChildren().clear(); // Hapus fallback label
            mapContainer.getChildren().add(mapView);
    
        } catch (Exception e) {
            System.out.println("Error loading map image: " + e.getMessage());
            // Fallback jika gambar tidak dapat dimuat
            Label fallbackLabel = new Label("Peta Indonesia tidak dapat dimuat");
            fallbackLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
            mapContainer.getChildren().add(fallbackLabel);
        }
    
        // Kontrol zoom peta
        HBox mapControls = new HBox(10);
        mapControls.setAlignment(Pos.CENTER_RIGHT);
        
        Button zoomInBtn = new Button("+");
        styleMapButton(zoomInBtn);
        zoomInBtn.setOnAction(e -> zoomMap(true));
    
        Button zoomOutBtn = new Button("-");
        styleMapButton(zoomOutBtn);
        zoomOutBtn.setOnAction(e -> zoomMap(false));
    
        Button resetBtn = new Button("↺");
        styleMapButton(resetBtn);
        resetBtn.setOnAction(e -> resetMap());
    
        mapControls.getChildren().addAll(zoomOutBtn, zoomInBtn, resetBtn);
    
        container.getChildren().addAll(title, mapContainer, mapControls);
        return container;
    }

    private void addRegionTooltip(ImageView mapView, String tooltipText, String color) {
        Tooltip tooltip = new Tooltip(tooltipText);
        tooltip.setStyle(
            "-fx-background-color: " + color + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 5 10;"
        );
        
        mapView.setOnMouseMoved(e -> {
            tooltip.show(mapView, e.getScreenX() + 10, e.getScreenY() + 10);
        });
    
        mapView.setOnMouseExited(e -> {
            tooltip.hide();
        });
    }

    private void styleMapButton(Button button) {
        button.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DEE2E6;" +
            "-fx-border-radius: 3;" +
            "-fx-min-width: 30px;" +
            "-fx-min-height: 30px;" +
            "-fx-cursor: hand;"
        );
    }

    private void zoomMap(boolean zoomIn) {
        StackPane mapContainer = (StackPane) mainLayout.lookup("#mapContainer");
        if (mapContainer != null && !mapContainer.getChildren().isEmpty()) {
            Node mapView = mapContainer.getChildren().get(0);
            if (mapView instanceof ImageView) {
                ImageView map = (ImageView) mapView;
                double scale = zoomIn ? 1.1 : 0.9;
                map.setFitWidth(map.getFitWidth() * scale);
                map.setFitHeight(map.getFitHeight() * scale);
            }
        }
    }

    private void resetMap() {
        StackPane mapContainer = (StackPane) mainLayout.lookup("#mapContainer");
        if (mapContainer != null && !mapContainer.getChildren().isEmpty()) {
            Node mapView = mapContainer.getChildren().get(0);
            if (mapView instanceof ImageView) {
                ImageView map = (ImageView) mapView;
                map.setFitWidth(580);
                map.setFitHeight(280);
            }
        }
    }
    
    private VBox createProvinceTable() {
        VBox container = new VBox(15);
        container.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setPrefWidth(400);
        
        Label title = new Label("Kasus per Provinsi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Navigasi tabel
        HBox tableNav = new HBox(10);
        tableNav.setAlignment(Pos.CENTER_RIGHT);
        
        Button prevBtn = new Button("<");
        prevBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DEE2E6;" +
            "-fx-border-radius: 3;"
        );
        
        Button nextBtn = new Button(">");
        nextBtn.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #DEE2E6;" +
            "-fx-border-radius: 3;"
        );
        
        tableNav.getChildren().addAll(prevBtn, nextBtn);
        
        // Tabel provinsi
        ListView<HBox> provinceList = new ListView<>();
        provinceList.setPrefHeight(300);
        
        // Data provinsi
        addProvinceRow(provinceList, "DKI Jakarta", "598,234", true);
        addProvinceRow(provinceList, "Jawa Barat", "398,765", true);
        addProvinceRow(provinceList, "Banten", "184,532", false);
        addProvinceRow(provinceList, "Jawa Timur", "266,789", true);
        addProvinceRow(provinceList, "Jawa Tengah", "243,567", true);
        addProvinceRow(provinceList, "Sulawesi Selatan", "129,876", true);
        addProvinceRow(provinceList, "D.I. Yogyakarta", "122,345", false);
        addProvinceRow(provinceList, "Kalimantan Timur", "111,234", true);
        
        container.getChildren().addAll(title, tableNav, provinceList);
        return container;
    }
    
    private void addProvinceRow(ListView<HBox> listView, String province, String cases, boolean increasing) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(10, 15, 10, 15));
        
        Label provinceLabel = new Label(province);
        provinceLabel.setStyle("-fx-font-weight: bold;");
        provinceLabel.setPrefWidth(200);
        
        Label casesLabel = new Label(cases);
        casesLabel.setStyle("-fx-font-weight: bold;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label trendLabel = new Label(increasing ? "▲" : "▼");
        trendLabel.setStyle("-fx-text-fill: " + (increasing ? DANGER : SUCCESS) + "; -fx-font-weight: bold;");
        
        row.getChildren().addAll(provinceLabel, spacer, casesLabel, trendLabel);
        listView.getItems().add(row);
    }
    
    private HBox createTrendCharts() {
        HBox container = new HBox(20);
        container.setAlignment(Pos.CENTER);
        
        // Grafik trend kasus baru
        VBox newCasesChart = createTrendChart("Kasus Baru per Hari", DANGER);
        
        // Grafik trend kesembuhan
        VBox recoveryChart = createTrendChart("Kasus Sembuh per Hari", SUCCESS);
        
        // Grafik trend kematian
        VBox deathChart = createTrendChart("Kasus Meninggal per Hari", INFO);
        
        container.getChildren().addAll(newCasesChart, recoveryChart, deathChart);
        return container;
    }
    
    private VBox createTrendChart(String title, String color) {
        VBox chart = new VBox(10);
        chart.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 15;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        chart.setPrefWidth(300);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        // Membuat area chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                return String.format("%d", object.intValue());
            }
        });
        
        AreaChart<String, Number> areaChart = new AreaChart<>(xAxis, yAxis);
        areaChart.setCreateSymbols(true);
        areaChart.setLegendVisible(false);
        areaChart.setPrefHeight(150);
        
        // Set chart style
        areaChart.setStyle(
            ".chart-series-area-fill { -fx-fill: " + color + "33; }" +
            ".chart-series-area-line { -fx-stroke: " + color + "; }"
        );

        // Menambahkan data dengan tooltip
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        String[][] dataPoints;
        
        if (title.contains("Kasus Baru")) {
            dataPoints = new String[][]{
                {"1", "1532"}, {"2", "1423"}, {"3", "1654"}, {"4", "1245"},
                {"5", "1876"}, {"6", "1765"}, {"7", "1543"}, {"8", "1432"}
            };
        } else if (title.contains("Sembuh")) {
            dataPoints = new String[][]{
                {"1", "1245"}, {"2", "1354"}, {"3", "1432"}, {"4", "1543"},
                {"5", "1621"}, {"6", "1532"}, {"7", "1432"}, {"8", "1321"}
            };
        } else {
            dataPoints = new String[][]{
                {"1", "87"}, {"2", "92"}, {"3", "78"}, {"4", "85"},
                {"5", "76"}, {"6", "82"}, {"7", "79"}, {"8", "73"}
            };
        }

        for (String[] point : dataPoints) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(
                point[0], Integer.parseInt(point[1])
            );

            // Add tooltip and hover effect to each data point
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    // Create tooltip
                    Tooltip tooltip = new Tooltip(
                        "Hari " + point[0] + "\nJumlah: " + point[1]
                    );
                    tooltip.setStyle(
                        "-fx-background-color: #2C3E50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 8px;"
                    );

                    // Add hover effect
                    newNode.setOnMouseEntered(e -> {
                        newNode.setStyle(
                            "-fx-background-color: " + color + ";" +
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

            series.getData().add(data);
        }

        areaChart.getData().add(series);
        chart.getChildren().addAll(titleLabel, areaChart);
        return chart;
    }
    
    private VBox createVaccinationSection() {
        VBox container = new VBox(15);
        container.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 20;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 2);"
        );
        container.setPrefWidth(400);
        
        Label title = new Label("Progress Vaksinasi");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        // Slider untuk mengatur persentase vaksinasi
        HBox sliderContainer = new HBox(15);
        sliderContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label sliderLabel = new Label("Persentase:");
        
        vaccinationSlider = new Slider(0, 100, 65);
        vaccinationSlider.setPrefWidth(200);
        HBox.setHgrow(vaccinationSlider, Priority.ALWAYS);
        
        vaccinationPercentLabel = new Label("65%");
        vaccinationPercentLabel.setStyle("-fx-font-weight: bold;");
        
        sliderContainer.getChildren().addAll(sliderLabel, vaccinationSlider, vaccinationPercentLabel);
        
        // Progress bar vaksinasi
        vaccinationProgress = new ProgressBar(0.65);
        vaccinationProgress.setStyle("-fx-accent: " + SUCCESS + ";");
        vaccinationProgress.setPrefWidth(Double.MAX_VALUE);
        vaccinationProgress.setPrefHeight(20);
        
        // Event handler untuk slider
        vaccinationSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double value = newVal.doubleValue();
            vaccinationPercentLabel.setText(String.format("%.0f%%", value));
            vaccinationProgress.setProgress(value / 100.0);
        });
        
        // Tabel informasi vaksinasi
        TableView<VaccinationData> vaccinationTable = new TableView<>();
        vaccinationTable.setPrefHeight(200);
        
        TableColumn<VaccinationData, String> groupCol = new TableColumn<>("Kelompok");
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        groupCol.setPrefWidth(150);
        
        TableColumn<VaccinationData, String> targetCol = new TableColumn<>("Target");
        targetCol.setCellValueFactory(new PropertyValueFactory<>("target"));
        targetCol.setPrefWidth(100);
        
        TableColumn<VaccinationData, String> vaccinatedCol = new TableColumn<>("Tervaksin");
        vaccinatedCol.setCellValueFactory(new PropertyValueFactory<>("vaccinated"));
        vaccinatedCol.setPrefWidth(100);
        
        vaccinationTable.getColumns().addAll(groupCol, targetCol, vaccinatedCol);
        
        // Data vaksinasi
        ObservableList<VaccinationData> data = FXCollections.observableArrayList(
            new VaccinationData("Tenaga Kesehatan", "1,500,000", "1,450,000"),
            new VaccinationData("Lansia", "21,500,000", "12,350,000"),
            new VaccinationData("Petugas Publik", "16,900,000", "9,870,000"),
            new VaccinationData("Masyarakat Umum", "141,000,000", "85,600,000")
        );
        
        vaccinationTable.setItems(data);
        
        // Combo box untuk jenis vaksin
        HBox vaccineTypeContainer = new HBox(15);
        vaccineTypeContainer.setAlignment(Pos.CENTER_LEFT);
        
        Label vaccineTypeLabel = new Label("Jenis Vaksin:");
        
        ComboBox<String> vaccineTypeCombo = new ComboBox<>();
        vaccineTypeCombo.getItems().addAll(
            "Semua Jenis", "Sinovac", "AstraZeneca", "Pfizer", "Moderna", "Sinopharm"
        );
        vaccineTypeCombo.setValue("Semua Jenis");
        vaccineTypeCombo.setPrefWidth(200);
        
        vaccineTypeContainer.getChildren().addAll(vaccineTypeLabel, vaccineTypeCombo);
        
        container.getChildren().addAll(
            title,
            new Separator(),
            sliderContainer,
            vaccinationProgress,
            new Separator(),
            vaccinationTable,
            new Separator(),
            vaccineTypeContainer
        );
        
        return container;
    }
    
    private HBox createFooter() {
        HBox footer = new HBox(20);
        footer.setStyle(
            "-fx-background-color: " + CARD_BG + ";" +
            "-fx-border-color: #DEE2E6;" +
            "-fx-border-width: 1 0 0 0;" +
            "-fx-padding: 15;"
        );
        footer.setAlignment(Pos.CENTER_LEFT);
        
        Label helpLabel = new Label("Pertanyaan?");
        helpLabel.setStyle("-fx-font-weight: bold;");
        
        Label hotlineLabel = new Label("COVID-19 Hotline");
        
        Button callButton = new Button("119 ext 9");
        callButton.setStyle(
            "-fx-background-color: " + PRIMARY + ";" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 20;"
        );
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label copyrightLabel = new Label("© 2023 Kementerian Kesehatan RI");
        copyrightLabel.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
        
        footer.getChildren().addAll(helpLabel, hotlineLabel, callButton, spacer, copyrightLabel);
        
        return footer;
    }
    
    // Kelas data untuk tabel vaksinasi
    public static class VaccinationData {
        private final SimpleStringProperty group;
        private final SimpleStringProperty target;
        private final SimpleStringProperty vaccinated;
        
        public VaccinationData(String group, String target, String vaccinated) {
            this.group = new SimpleStringProperty(group);
            this.target = new SimpleStringProperty(target);
            this.vaccinated = new SimpleStringProperty(vaccinated);
        }
        
        public String getGroup() { return group.get(); }
        public String getTarget() { return target.get(); }
        public String getVaccinated() { return vaccinated.get(); }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}