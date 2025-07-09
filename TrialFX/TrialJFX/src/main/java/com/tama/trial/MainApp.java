package com.tama.trial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Main application class for the JavaFX application.
 * 
 * This class demonstrates two different approaches to loading FXML files:
 * 
 * Approach 1 (Static method):
 * ```
 * Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_layout.fxml")));
 * stage.setTitle("Test Input");
 * stage.setScene(new Scene(root, 300, 275));
 * stage.show();
 * ```
 * 
 * Approach 2 (Instance method):
 * ```
 * FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main_layout.fxml"));
 * Scene scene = new Scene(fxmlLoader.load(), 320, 240);
 * stage.setTitle("Hello!");
 * stage.setScene(scene);
 * stage.show();
 * ```
 * 
 * Key Differences:
 * 1. Resource Loading:
 *    - Approach 1: Uses getClass().getResource() which is relative to the current class
 *    - Approach 2: Uses MainApp.class.getResource() which is explicitly relative to MainApp class
 * 
 * 2. FXMLLoader Usage:
 *    - Approach 1: Uses static FXMLLoader.load() method
 *    - Approach 2: Creates an explicit FXMLLoader instance first
 * 
 * 3. Null Handling:
 *    - Approach 1: Uses Objects.requireNonNull() for explicit null checking
 *    - Approach 2: No explicit null checking in the original code
 * 
 * 4. Access to Controller:
 *    - Approach 1: No direct access to the FXMLLoader after loading
 *    - Approach 2: Maintains a reference to the FXMLLoader, allowing access to the controller
 * 
 * Recommendation:
 * Approach 2 is generally recommended because:
 * - It provides access to the controller instance via fxmlLoader.getController()
 * - It's more flexible, allowing custom controller factories, resource bundles, etc.
 * - It's more explicit about which class is used as the base for resource loading
 * - It follows modern JavaFX application development practices
 * 
 * The implementation below uses Approach 2 with added null safety.
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Approach 2 (Recommended): Using explicit FXMLLoader instance with null safety
        FXMLLoader fxmlLoader = new FXMLLoader(Objects.requireNonNull(
            MainApp.class.getResource("main_layout.fxml"),
            "FXML resource not found"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // If you need to access the controller:
        // MainController controller = fxmlLoader.getController();

        // Approach 1 (Alternative): Using static load method
        // Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main_layout.fxml")));
        // stage.setTitle("Test Input");
        // stage.setScene(new Scene(root, 300, 275));
        // stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
