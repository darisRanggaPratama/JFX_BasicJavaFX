package com.tama.crudxmljavafx.controller;

import com.tama.crudxmljavafx.model.Customer;
import com.tama.crudxmljavafx.service.CustomerService;
import com.tama.crudxmljavafx.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller for the customer form dialog
 */
public class CustomerFormController implements Initializable {

    @FXML private Label formTitleLabel;
    @FXML private TextField idField;
    @FXML private TextField nikField;
    @FXML private TextField nameField;
    @FXML private DatePicker bornDatePicker;
    @FXML private CheckBox activeCheckBox;
    @FXML private TextField salaryField;
    @FXML private Label validationLabel;
    
    @FXML private Button saveButton;
    @FXML private Button updateButton;
    @FXML private Button deleteButton;
    @FXML private Button cancelButton;

    private CustomerService customerService;
    private MainController mainController;
    private Customer currentCustomer;
    private boolean isEditMode = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerService = new CustomerService();
        
        // Initialize form
        setupValidation();
        setupFormMode();
    }

    private void setupValidation() {
        // Add input validation
        nikField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 6) {
                nikField.setText(oldValue);
            }
        });
        
        salaryField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                salaryField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    private void setupFormMode() {
        // Default to add mode
        isEditMode = false;
        saveButton.setVisible(true);
        updateButton.setVisible(false);
        deleteButton.setVisible(false);
        
        // Set default values
        activeCheckBox.setSelected(true);
        bornDatePicker.setValue(LocalDate.now().minusYears(25));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setCustomer(Customer customer) {
        this.currentCustomer = customer;
        this.isEditMode = true;
        
        // Update form mode
        formTitleLabel.setText("Edit Customer");
        saveButton.setVisible(false);
        updateButton.setVisible(true);
        deleteButton.setVisible(true);
        
        // Populate form fields
        populateForm();
    }

    private void populateForm() {
        if (currentCustomer != null) {
            idField.setText(currentCustomer.getIdx() != null ? currentCustomer.getIdx().toString() : "");
            nikField.setText(currentCustomer.getNik() != null ? currentCustomer.getNik() : "");
            nameField.setText(currentCustomer.getName() != null ? currentCustomer.getName() : "");
            bornDatePicker.setValue(currentCustomer.getBorn());
            activeCheckBox.setSelected(currentCustomer.getActive() != null ? currentCustomer.getActive() : false);
            salaryField.setText(currentCustomer.getSalary() != null ? currentCustomer.getSalary().toString() : "");
        }
    }

    @FXML
    private void handleSave() {
        if (validateForm()) {
            try {
                Customer customer = createCustomerFromForm();
                int result = customerService.saveCustomer(customer);
                
                if (result > 0) {
                    AlertUtil.showSuccess("Success", "Customer has been saved successfully!");
                    closeForm();
                    if (mainController != null) {
                        mainController.refreshTable();
                    }
                } else {
                    AlertUtil.showError("Error", "Failed to save customer. Please try again.");
                }
            } catch (Exception e) {
                AlertUtil.showError("Error", "Failed to save customer: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleUpdate() {
        if (validateForm()) {
            if (AlertUtil.showConfirmation("Update Customer", 
                "Are you sure you want to update this customer?")) {
                
                try {
                    Customer customer = createCustomerFromForm();
                    customer.setIdx(currentCustomer.getIdx());
                    
                    boolean result = customerService.updateCustomer(customer);
                    
                    if (result) {
                        AlertUtil.showSuccess("Success", "Customer has been updated successfully!");
                        closeForm();
                        if (mainController != null) {
                            mainController.refreshTable();
                        }
                    } else {
                        AlertUtil.showError("Error", "Failed to update customer. Please try again.");
                    }
                } catch (Exception e) {
                    AlertUtil.showError("Error", "Failed to update customer: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleDelete() {
        if (currentCustomer != null) {
            if (AlertUtil.showConfirmation("Delete Customer", 
                "Are you sure you want to delete this customer?\nThis action cannot be undone.")) {
                
                try {
                    boolean result = customerService.deleteCustomer(currentCustomer.getIdx());
                    
                    if (result) {
                        AlertUtil.showSuccess("Success", "Customer has been deleted successfully!");
                        closeForm();
                        if (mainController != null) {
                            mainController.refreshTable();
                        }
                    } else {
                        AlertUtil.showError("Error", "Failed to delete customer. Please try again.");
                    }
                } catch (Exception e) {
                    AlertUtil.showError("Error", "Failed to delete customer: " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private void handleCancel() {
        closeForm();
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        // Validate NIK
        if (nikField.getText().trim().isEmpty()) {
            errors.append("NIK is required.\n");
        } else if (nikField.getText().trim().length() != 6) {
            errors.append("NIK must be exactly 6 characters.\n");
        }
        
        // Validate Name
        if (nameField.getText().trim().isEmpty()) {
            errors.append("Name is required.\n");
        }
        
        // Validate Salary
        if (!salaryField.getText().trim().isEmpty()) {
            try {
                int salary = Integer.parseInt(salaryField.getText().trim());
                if (salary < 0) {
                    errors.append("Salary must be a positive number.\n");
                }
            } catch (NumberFormatException e) {
                errors.append("Salary must be a valid number.\n");
            }
        }
        
        if (errors.length() > 0) {
            validationLabel.setText(errors.toString().trim());
            validationLabel.setVisible(true);
            return false;
        } else {
            validationLabel.setVisible(false);
            return true;
        }
    }

    private Customer createCustomerFromForm() {
        Customer customer = new Customer();
        
        customer.setNik(nikField.getText().trim());
        customer.setName(nameField.getText().trim());
        customer.setBorn(bornDatePicker.getValue());
        customer.setActive(activeCheckBox.isSelected());
        
        if (!salaryField.getText().trim().isEmpty()) {
            customer.setSalary(Integer.parseInt(salaryField.getText().trim()));
        } else {
            customer.setSalary(0);
        }
        
        return customer;
    }

    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
