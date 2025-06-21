package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contoh 6: Aplikasi Kalkulator dengan FXML
 * 
 * Konsep yang dipelajari:
 * - FXMLLoader untuk memuat file FXML
 * - Pemisahan UI (FXML) dari Logic (Controller)
 * - CSS styling untuk FXML
 * - Event handling melalui FXML
 * - MVC (Model-View-Controller) pattern
 */
public class CalculatorApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Load FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(
                CalculatorApp.class.getResource("calculator-view.fxml")
            );
            
            // Create scene from FXML
            Scene scene = new Scene(fxmlLoader.load(), 400, 600);
            
            // Load CSS stylesheet
            String cssFile = getClass().getResource("calculator-styles.css").toExternalForm();
            scene.getStylesheets().add(cssFile);
            
            // Configure stage
            primaryStage.setTitle("Belajar JavaFX - Kalkulator dengan FXML");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Fixed size for calculator
            primaryStage.show();
            
            System.out.println("Calculator App started successfully!");
            
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR
            );
            alert.setTitle("Error");
            alert.setHeaderText("Gagal Memuat Aplikasi");
            alert.setContentText("Terjadi kesalahan saat memuat file FXML: " + e.getMessage());
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
