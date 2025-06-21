package com.tama.crudxmljavafx.controller;

import com.tama.crudxmljavafx.model.Customer;
import com.tama.crudxmljavafx.service.CustomerService;
import com.tama.crudxmljavafx.util.AlertUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller for the customer management application
 */
public class MainController implements Initializable {

    @FXML private TextField searchField;
    @FXML private Button searchButton;
    @FXML private Button clearSearchButton;
    @FXML private ComboBox<Integer> pageSizeComboBox;
    
    @FXML private Button addButton;
    @FXML private Button refreshButton;
    @FXML private Button uploadCSVButton;
    @FXML private Button downloadCSVButton;
    @FXML private Button uploadExcelButton;
    @FXML private Button downloadExcelButton;
    
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> idColumn;
    @FXML private TableColumn<Customer, String> nikColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> bornColumn;
    @FXML private TableColumn<Customer, String> activeColumn;
    @FXML private TableColumn<Customer, String> salaryColumn;
    
    @FXML private Button firstPageButton;
    @FXML private Button prevPageButton;
    @FXML private Button nextPageButton;
    @FXML private Button lastPageButton;
    @FXML private Label pageInfoLabel;
    @FXML private Label recordCountLabel;
    @FXML private Label statusLabel;

    private CustomerService customerService;
    private ObservableList<Customer> customerData;
    private int currentPage = 0;
    private int pageSize = 10;
    private String currentSearchTerm = "";
    private int totalPages = 0;
    private int totalRecords = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customerService = new CustomerService();
        customerData = FXCollections.observableArrayList();
        
