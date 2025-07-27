package com.tama.customer.view;

import com.tama.customer.config.DatabaseConfig;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DatabaseConnectionView extends Dialog<Boolean> {
    private final TextField serverField = new TextField("localhost");
    private final TextField databaseField = new TextField();
    private final TextField portField = new TextField("3306");
    private final TextField userField = new TextField("root");
    private final PasswordField passwordField = new PasswordField();

    public DatabaseConnectionView(Stage owner) {
        setTitle("Database Connection");
        setHeaderText("Please enter database connection details");

        // Create the content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Server:"), 0, 0);
        grid.add(serverField, 1, 0);
        grid.add(new Label("Database:"), 0, 1);
        grid.add(databaseField, 1, 1);
        grid.add(new Label("Port:"), 0, 2);
        grid.add(portField, 1, 2);
        grid.add(new Label("Username:"), 0, 3);
        grid.add(userField, 1, 3);
        grid.add(new Label("Password:"), 0, 4);
        grid.add(passwordField, 1, 4);

        getDialogPane().setContent(grid);

        ButtonType connectButtonType = new ButtonType("Connect", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(connectButtonType, ButtonType.CANCEL);

        // Set the dialog result converter
        setResultConverter(dialogButton -> {
            if (dialogButton == connectButtonType) {
                try {
                    DatabaseConfig config = DatabaseConfig.getInstance();
                    config.configure(
                        serverField.getText(),
                        databaseField.getText(),
                        Integer.parseInt(portField.getText()),
                        userField.getText(),
                        passwordField.getText()
                    );
                    return config.testConnection();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Connection Error");
                    alert.setContentText("Failed to connect to database: " + e.getMessage());
                    alert.showAndWait();
                    return false;
                }
            }
            return false;
        });

        // Enable/disable connect button based on field values
        Button connectButton = (Button) getDialogPane().lookupButton(connectButtonType);
        connectButton.setDisable(true);

        // Add validation
        databaseField.textProperty().addListener((obs, oldVal, newVal) ->
            connectButton.setDisable(newVal.trim().isEmpty()));

        // Optional: set owner after dialog is fully configured
        if (owner != null) {
            initOwner(owner);
        }
    }
}
