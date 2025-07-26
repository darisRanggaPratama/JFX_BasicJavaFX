package com.tama.customer.controller;

import com.tama.customer.util.DBConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

public class DatabaseConnectionController {
    @FXML private TextField serverField;
    @FXML private TextField portField;
    @FXML private TextField databaseField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private Stage dialogStage;
    private boolean connectionSuccessful = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConnectionSuccessful() {
        return connectionSuccessful;
    }

    @FXML
    private void handleTestConnection() {
        try {
            setupConnection();
            boolean isValid = DBConnectionManager.getInstance().testConnection();
            if (isValid) {
                statusLabel.setText("Connection test successful!");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("Connection test failed!");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleConnect() {
        try {
            setupConnection();
            if (DBConnectionManager.getInstance().testConnection()) {
                connectionSuccessful = true;
                dialogStage.close();
            } else {
                statusLabel.setText("Could not establish connection!");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void setupConnection() {
        String server = serverField.getText();
        String port = portField.getText();
        String database = databaseField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        System.out.println("Attempting to connect to database:");
        System.out.println("Server: " + server);
        System.out.println("Port: " + port);
        System.out.println("Database: " + database);
        System.out.println("Username: " + username);

        DBConnectionManager.getInstance().setupConnection(
            server, database, port, username, password
        );
    }
}
