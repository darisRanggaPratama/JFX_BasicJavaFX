package com.tama.otodidakjfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller untuk Calculator FXML
 * 
 * Konsep yang dipelajari:
 * - @FXML annotation untuk binding dengan FXML
 * - Initializable interface
 * - Event handling dari FXML
 * - Calculator logic implementation
 * - ObservableList untuk history
 */
public class CalculatorController implements Initializable {

    @FXML private TextField displayField;
    @FXML private Label operationLabel;
    @FXML private ListView<String> historyListView;
    
    // Calculator state
    private double firstOperand = 0;
    private String operator = "";
    private boolean waitingForOperand = false;
    private boolean lastInputWasEquals = false;
    
    private ObservableList<String> history;
    private DecimalFormat decimalFormat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize history list
        history = FXCollections.observableArrayList();
        historyListView.setItems(history);
        
        // Initialize decimal format
        decimalFormat = new DecimalFormat("#.##########");
        
        // Set initial display
        displayField.setText("0");
        operationLabel.setText(" ");
        
        System.out.println("Calculator Controller initialized!");
    }

    @FXML
    private void handleNumber(ActionEvent event) {
        Button button = (Button) event.getSource();
        String digit = button.getText();
        
        if (waitingForOperand || lastInputWasEquals) {
            displayField.setText(digit);
            waitingForOperand = false;
            lastInputWasEquals = false;
        } else {
            String currentText = displayField.getText();
            if (currentText.equals("0")) {
                displayField.setText(digit);
            } else {
                displayField.setText(currentText + digit);
            }
        }
    }

    @FXML
    private void handleDecimal(ActionEvent event) {
        String currentText = displayField.getText();
        
        if (waitingForOperand || lastInputWasEquals) {
            displayField.setText("0.");
            waitingForOperand = false;
            lastInputWasEquals = false;
        } else if (!currentText.contains(".")) {
            displayField.setText(currentText + ".");
        }
    }

    @FXML
    private void handleOperation(ActionEvent event) {
        Button button = (Button) event.getSource();
        String newOperator = (String) button.getUserData();
        
        double currentValue = Double.parseDouble(displayField.getText());
        
        if (!operator.isEmpty() && !waitingForOperand && !lastInputWasEquals) {
            // Perform the pending operation
            double result = performCalculation(firstOperand, currentValue, operator);
            displayField.setText(formatNumber(result));
            firstOperand = result;
        } else {
            firstOperand = currentValue;
        }
        
        operator = newOperator;
        waitingForOperand = true;
        lastInputWasEquals = false;
        
        // Update operation label
        operationLabel.setText(formatNumber(firstOperand) + " " + getOperatorSymbol(operator));
    }

    @FXML
    private void handleEquals(ActionEvent event) {
        if (!operator.isEmpty() && !waitingForOperand) {
            double secondOperand = Double.parseDouble(displayField.getText());
            double result = performCalculation(firstOperand, secondOperand, operator);
            
            // Add to history
            String calculation = formatNumber(firstOperand) + " " + getOperatorSymbol(operator) + 
                               " " + formatNumber(secondOperand) + " = " + formatNumber(result);
            addToHistory(calculation);
            
            displayField.setText(formatNumber(result));
            operationLabel.setText(" ");
            
            // Reset for next calculation
            firstOperand = result;
            operator = "";
            waitingForOperand = true;
            lastInputWasEquals = true;
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        displayField.setText("0");
        operationLabel.setText(" ");
        firstOperand = 0;
        operator = "";
        waitingForOperand = false;
        lastInputWasEquals = false;
    }

    @FXML
    private void handleBackspace(ActionEvent event) {
        String currentText = displayField.getText();
        
        if (!waitingForOperand && !lastInputWasEquals && currentText.length() > 1) {
            displayField.setText(currentText.substring(0, currentText.length() - 1));
        } else {
            displayField.setText("0");
            waitingForOperand = false;
        }
    }

    @FXML
    private void handleClearHistory(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi");
        confirmAlert.setHeaderText("Hapus Riwayat");
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus semua riwayat perhitungan?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            history.clear();
            showAlert("Sukses", "Riwayat perhitungan berhasil dihapus!", Alert.AlertType.INFORMATION);
        }
    }

    private double performCalculation(double first, double second, String op) {
        switch (op) {
            case "+":
                return first + second;
            case "-":
                return first - second;
            case "*":
                return first * second;
            case "/":
                if (second == 0) {
                    showAlert("Error", "Tidak dapat membagi dengan nol!", Alert.AlertType.ERROR);
                    return first;
                }
                return first / second;
            default:
                return second;
        }
    }

    private String getOperatorSymbol(String op) {
        switch (op) {
            case "+": return "+";
            case "-": return "-";
            case "*": return "×";
            case "/": return "÷";
            default: return "";
        }
    }

    private String formatNumber(double number) {
        // Remove unnecessary decimal places
        if (number == (long) number) {
            return String.format("%.0f", number);
        } else {
            return decimalFormat.format(number);
        }
    }

    private void addToHistory(String calculation) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        String historyEntry = "[" + timestamp + "] " + calculation;
        
        history.add(0, historyEntry); // Add to top of list
        
        // Limit history to 50 entries
        if (history.size() > 50) {
            history.remove(history.size() - 1);
        }
        
        // Auto-scroll to top
        historyListView.scrollTo(0);
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
