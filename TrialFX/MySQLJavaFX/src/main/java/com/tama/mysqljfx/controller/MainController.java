package com.tama.mysqljfx.controller;

import com.tama.mysqljfx.dao.DepartmentDAOImpl;
import com.tama.mysqljfx.dao.FacultyDAOImpl;
import com.tama.mysqljfx.entity.Department;
import com.tama.mysqljfx.entity.Faculty;
import com.tama.mysqljfx.util.MySQLConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TextField txtFaculty;
    @FXML
    private TableView<Faculty> tableFaculty;
    @FXML
    private TableColumn<Faculty, Integer> facColID;
    @FXML
    private TableColumn<Faculty, String> facColName;
    @FXML
    private TextField txtDepartment;
    @FXML
    private ComboBox<Faculty> cboxFaculty;
    @FXML
    private TableView<Department> tableDepartment;
    @FXML
    private TableColumn<Department, Integer> depColID;
    @FXML
    private TableColumn<Department, String> depColName;
    @FXML
    private TableColumn<Department, Faculty> depColFaculty;
    private ObservableList<Faculty> faculties;
    private ObservableList<Department> departments;
    private FacultyDAOImpl facultyDAO;
    private DepartmentDAOImpl departmentDAO;

    @FXML
    private void saveFacultyAction(ActionEvent actionEvent) {
        // Check text field is not empty. If empty show alert
        if (txtFaculty.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill the field: Faculty Name");
            alert.showAndWait();
        } else {
            Faculty faculty = new Faculty();
            faculty.setName(txtFaculty.getText().trim());
            try {
                int result = facultyDAO.addData(faculty);
                if (result == 1) {
                    // Refresh the list by clearing and adding all data from database
                    faculties.setAll(facultyDAO.fetchAll());
                    txtFaculty.clear();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    @FXML
    private void saveDepartmentAction(ActionEvent actionEvent) {
        if (txtDepartment.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill the field: Department Name");
            alert.showAndWait();
        } else {
            Department department = new Department();
            department.setName(txtDepartment.getText().trim());
            department.setFaculty(cboxFaculty.getValue());
            try {
                int result = departmentDAO.addData(department);
                if (result == 1) {
                    // Refresh the list by clearing and adding all data from database
                    departments.setAll(departmentDAO.fetchAll());
                    txtDepartment.clear();
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        facultyDAO = new FacultyDAOImpl();
        departmentDAO = new DepartmentDAOImpl();
        faculties = FXCollections.observableArrayList();
        departments = FXCollections.observableArrayList();

        initializeTables();
        loadData();
        setupTableClickHandlers();
    }

    private void initializeTables() {
        // Faculty table initialization
        facColID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        facColName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        // Department table initialization
        depColID.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        depColName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        depColFaculty.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFaculty()));
        depColFaculty.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Faculty faculty, boolean empty) {
                super.updateItem(faculty, empty);
                setText(empty || faculty == null ? "" : faculty.getName());
            }
        });

        // ComboBox setup
        cboxFaculty.setItems(faculties);
        cboxFaculty.setPromptText("Select Faculty");
    }

    private void loadData() {
        try {
            faculties.setAll(facultyDAO.fetchAll());
            departments.setAll(departmentDAO.fetchAll());
            tableFaculty.setItems(faculties);
            tableDepartment.setItems(departments);
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Failed to load data: " + e.getMessage());
        }
    }

    private void setupTableClickHandlers() {
        tableFaculty.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Faculty selectedFaculty = tableFaculty.getSelectionModel().getSelectedItem();
                if (selectedFaculty != null) {
                    openFacultyEditDialog(selectedFaculty);
                }
            }
        });

        tableDepartment.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Department selectedDepartment = tableDepartment.getSelectionModel().getSelectedItem();
                if (selectedDepartment != null) {
                    openDepartmentEditDialog(selectedDepartment);
                }
            }
        });
    }

    private void openFacultyEditDialog(Faculty faculty) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/mysqljfx/faculty-edit-modal.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Edit Faculty");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            FacultyEditController controller = loader.getController();
            controller.setFaculty(faculty);
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            showErrorAlert("Failed to open faculty edit window: " + e.getMessage());
        }
    }

    private void openDepartmentEditDialog(Department department) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/mysqljfx/department-edit-modal.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Edit Department");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);

            DepartmentEditController controller = loader.getController();
            controller.setDepartment(department);
            controller.setMainController(this);

            stage.showAndWait();
        } catch (IOException e) {
            showErrorAlert("Failed to open department edit window: " + e.getMessage());
        }
    }

    public void refreshFacultyData() {
        try {
            faculties.setAll(facultyDAO.fetchAll());
            departments.setAll(departmentDAO.fetchAll());  // Refresh departments too as they may be affected
            cboxFaculty.setItems(faculties);
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Failed to refresh faculty data: " + e.getMessage());
        }
    }

    public void refreshDepartmentData() {
        try {
            departments.setAll(departmentDAO.fetchAll());
        } catch (SQLException | ClassNotFoundException e) {
            showErrorAlert("Failed to refresh department data: " + e.getMessage());
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void reportAction(ActionEvent actionEvent) {
        try {
            // Get report template path
            String reportPath = getClass().getResource("/com/tama/mysqljfx/report/Reports.jrxml").getPath();

            // Compile report
            JasperReport jasperReport = JasperCompileManager.compileReport(reportPath);

            // Get database connection
            Connection conn = MySQLConnection.createConnection();

            // Fill report with data
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, conn);

            // View the report
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Department Report");
            viewer.setVisible(true);

        } catch (Exception e) {
            showErrorAlert("Error generating report: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