        initializeComponents();
        setupTableColumns();
        loadCustomers();
    }

    private void initializeComponents() {
        // Initialize page size combo box
        pageSizeComboBox.setItems(FXCollections.observableArrayList(1, 5, 10, 25, 50, 100));
        pageSizeComboBox.setValue(pageSize);
        
        // Set table data
        customerTable.setItems(customerData);
        
        // Setup search field
        searchField.setOnAction(e -> handleSearch());
        
        updateStatusLabel("Ready");
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getIdx() != null ? 
                cellData.getValue().getIdx().toString() : ""));
        
        nikColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNik()));
        
        nameColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getName()));
        
        bornColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getBorn() != null ? 
                cellData.getValue().getBorn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : ""));
        
        activeColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getActiveStatus()));
        
        salaryColumn.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getFormattedSalary()));
    }

    private void loadCustomers() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Platform.runLater(() -> updateStatusLabel("Loading customers..."));
                
                List<Customer> customers = customerService.getCustomers(currentPage, pageSize, currentSearchTerm);
                totalRecords = customerService.getTotalCount(currentSearchTerm);
                totalPages = customerService.getTotalPages(pageSize, currentSearchTerm);
                
                Platform.runLater(() -> {
                    customerData.clear();
                    customerData.addAll(customers);
                    updatePaginationControls();
                    updateStatusLabel("Ready");
                });
                
                return null;
            }
        };
        
        new Thread(task).start();
    }

    private void updatePaginationControls() {
        // Update page info
        pageInfoLabel.setText(String.format("Page %d of %d", currentPage + 1, Math.max(1, totalPages)));
        recordCountLabel.setText(String.format("Total Records: %d", totalRecords));
        
        // Update button states
        firstPageButton.setDisable(currentPage == 0);
        prevPageButton.setDisable(currentPage == 0);
        nextPageButton.setDisable(currentPage >= totalPages - 1);
        lastPageButton.setDisable(currentPage >= totalPages - 1);
    }

    private void updateStatusLabel(String status) {
        statusLabel.setText(status);
    }

    @FXML
    private void handleSearch() {
        currentSearchTerm = searchField.getText().trim();
        currentPage = 0;
        loadCustomers();
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        currentSearchTerm = "";
        currentPage = 0;
        loadCustomers();
    }

    @FXML
    private void handlePageSizeChange() {
        pageSize = pageSizeComboBox.getValue();
        currentPage = 0;
        loadCustomers();
    }

    @FXML
    private void handleAddCustomer() {
        openCustomerForm(null);
    }

    @FXML
    private void handleRefresh() {
        loadCustomers();
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                openCustomerForm(selectedCustomer);
            }
        }
    }

    @FXML
    private void handleFirstPage() {
        currentPage = 0;
        loadCustomers();
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            loadCustomers();
        }
    }

    @FXML
    private void handleNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadCustomers();
        }
    }

    @FXML
    private void handleLastPage() {
        currentPage = Math.max(0, totalPages - 1);
        loadCustomers();
    }

    @FXML
    private void handleUploadCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File to Upload");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        
        File file = fileChooser.showOpenDialog(uploadCSVButton.getScene().getWindow());
        if (file != null) {
            if (AlertUtil.showConfirmation("Upload CSV", 
                "Are you sure you want to upload data from this CSV file?\nThis will add new records to the database.")) {
                
                uploadFile(file.getAbsolutePath(), "CSV");
            }
        }
    }

    @FXML
    private void handleDownloadCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("customers.csv");
        
        File file = fileChooser.showSaveDialog(downloadCSVButton.getScene().getWindow());
        if (file != null) {
            downloadFile(file.getAbsolutePath(), "CSV");
        }
    }

    @FXML
    private void handleUploadExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File to Upload");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        
        File file = fileChooser.showOpenDialog(uploadExcelButton.getScene().getWindow());
        if (file != null) {
            if (AlertUtil.showConfirmation("Upload Excel", 
                "Are you sure you want to upload data from this Excel file?\nThis will add new records to the database.")) {
                
                uploadFile(file.getAbsolutePath(), "Excel");
            }
        }
    }

    @FXML
    private void handleDownloadExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("customers.xlsx");
        
        File file = fileChooser.showSaveDialog(downloadExcelButton.getScene().getWindow());
        if (file != null) {
            downloadFile(file.getAbsolutePath(), "Excel");
        }
    }

    private void uploadFile(String filePath, String fileType) {
        Task<int[]> task = new Task<int[]>() {
            @Override
            protected int[] call() throws Exception {
                Platform.runLater(() -> updateStatusLabel("Uploading " + fileType + " file..."));
                
                if ("CSV".equals(fileType)) {
                    return customerService.importFromCSV(filePath);
                } else {
                    return customerService.importFromExcel(filePath);
                }
            }
            
            @Override
            protected void succeeded() {
                int[] result = getValue();
                Platform.runLater(() -> {
                    AlertUtil.showFileOperationResult("Upload " + fileType, result[0], result[1]);
                    loadCustomers();
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    AlertUtil.showError("Upload Error", "Failed to upload " + fileType + " file: " + getException().getMessage());
                    updateStatusLabel("Ready");
                });
            }
        };
        
        new Thread(task).start();
    }

    private void downloadFile(String filePath, String fileType) {
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                Platform.runLater(() -> updateStatusLabel("Downloading " + fileType + " file..."));
                
                if ("CSV".equals(fileType)) {
                    return customerService.exportToCSV(filePath);
                } else {
                    return customerService.exportToExcel(filePath);
                }
            }
            
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    if (getValue()) {
                        AlertUtil.showSuccess("Download " + fileType, 
                            fileType + " file has been saved successfully to:\n" + filePath);
                    } else {
                        AlertUtil.showError("Download Error", "Failed to save " + fileType + " file.");
                    }
                    updateStatusLabel("Ready");
                });
            }
            
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    AlertUtil.showError("Download Error", "Failed to download " + fileType + " file: " + getException().getMessage());
                    updateStatusLabel("Ready");
                });
            }
        };
        
        new Thread(task).start();
    }

    private void openCustomerForm(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/crudxmljavafx/customer-form.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/com/tama/crudxmljavafx/styles.css").toExternalForm());
            
            CustomerFormController controller = loader.getController();
            controller.setMainController(this);
            
            Stage stage = new Stage();
            stage.setTitle(customer == null ? "Add New Customer" : "Edit Customer");
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(addButton.getScene().getWindow());
            stage.setResizable(false);
            
            if (customer != null) {
                controller.setCustomer(customer);
            }
            
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtil.showError("Error", "Failed to open customer form: " + e.getMessage());
        }
    }

    public void refreshTable() {
        loadCustomers();
    }
}
