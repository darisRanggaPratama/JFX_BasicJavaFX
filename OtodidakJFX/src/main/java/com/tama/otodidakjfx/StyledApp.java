package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Contoh 4: Aplikasi dengan CSS Styling
 * 
 * Konsep yang dipelajari:
 * - Menggunakan external CSS file
 * - CSS classes dan styling
 * - Responsive design
 * - Modern UI design
 */
public class StyledApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // Title
        Label title = new Label("Aplikasi JavaFX dengan CSS Styling");
        title.getStyleClass().add("title");

        // Create main content
        VBox mainContent = createMainContent();
        mainContent.getStyleClass().add("card");

        root.getChildren().addAll(title, mainContent);

        // Create scene and apply CSS
        Scene scene = new Scene(root, 600, 500);
        
        // Load CSS file
        String cssFile = getClass().getResource("styles.css").toExternalForm();
        scene.getStylesheets().add(cssFile);

        primaryStage.setTitle("Belajar JavaFX - CSS Styling");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainContent() {
        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);

        // Subtitle
        Label subtitle = new Label("Form Login Bergaya");
        subtitle.getStyleClass().add("subtitle");

        // Login form
        GridPane loginForm = createLoginForm();

        // Button container
        HBox buttonContainer = createButtonContainer();

        // Status area
        TextArea statusArea = new TextArea();
        statusArea.setPrefRowCount(4);
        statusArea.setEditable(false);
        statusArea.setPromptText("Status login akan ditampilkan di sini...");

        content.getChildren().addAll(subtitle, loginForm, buttonContainer, statusArea);
        return content;
    }

    private GridPane createLoginForm() {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        // Username
        Label userLabel = new Label("Username:");
        userLabel.getStyleClass().add("form-label");
        TextField userField = new TextField();
        userField.getStyleClass().add("custom-text-field");
        userField.setPromptText("Masukkan username");

        // Password
        Label passLabel = new Label("Password:");
        passLabel.getStyleClass().add("form-label");
        PasswordField passField = new PasswordField();
        passField.getStyleClass().add("custom-text-field");
        passField.setPromptText("Masukkan password");

        // Remember me
        CheckBox rememberBox = new CheckBox("Ingat saya");

        // Add to grid
        grid.add(userLabel, 0, 0);
        grid.add(userField, 1, 0);
        grid.add(passLabel, 0, 1);
        grid.add(passField, 1, 1);
        grid.add(rememberBox, 1, 2);

        return grid;
    }

    private HBox createButtonContainer() {
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        // Login button
        Button loginBtn = new Button("Login");
        loginBtn.getStyleClass().add("custom-button");
        loginBtn.setPrefWidth(100);

        // Register button
        Button registerBtn = new Button("Register");
        registerBtn.getStyleClass().add("success-button");
        registerBtn.setPrefWidth(100);

        // Cancel button
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("danger-button");
        cancelBtn.setPrefWidth(100);

        // Add event handlers
        loginBtn.setOnAction(e -> handleLogin());
        registerBtn.setOnAction(e -> handleRegister());
        cancelBtn.setOnAction(e -> handleCancel());

        buttonBox.getChildren().addAll(loginBtn, registerBtn, cancelBtn);
        return buttonBox;
    }

    private void handleLogin() {
        showAlert("Login", "Fitur login akan diimplementasikan!", Alert.AlertType.INFORMATION);
    }

    private void handleRegister() {
        showAlert("Register", "Fitur register akan diimplementasikan!", Alert.AlertType.INFORMATION);
    }

    private void handleCancel() {
        showAlert("Cancel", "Operasi dibatalkan!", Alert.AlertType.WARNING);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
