package com.tama.jfxcrud.view;

import com.tama.jfxcrud.util.DatabaseConnection;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConnectionSetupView {
    private final Stage stage;
    private boolean connectionSuccessful = false;
    
    private TextField hostField;
    private TextField portField;
    private TextField databaseField;
    private TextField userField;
    private PasswordField passwordField;
    
    public ConnectionSetupView() {
        this.stage = new Stage();
        stage.setTitle("Database Connection Setup");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        
        createUI();
    }
    
    private void createUI() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setSpacing(10);
        mainLayout.setAlignment(Pos.CENTER);
        
        Label titleLabel = new Label("Database Connection Setup");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setAlignment(Pos.CENTER);
        
        hostField = new TextField("localhost");
        portField = new TextField("3306");
        databaseField = new TextField("test");
        userField = new TextField("rangga");
        passwordField = new PasswordField();
        passwordField.setText("rangga");
        
        formGrid.add(new Label("Host:"), 0, 0);
        formGrid.add(hostField, 1, 0);
        formGrid.add(new Label("Port:"), 0, 1);
        formGrid.add(portField, 1, 1);
        formGrid.add(new Label("Database:"), 0, 2);
        formGrid.add(databaseField, 1, 2);
        formGrid.add(new Label("Username:"), 0, 3);
        formGrid.add(userField, 1, 3);
        formGrid.add(new Label("Password:"), 0, 4);
        formGrid.add(passwordField, 1, 4);
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button testButton = new Button("Test Connection");
        Button connectButton = new Button("Connect");
        Button cancelButton = new Button("Cancel");
        
        testButton.setOnAction(e -> testConnection());
        connectButton.setOnAction(e -> connect());
        cancelButton.setOnAction(e -> stage.close());
        
        buttonBox.getChildren().addAll(testButton, connectButton, cancelButton);
        
        mainLayout.getChildren().addAll(titleLabel, formGrid, buttonBox);
        
        Scene scene = new Scene(mainLayout, 400, 300);
        scene.getStylesheets().add(getClass().getResource("/com/tama/jfxcrud/styles.css").toExternalForm());
        stage.setScene(scene);
    }
    
    private void testConnection() {
        String host = hostField.getText();
        String port = portField.getText();
        String database = databaseField.getText();
        String user = userField.getText();
        String password = passwordField.getText();
        
        boolean success = DatabaseConnection.testConnection(host, port, database, user, password);
        
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Connection Test");
        alert.setHeaderText(success ? "Connection Successful" : "Connection Failed");
        alert.setContentText(success ? "Successfully connected to the database." : "Failed to connect to the database. Please check your settings.");
        alert.showAndWait();
    }
    
    private void connect() {
        String host = hostField.getText();
        String port = portField.getText();
        String database = databaseField.getText();
        String user = userField.getText();
        String password = passwordField.getText();

        boolean success = DatabaseConnection.testConnection(host, port, database, user, password);

        if (success) {
            DatabaseConnection.setConnectionParams(host, port, database, user, password);
            // Establish actual connection
            boolean connected = DatabaseConnection.establishConnection();
            if (connected) {
                connectionSuccessful = true;
                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText("Connection Failed");
                alert.setContentText("Failed to establish connection to the database.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection Error");
            alert.setHeaderText("Connection Failed");
            alert.setContentText("Failed to connect to the database. Please check your settings.");
            alert.showAndWait();
        }
    }
    
    public boolean showAndWait() {
        stage.showAndWait();
        return connectionSuccessful;
    }
}