package com.tama.crudxmljavafx.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

/**
 * Utility class for showing alerts and dialogs
 */
public class AlertUtil {

    /**
     * Show success message
     * @param title Dialog title
     * @param message Success message
     */
    public static void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show error message
     * @param title Dialog title
     * @param message Error message
     */
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show warning message
     * @param title Dialog title
     * @param message Warning message
     */
    public static void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Show confirmation dialog
     * @param title Dialog title
     * @param message Confirmation message
     * @return true if user clicks OK, false otherwise
     */
    public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Show input dialog
     * @param title Dialog title
     * @param message Input message
     * @param defaultValue Default input value
     * @return User input or null if cancelled
     */
    public static String showInputDialog(String title, String message, String defaultValue) {
        TextInputDialog dialog = new TextInputDialog(defaultValue);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    /**
     * Show file operation result
     * @param operation Operation name (Upload/Download)
     * @param successCount Number of successful operations
     * @param failCount Number of failed operations
     */
    public static void showFileOperationResult(String operation, int successCount, int failCount) {
        String message = String.format("%s completed!\nSuccess: %d records\nFailed: %d records", 
                                     operation, successCount, failCount);
        
        if (failCount > 0) {
            showWarning(operation + " Result", message);
        } else {
            showSuccess(operation + " Result", message);
        }
    }
}
