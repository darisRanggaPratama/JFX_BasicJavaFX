package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contoh 7: Form Management dengan FXML dan Validasi
 * 
 * Konsep yang dipelajari:
 * - Complex form dengan multiple sections
 * - Real-time validation
 * - Dependent ComboBox (Faculty -> Major)
 * - DatePicker, Spinner, ListView controls
 * - Form state management
 * - User experience best practices
 */
public class StudentFormApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Load FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(
                StudentFormApp.class.getResource("student-form.fxml")
            );
            
            // Create scene from FXML
            Scene scene = new Scene(fxmlLoader.load(), 800, 700);
            
            // Load CSS stylesheet
            String cssFile = getClass().getResource("styles.css").toExternalForm();
            scene.getStylesheets().add(cssFile);
            
            // Configure stage
            primaryStage.setTitle("Belajar JavaFX - Form Pendaftaran Mahasiswa");
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(600);
            primaryStage.setMinHeight(500);
            primaryStage.show();
            
            System.out.println("Student Form App started successfully!");
            
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
