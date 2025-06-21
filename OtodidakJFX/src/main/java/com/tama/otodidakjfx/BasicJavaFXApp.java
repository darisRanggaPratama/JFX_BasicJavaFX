package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Contoh 1: Aplikasi JavaFX Sederhana tanpa FXML
 * 
 * Konsep yang dipelajari:
 * - Application class
 * - Stage (window)
 * - Scene (content)
 * - Layout (VBox)
 * - Controls (Label, Button)
 */
public class BasicJavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Membuat controls
        Label label = new Label("Selamat datang di JavaFX 21!");
        Button button = new Button("Klik Saya!");
        
        // 2. Event handling sederhana
        button.setOnAction(e -> {
            label.setText("Button telah diklik!");
        });
        
        // 3. Layout container
        VBox root = new VBox(10); // spacing 10 pixels
        root.getChildren().addAll(label, button);
        
        // 4. Scene
        Scene scene = new Scene(root, 300, 200);
        
        // 5. Stage configuration
        primaryStage.setTitle("Belajar JavaFX - Aplikasi Sederhana");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
