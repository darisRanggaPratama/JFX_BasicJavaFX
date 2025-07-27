package com.tama.customer.view;

import com.tama.customer.model.Customer;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerDialog extends Dialog<Customer> {
    private final TextField nikField = new TextField();
    private final TextField nameField = new TextField();
    private final DatePicker bornField = new DatePicker();
    private final CheckBox activeField = new CheckBox();
    private final TextField salaryField = new TextField();
    private final Label errorLabel = new Label();

    public CustomerDialog(Customer customer) {
        setTitle(customer == null ? "Add New Customer" : "Edit Customer");
        setHeaderText(null);

        // Configure the dialog
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        // Add form fields
        grid.add(new Label("NIK:*"), 0, 0);
        grid.add(nikField, 1, 0);
        grid.add(new Label("Name:*"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Birth Date:*"), 0, 2);
        grid.add(bornField, 1, 2);
        grid.add(new Label("Active:"), 0, 3);
        grid.add(activeField, 1, 3);
        grid.add(new Label("Salary:*"), 0, 4);
        grid.add(salaryField, 1, 4);

        // Add error label
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 0, 5, 2, 1);

        // Set field defaults
        activeField.setSelected(true);
        bornField.setValue(LocalDate.now());

        // Set field prompts
        nikField.setPromptText("Enter NIK");
        nameField.setPromptText("Enter Name");
        salaryField.setPromptText("Enter Salary");

        // Fill fields if editing
        if (customer != null) {
            nikField.setText(customer.getNik());
            nameField.setText(customer.getName());
            bornField.setValue(customer.getBorn());
            activeField.setSelected(customer.isActive());
            salaryField.setText(String.valueOf(customer.getSalary()));
        }

        getDialogPane().setContent(grid);

        // Request focus on appropriate field
        Platform.runLater(() -> nikField.requestFocus());

        // Add validation
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);

        // Validation listeners
        nikField.textProperty().addListener((obs, oldVal, newVal) -> validateInput(saveButton));
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateInput(saveButton));
        bornField.valueProperty().addListener((obs, oldVal, newVal) -> validateInput(saveButton));
        salaryField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*")) {
                salaryField.setText(newVal.replaceAll("[^\\d]", ""));
            }
            validateInput(saveButton);
        });

        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Customer result = customer == null ? new Customer() : customer;
                result.setNik(nikField.getText());
                result.setName(nameField.getText());
                result.setBorn(bornField.getValue());
                result.setActive(activeField.isSelected());
                result.setSalary(new BigDecimal(salaryField.getText()));
                return result;
            }
            return null;
        });
    }

    private void validateInput(Button saveButton) {
        StringBuilder error = new StringBuilder();
        boolean valid = true;

        // NIK validation
        if (nikField.getText().trim().isEmpty()) {
            error.append("NIK is required\n");
            valid = false;
        } else if (nikField.getText().length() < 5) {
            error.append("NIK must be at least 5 characters\n");
            valid = false;
        }

        // Name validation
        if (nameField.getText().trim().isEmpty()) {
            error.append("Name is required\n");
            valid = false;
        }

        // Birth date validation
        if (bornField.getValue() == null) {
            error.append("Birth date is required\n");
            valid = false;
        } else if (bornField.getValue().isAfter(LocalDate.now())) {
            error.append("Birth date cannot be in the future\n");
            valid = false;
        }

        // Salary validation
        try {
            if (salaryField.getText().trim().isEmpty()) {
                error.append("Salary is required\n");
                valid = false;
            } else {
                int salary = Integer.parseInt(salaryField.getText());
                if (salary < 0) {
                    error.append("Salary must be positive\n");
                    valid = false;
                }
            }
        } catch (NumberFormatException e) {
            error.append("Salary must be a valid number\n");
            valid = false;
        }

        errorLabel.setText(error.toString());
        saveButton.setDisable(!valid);
    }
}
