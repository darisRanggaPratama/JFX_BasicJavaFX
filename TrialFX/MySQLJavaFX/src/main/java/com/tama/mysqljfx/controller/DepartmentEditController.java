package com.tama.mysqljfx.controller;

import com.tama.mysqljfx.dao.DepartmentDAOImpl;
import com.tama.mysqljfx.dao.FacultyDAOImpl;
import com.tama.mysqljfx.entity.Department;
import com.tama.mysqljfx.entity.Faculty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DepartmentEditController {
    @FXML
    private TextField departmentIdField;
    @FXML
    private TextField departmentNameField;
    @FXML
    private ComboBox<Faculty> facultyComboBox;

    private Department department;
    private DepartmentDAOImpl departmentDAO;
    private FacultyDAOImpl facultyDAO;
    private MainController mainController;

    @FXML
    public void initialize() {
        departmentDAO = new DepartmentDAOImpl();
        facultyDAO = new FacultyDAOImpl();
        loadFaculties();
    }

    private void loadFaculties() {
        try {
            facultyComboBox.getItems().setAll(facultyDAO.fetchAll());
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to load faculties: " + e.getMessage());
        }
    }

    public void setDepartment(Department department) {
        this.department = department;
        departmentIdField.setText(String.valueOf(department.getId()));
        departmentNameField.setText(department.getName());
        facultyComboBox.setValue(department.getFaculty());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleUpdate() {
        if (departmentNameField.getText().trim().isEmpty() || facultyComboBox.getValue() == null) {
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        department.setName(departmentNameField.getText().trim());
        department.setFaculty(facultyComboBox.getValue());

        try {
            int result = departmentDAO.updateData(department);
            if (result == 1) {
                mainController.refreshDepartmentData();
                closeWindow();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to update department: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            int result = departmentDAO.deleteData(department);
            if (result == 1) {
                mainController.refreshDepartmentData();
                closeWindow();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to delete department: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) departmentIdField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
