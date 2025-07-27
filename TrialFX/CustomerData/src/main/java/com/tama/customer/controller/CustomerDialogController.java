package com.tama.customer.controller;

import com.tama.customer.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

import java.time.LocalDate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class CustomerDialogController {
    @FXML private TextField nikField;
    @FXML private TextField nameField;
    @FXML private DatePicker bornField;
    @FXML private CheckBox activeField;
    @FXML private TextField salaryField;
    @FXML private Button deleteButton;

    private Stage dialogStage;
    private Customer customer;
    private boolean saveClicked = false;
    private boolean deleteClicked = false;

    @FXML
    private void initialize() {
        // Set up validation for NIK (numbers only)
        UnaryOperator<TextFormatter.Change> nikFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };
        nikField.setTextFormatter(new TextFormatter<>(nikFilter));

        // Set up validation for salary (numbers and decimal point only)
        Pattern validEditingState = Pattern.compile("-?(([1-9]\\d*)|0)?(\\.[\\d]*)?");
        UnaryOperator<TextFormatter.Change> salaryFilter = change -> {
            String newText = change.getControlNewText();
            if (validEditingState.matcher(newText).matches()) {
                return change;
            }
            return null;
        };
        salaryField.setTextFormatter(new TextFormatter<>(salaryFilter));

        // Set default date to today
        bornField.setValue(LocalDate.now());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCustomer(Customer customer, boolean isEditMode) {
        this.customer = customer;

        if (customer != null) {
            nikField.setText(customer.getNik());
            nameField.setText(customer.getName());
            bornField.setValue(customer.getBorn());
            activeField.setSelected(customer.isActive());
            salaryField.setText(String.valueOf(customer.getSalary()));

            // Disable NIK field in edit mode
            nikField.setDisable(isEditMode);
        }

        // Show delete button only in edit mode
        if (deleteButton != null) {
            deleteButton.setVisible(isEditMode);
            deleteButton.setManaged(isEditMode);
        }
    }

    public boolean isSaveClicked() {
        return saveClicked;
    }

    public boolean isDeleteClicked() {
        return deleteClicked;
    }

    @FXML
    private void handleDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Customer Record");
        alert.setContentText("Are you sure you want to delete this customer record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            customer.setNik(nikField.getText());
            customer.setName(nameField.getText());
            customer.setBorn(bornField.getValue());
            customer.setActive(activeField.isSelected());
            customer.setSalary(Double.parseDouble(salaryField.getText()));

            saveClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        StringBuilder errorMessage = new StringBuilder();

        if (nikField.getText() == null || nikField.getText().trim().isEmpty()) {
            errorMessage.append("NIK is required!\n");
        }
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage.append("Name is required!\n");
        }
        if (bornField.getValue() == null) {
            errorMessage.append("Birth date is required!\n");
        }
        if (salaryField.getText() == null || salaryField.getText().trim().isEmpty()) {
            errorMessage.append("Salary is required!\n");
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct the invalid fields");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return false;
        }
    }
}
