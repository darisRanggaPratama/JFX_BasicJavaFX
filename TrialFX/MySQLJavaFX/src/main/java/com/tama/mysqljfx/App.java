package com.tama.mysqljfx;

import com.tama.mysqljfx.util.MySQLConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Check database connection first
        try {
            Connection connection = MySQLConnection.createConnection();
            if (MySQLConnection.isConnected(connection)) {
                connection.close(); // Close the test connection
                // If connection successful, proceed with application
                Parent root = FXMLLoader.load(Objects.requireNonNull(App.class.getResource("/com/tama/mysqljfx/main-view.fxml")));
                Scene scene = new Scene(root);
                stage.setTitle("Hello!");
                stage.setScene(scene);
                stage.show();
            }
        } catch (SQLException e) {
            // Show error dialog
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Database Connection Failed");
            alert.setContentText("Error: " + e.getMessage() + "\nApplication will now close.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    Platform.exit();
                }
            });
        }
    }

    public static void main(String[] args) {
        launch();
    }
}