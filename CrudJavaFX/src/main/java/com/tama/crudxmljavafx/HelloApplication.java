package com.tama.crudxmljavafx;

import com.tama.crudxmljavafx.util.AlertUtil;
import com.tama.crudxmljavafx.util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main JavaFX Application for Customer Management System
 */
public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            // Test database connection
            if (!DatabaseConnection.testConnection()) {
                AlertUtil.showError("Database Connection Error",
                    "Failed to connect to database. Please check your MySQL server and database configuration.");
                return;
            }

            // Load main view
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

            // Add CSS styling
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

            stage.setTitle("Customer Management System");
            stage.setScene(scene);
            stage.setMinWidth(1000);
            stage.setMinHeight(700);
            stage.show();

        } catch (Exception e) {
            AlertUtil.showError("Application Error",
                "Failed to start application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        // Close database connection when application stops
        DatabaseConnection.closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch();
    }
}