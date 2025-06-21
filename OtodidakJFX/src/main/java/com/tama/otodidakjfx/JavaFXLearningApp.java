package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Aplikasi Utama untuk Belajar JavaFX 21
 * 
 * Menu aplikasi yang menggabungkan semua contoh pembelajaran:
 * 1. Aplikasi Sederhana
 * 2. Layout Examples
 * 3. Controls dan Events
 * 4. Styled Application
 * 5. TableView Management
 * 6. Calculator (FXML)
 * 7. Student Form (FXML)
 */
public class JavaFXLearningApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        
        // Title
        VBox titleBox = createTitleSection();
        root.setTop(titleBox);
        
        // Menu buttons
        VBox menuBox = createMenuSection();
        root.setCenter(menuBox);
        
        // Footer
        VBox footerBox = createFooterSection();
        root.setBottom(footerBox);
        
        // Create scene
        Scene scene = new Scene(root, 600, 500);
        
        // Load CSS
        String cssFile = getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(cssFile);
        
        primaryStage.setTitle("Belajar JavaFX 21 - Menu Utama");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createTitleSection() {
        VBox titleBox = new VBox(10);
        titleBox.setPadding(new Insets(30, 20, 20, 20));
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getStyleClass().add("card");
        
        Label mainTitle = new Label("🚀 Belajar JavaFX 21");
        mainTitle.getStyleClass().add("title");
        
        Label subtitle = new Label("Dari Dasar hingga Mahir");
        subtitle.getStyleClass().add("subtitle");
        
        Label description = new Label("Pilih contoh aplikasi yang ingin Anda pelajari:");
        description.setStyle("-fx-font-size: 14px; -fx-text-fill: #7f8c8d;");
        
        titleBox.getChildren().addAll(mainTitle, subtitle, description);
        return titleBox;
    }

    private VBox createMenuSection() {
        VBox menuBox = new VBox(15);
        menuBox.setPadding(new Insets(20));
        menuBox.setAlignment(Pos.CENTER);
        
        // Create menu buttons
        Button basicAppBtn = createMenuButton(
            "1. Aplikasi Sederhana", 
            "Hello World tanpa FXML",
            () -> new BasicJavaFXApp().start(new Stage())
        );
        
        Button layoutBtn = createMenuButton(
            "2. Layout Examples", 
            "VBox, HBox, BorderPane, GridPane",
            () -> new LayoutExampleApp().start(new Stage())
        );
        
        Button controlsBtn = createMenuButton(
            "3. Controls & Events", 
            "TextField, Button, ComboBox, ListView",
            () -> new ControlsAndEventsApp().start(new Stage())
        );
        
        Button styledBtn = createMenuButton(
            "4. Styled Application", 
            "Aplikasi dengan CSS styling",
            () -> new StyledApp().start(new Stage())
        );
        
        Button calculatorBtn = createMenuButton(
            "5. Calculator (FXML)",
            "Kalkulator dengan FXML dan CSS",
            () -> {
                try {
                    new CalculatorApp().start(new Stage());
                } catch (Exception ex) {
                    showErrorAlert("Error", "Gagal membuka Calculator: " + ex.getMessage());
                }
            }
        );

        Button studentFormBtn = createMenuButton(
            "6. Student Form (FXML)",
            "Form kompleks dengan validasi",
            () -> {
                try {
                    new StudentFormApp().start(new Stage());
                } catch (Exception ex) {
                    showErrorAlert("Error", "Gagal membuka Student Form: " + ex.getMessage());
                }
            }
        );
        
        Button exitBtn = createMenuButton(
            "❌ Keluar", 
            "Tutup aplikasi",
            () -> System.exit(0)
        );
        exitBtn.getStyleClass().clear();
        exitBtn.getStyleClass().add("danger-button");
        
        menuBox.getChildren().addAll(
            basicAppBtn, layoutBtn, controlsBtn, styledBtn, 
            calculatorBtn, studentFormBtn, 
            new Separator(),
            exitBtn
        );
        
        return menuBox;
    }

    private VBox createFooterSection() {
        VBox footerBox = new VBox(5);
        footerBox.setPadding(new Insets(20));
        footerBox.setAlignment(Pos.CENTER);
        
        Label footerText = new Label("💡 Tips: Setiap aplikasi akan terbuka di window baru");
        footerText.setStyle("-fx-font-size: 12px; -fx-text-fill: #95a5a6; -fx-font-style: italic;");
        
        Label versionText = new Label("JavaFX 21 • Java 21 • Maven 3.9");
        versionText.setStyle("-fx-font-size: 11px; -fx-text-fill: #bdc3c7;");
        
        footerBox.getChildren().addAll(footerText, versionText);
        return footerBox;
    }

    private Button createMenuButton(String title, String description, Runnable action) {
        Button button = new Button();
        button.getStyleClass().add("custom-button");
        button.setPrefWidth(400);
        button.setPrefHeight(60);
        
        // Create button content
        VBox content = new VBox(2);
        content.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label descLabel = new Label(description);
        descLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #ecf0f1;");
        
        content.getChildren().addAll(titleLabel, descLabel);
        button.setGraphic(content);
        
        button.setOnAction(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                showErrorAlert("Error", "Gagal membuka aplikasi: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        return button;
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
