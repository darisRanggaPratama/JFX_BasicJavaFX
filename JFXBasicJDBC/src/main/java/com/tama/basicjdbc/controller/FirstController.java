package com.tama.basicjdbc.controller;

import com.tama.basicjdbc.dao.UserDAO;
import com.tama.basicjdbc.model.Users;
import com.tama.basicjdbc.utility.JDBCConnection;
import com.tama.basicjdbc.view.UserFormDialog;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.kordamp.ikonli.javafx.FontIcon;

import javax.xml.xpath.XPath;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FirstController {
    @FXML
    public Button btnPrint;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnRefresh;
    @FXML
    private TableView<Users> tableData;
    @FXML
    private TableColumn<Users, Integer> colId;
    @FXML
    private TableColumn<Users, String> colName;
    @FXML
    private TableColumn<Users, String> colAddress;
    @FXML
    private TableColumn<Users, String> colPhone;
    @FXML
    private TableColumn<Users, Void> colAction;

    private UserDAO userDAO;
    private static final Logger LOGGER = Logger.getLogger(FirstController.class.getName());

    public void initialize() {
        try {
            userDAO = new UserDAO();

            // Configure table columns
            colId.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
            colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

            // Add action buttons to the table
            addButtonsToTable();

            // Load data
            refreshData();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing FirstController", e);
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Error Inisialisasi", "Gagal memulai aplikasi: " + e.getMessage()));
        }
    }

    private void addButtonsToTable() {
        Callback<TableColumn<Users, Void>, TableCell<Users, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Users, Void> call(final TableColumn<Users, Void> param) {
                return new TableCell<>() {
                    private final Button editButton = createIconButton("fas-pencil-alt", "Edit");
                    private final Button deleteButton = createIconButton("fas-trash-alt", "Delete");
                    private final HBox pane = new HBox(5, editButton, deleteButton);

                    {
                        editButton.setOnAction(event -> {
                            Users user = getTableView().getItems().get(getIndex());
                            showEditDialog(user);
                        });

                        deleteButton.setOnAction(event -> {
                            Users user = getTableView().getItems().get(getIndex());
                            showDeleteConfirmation(user);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : pane);
                    }
                };
            }
        };

        colAction.setCellFactory(cellFactory);
    }

    private Button createIconButton(String iconName, String tooltipText) {
        Button button = new Button();
        FontIcon icon = new FontIcon(iconName);
        icon.setIconSize(16);
        button.setGraphic(icon);
        button.setTooltip(new Tooltip(tooltipText));
        button.getStyleClass().add("icon-button");
        return button;
    }

    private void showEditDialog(Users user) {
        UserFormDialog dialog = new UserFormDialog(user, "Edit User");
        Optional<Users> result = dialog.showAndWait();
        
        result.ifPresent(updatedUser -> {
            int updateResult = userDAO.updateData(updatedUser);
            if (updateResult > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil diperbarui.");
                refreshData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui data.");
            }
        });
    }

    private void showDeleteConfirmation(Users user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Data User");
        alert.setContentText("Apakah Anda yakin ingin menghapus user: " + user.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int deleteResult = userDAO.deleteData(user);
            if (deleteResult > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil dihapus.");
                refreshData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus data.");
            }
        }
    }

    @FXML
    public void refreshData() {
        try {
            ObservableList<Users> userList = userDAO.showData();
            tableData.setItems(userList);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saat refresh data", e);
            showAlert(Alert.AlertType.ERROR,
                    "Error Refresh Data",
                    "Terjadi kesalahan saat memuat data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void showAddDialog(ActionEvent actionEvent) {
        try {
            // Membuat dialog untuk menambah data baru
            UserFormDialog dialog = new UserFormDialog(null, "Tambah User Baru");
            Optional<Users> result = dialog.showAndWait();

            result.ifPresent(newUser -> {
                if (validateUserInput(newUser)) {
                    int addResult = userDAO.addData(newUser);
                    if (addResult > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data berhasil ditambahkan.");
                        refreshData();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Gagal menambahkan data.");
                    }
                }
            });
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saat menambahkan data", e);
            showAlert(Alert.AlertType.ERROR,
                    "Error Dialog",
                    "Terjadi kesalahan saat membuka dialog: " + e.getMessage());
        }
    }

    private boolean validateUserInput(Users user) {
        StringBuilder errorMessage = new StringBuilder();

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            errorMessage.append("- Nama tidak boleh kosong\n");
        }

        if (user.getPhone() != null && !user.getPhone().matches("\\d+")) {
            errorMessage.append("- Nomor telepon hanya boleh berisi angka\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validasi Data",
                    "Silakan perbaiki kesalahan berikut:\n" + errorMessage);
            return false;
        }

        return true;
    }

    public void printData(ActionEvent actionEvent)  {


        try {
            // Create parameters map with proper generic type
            Map<String, Object> parameters = new HashMap<>();

            // Get to report a path
            String reportPath = getClass().getResource("/com/tama/basicjdbc/report/Laporan.jasper").getPath();

            // Fill and display a report
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    reportPath,
                    parameters,
                    JDBCConnection.getConnection()
            );

            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle("Laporan Data User");
            viewer.setVisible(true);

        } catch (JRException | SQLException e) {
            LOGGER.log(Level.SEVERE, "Error printing report", e);
            showAlert(
                    Alert.AlertType.ERROR,
                    "Error Cetak Laporan",
                    "Terjadi kesalahan saat mencetak laporan: " + e.getMessage()
            );
        }

    }
}