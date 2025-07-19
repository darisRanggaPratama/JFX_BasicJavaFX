package com.tama.mysqljfx.controller;

import com.tama.mysqljfx.dao.FacultyDAOImpl;
import com.tama.mysqljfx.entity.Faculty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class FacultyEditController {
    @FXML
    private TextField facultyIdField;
    @FXML
    private TextField facultyNameField;

    private Faculty faculty;
    private FacultyDAOImpl facultyDAO;
    private MainController mainController;

    @FXML
    public void initialize() {
        facultyDAO = new FacultyDAOImpl();
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
        facultyIdField.setText(String.valueOf(faculty.getId()));
        facultyNameField.setText(faculty.getName());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void handleUpdate() {
        if (facultyNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Please fill in the faculty name.");
            return;
        }

        faculty.setName(facultyNameField.getText().trim());
        try {
            int result = facultyDAO.updateData(faculty);
            if (result == 1) {
                mainController.refreshFacultyData();
                closeWindow();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to update faculty: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        try {
            int result = facultyDAO.deleteData(faculty);
            if (result == 1) {
                mainController.refreshFacultyData();
                closeWindow();
            }
        } catch (SQLException | ClassNotFoundException e) {
            showAlert("Error", "Failed to delete faculty: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) facultyIdField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
