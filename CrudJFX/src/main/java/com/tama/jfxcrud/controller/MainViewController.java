package com.tama.jfxcrud.controller;

import com.tama.jfxcrud.model.Customer;
import com.tama.jfxcrud.util.DatabaseConnection;
import com.tama.jfxcrud.util.JasperReportsUtil;
import com.tama.jfxcrud.view.ConnectionSetupView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    
    @FXML private TableView<Customer> tableView;
    @FXML private TableColumn<Customer, Integer> idxColumn;
    @FXML private TableColumn<Customer, String> nikColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, LocalDate> bornColumn;
    @FXML private TableColumn<Customer, Boolean> activeColumn;
    @FXML private TableColumn<Customer, Integer> salaryColumn;
    
    @FXML private TextField searchField;
    @FXML private ComboBox<Integer> rowsPerPageComboBox;
    @FXML private Label pageInfoLabel;
    @FXML private Button prevButton;
    @FXML private Button nextButton;
    @FXML private Label connectionStatusLabel;
    
    private final CustomerController controller;
    private Stage primaryStage;
    
    private int currentPage = 1;
    private int totalPages = 1;
    private int totalRecords = 0;
    private int rowsPerPage = 10;
    private String currentSearch = "";
    
    public MainViewController() {
        this.controller = new CustomerController();
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("DEBUG: MainViewController.initialize() called");

        try {
            setupTableColumns();
            System.out.println("DEBUG: Table columns setup completed");

            setupRowsPerPageComboBox();
            System.out.println("DEBUG: Rows per page combo box setup completed");

            setupSearchField();
            System.out.println("DEBUG: Search field setup completed");

            setupTableRowClickEvent();
            System.out.println("DEBUG: Table row click event setup completed");

            // Initial connection setup
            Platform.runLater(() -> {
                System.out.println("DEBUG: About to show connection setup");
                showConnectionSetup();
            });
        } catch (Exception e) {
            System.err.println("DEBUG: Error in initialize(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void setupTableColumns() {
        idxColumn.setCellValueFactory(new PropertyValueFactory<>("idx"));
        nikColumn.setCellValueFactory(new PropertyValueFactory<>("nik"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        bornColumn.setCellValueFactory(new PropertyValueFactory<>("born"));
        bornColumn.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(formatter.format(date));
                }
            }
        });
        
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean active, boolean empty) {
                super.updateItem(active, empty);
                if (empty || active == null) {
                    setText(null);
                } else {
                    setText(active ? "Yes" : "No");
                }
            }
        });
        
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Integer salary, boolean empty) {
                super.updateItem(salary, empty);
                if (empty || salary == null) {
                    setText(null);
                } else {
                    setText(String.format("%,d", salary));
                }
            }
        });
    }
    
    private void setupRowsPerPageComboBox() {
        System.out.println("DEBUG: Setting up rows per page combo box");

        if (rowsPerPageComboBox == null) {
            System.err.println("DEBUG: ERROR - rowsPerPageComboBox is null!");
            return;
        }

        rowsPerPageComboBox.setItems(FXCollections.observableArrayList(1, 5, 10, 25, 50, 100));
        rowsPerPageComboBox.setValue(10);

        // Set up listener via code (backup for FXML)
        rowsPerPageComboBox.setOnAction(e -> {
            Integer selectedValue = rowsPerPageComboBox.getValue();
            System.out.println("DEBUG: Rows per page changed via listener to: " + selectedValue);
            if (selectedValue != null) {
                rowsPerPage = selectedValue;
                currentPage = 1;
                loadData();
            }
        });

        System.out.println("DEBUG: Rows per page combo box setup completed with value: " + rowsPerPageComboBox.getValue());
    }
    
    private void setupSearchField() {
        System.out.println("DEBUG: Setting up search field");

        if (searchField == null) {
            System.err.println("DEBUG: ERROR - searchField is null!");
            return;
        }

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("DEBUG: Search field changed from '" + oldVal + "' to '" + newVal + "'");
            currentSearch = newVal != null ? newVal : "";
            currentPage = 1;
            loadData();
        });

        System.out.println("DEBUG: Search field setup completed");
    }
    
    private void setupTableRowClickEvent() {
        tableView.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    showCustomerDetailDialog(row.getItem());
                }
            });
            return row;
        });
    }
    
    private void showConnectionSetup() {
        ConnectionSetupView connectionSetupView = new ConnectionSetupView();
        boolean connected = connectionSetupView.showAndWait();
        
        if (connected) {
            updateConnectionStatus(true);
            loadData();
        } else {
            updateConnectionStatus(false);
        }
    }
    
    private void updateConnectionStatus(boolean connected) {
        if (connected) {
            connectionStatusLabel.setText("Connected");
            connectionStatusLabel.setStyle("-fx-text-fill: green;");
        } else {
            connectionStatusLabel.setText("Not Connected");
            connectionStatusLabel.setStyle("-fx-text-fill: red;");
        }
    }
    
    @FXML
    private void handleOpenConnection() {
        showConnectionSetup();
    }
    
    @FXML
    private void handleCloseConnection() {
        DatabaseConnection.closeConnection();
        updateConnectionStatus(false);
        tableView.setItems(FXCollections.observableArrayList());
        pageInfoLabel.setText("Page 0 of 0 (Total records: 0)");
    }
    
    @FXML
    private void handleExit() {
        Platform.exit();
    }
    
    @FXML
    private void handleAddData() {
        if (!DatabaseConnection.isConnected()) {
            showNotConnectedAlert();
            return;
        }
        
        showAddCustomerDialog();
    }
    
    @FXML
    private void handleRefreshTable() {
        System.out.println("DEBUG: ========== REFRESH TABLE CLICKED ==========");

        try {
            // 1. Validasi Koneksi Database
            System.out.println("DEBUG: Checking database connection...");
            if (!DatabaseConnection.isConnected()) {
                System.out.println("DEBUG: Cannot refresh - database not connected");
                showNotConnectedAlert();
                return;
            }
            System.out.println("DEBUG: Database connection verified");

            // 2. Reset State ke Kondisi Awal
            System.out.println("DEBUG: Resetting state to initial conditions...");
            currentPage = 1;
            currentSearch = "";

            // Clear search field
            if (searchField != null) {
                searchField.clear();
                System.out.println("DEBUG: Search field cleared");
            }

            // Reset rows per page to default
            if (rowsPerPageComboBox != null) {
                rowsPerPageComboBox.setValue(10);
                rowsPerPage = 10;
                System.out.println("DEBUG: Rows per page reset to default (10)");
            }

            // 3. Reload Data dari Database
            System.out.println("DEBUG: Reloading data from database...");
            loadData();

            // 4. Update UI Components (additional refresh)
            System.out.println("DEBUG: Refreshing UI components...");
            if (tableView != null) {
                tableView.refresh();
                System.out.println("DEBUG: Table view refreshed");
            }

            System.out.println("DEBUG: Table refresh completed successfully");

        } catch (Exception e) {
            // 5. Error Handling
            System.err.println("DEBUG: Error during table refresh: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Refresh Error",
                     "Failed to refresh table",
                     "An error occurred while refreshing the table: " + e.getMessage());
        }
    }
    
    @FXML
    private void handlePrevPage() {
        System.out.println("DEBUG: ========== PREVIOUS BUTTON CLICKED ==========");
        System.out.println("DEBUG: Current page: " + currentPage + ", Total pages: " + totalPages);
        System.out.println("DEBUG: Database connected: " + DatabaseConnection.isConnected());

        if (!DatabaseConnection.isConnected()) {
            System.out.println("DEBUG: Cannot navigate - database not connected");
            showNotConnectedAlert();
            return;
        }

        if (currentPage > 1) {
            currentPage--;
            System.out.println("DEBUG: Moving to page: " + currentPage);
            loadData();
        } else {
            System.out.println("DEBUG: Already on first page");
        }
    }

    @FXML
    private void handleNextPage() {
        System.out.println("DEBUG: ========== NEXT BUTTON CLICKED ==========");
        System.out.println("DEBUG: Current page: " + currentPage + ", Total pages: " + totalPages);
        System.out.println("DEBUG: Database connected: " + DatabaseConnection.isConnected());

        if (!DatabaseConnection.isConnected()) {
            System.out.println("DEBUG: Cannot navigate - database not connected");
            showNotConnectedAlert();
            return;
        }

        if (currentPage < totalPages) {
            currentPage++;
            System.out.println("DEBUG: Moving to page: " + currentPage);
            loadData();
        } else {
            System.out.println("DEBUG: Already on last page");
        }
    }

    @FXML
    private void handleRowsPerPageChange() {
        System.out.println("DEBUG: ========== ROWS PER PAGE CHANGED VIA FXML ==========");

        if (rowsPerPageComboBox == null) {
            System.err.println("DEBUG: ERROR - rowsPerPageComboBox is null in handler!");
            return;
        }

        Integer selectedValue = rowsPerPageComboBox.getValue();
        System.out.println("DEBUG: Selected value: " + selectedValue);

        if (selectedValue != null) {
            System.out.println("DEBUG: Changing rows per page from " + rowsPerPage + " to " + selectedValue);
            rowsPerPage = selectedValue;
            currentPage = 1;

            if (DatabaseConnection.isConnected()) {
                loadData();
            } else {
                System.out.println("DEBUG: Cannot load data - database not connected");
            }
        } else {
            System.out.println("DEBUG: Selected value is null");
        }
    }
    
    @FXML
    private void handleUploadCSV() {
        if (!DatabaseConnection.isConnected()) {
            showNotConnectedAlert();
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(primaryStage);
        
        if (file != null) {
            Map<String, Integer> result = controller.importCSV(file);
            showAlert(Alert.AlertType.INFORMATION, "Import Result", "CSV Import Completed", 
                    String.format("Successfully imported: %d records\nFailed to import: %d records", 
                            result.get("success"), result.get("failed")));
            loadData();
        }
    }
    
    @FXML
    private void handleDownloadCSV() {
        if (!DatabaseConnection.isConnected()) {
            showNotConnectedAlert();
            return;
        }

        // Test JasperReports first
        System.out.println("DEBUG: Testing JasperReports before CSV export...");
        testJasperReports();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        fileChooser.setInitialFileName("customers.csv");
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            try {
                List<Customer> allCustomers = controller.getCustomers(totalRecords, 0, currentSearch);
                Map<String, Integer> result = controller.exportCSV(file, allCustomers);
                showAlert(Alert.AlertType.INFORMATION, "Export Result", "CSV Export Completed",
                        String.format("Successfully exported: %d records\nFailed to export: %d records",
                                result.get("success"), result.get("failed")));
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to export data", e.getMessage());
            }
        }
    }

    private void testJasperReports() {
        try {
            System.out.println("DEBUG: Testing JasperReports functionality...");
            List<Customer> testCustomers = controller.getCustomers(5, 0, "");
            System.out.println("DEBUG: Got " + testCustomers.size() + " customers for test");

            if (!testCustomers.isEmpty()) {
                System.out.println("DEBUG: Calling JasperReportsUtil.showPrintPreview for test...");
                JasperReportsUtil.showPrintPreview(testCustomers, primaryStage);
                System.out.println("DEBUG: JasperReports test completed successfully");
            }
        } catch (Exception e) {
            System.err.println("DEBUG: JasperReports test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleUploadExcel() {
        if (!DatabaseConnection.isConnected()) {
            showNotConnectedAlert();
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(primaryStage);
        
        if (file != null) {
            Map<String, Integer> result = controller.importExcel(file);
            showAlert(Alert.AlertType.INFORMATION, "Import Result", "Excel Import Completed", 
                    String.format("Successfully imported: %d records\nFailed to import: %d records", 
                            result.get("success"), result.get("failed")));
            loadData();
        }
    }
    
    @FXML
    private void handleDownloadExcel() {
        if (!DatabaseConnection.isConnected()) {
            showNotConnectedAlert();
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        fileChooser.setInitialFileName("customers.xlsx");
        File file = fileChooser.showSaveDialog(primaryStage);
        
        if (file != null) {
            try {
                List<Customer> allCustomers = controller.getCustomers(totalRecords, 0, currentSearch);
                Map<String, Integer> result = controller.exportExcel(file, allCustomers);
                showAlert(Alert.AlertType.INFORMATION, "Export Result", "Excel Export Completed", 
                        String.format("Successfully exported: %d records\nFailed to export: %d records", 
                                result.get("success"), result.get("failed")));
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to export data", e.getMessage());
            }
        }
    }
    
    private void loadData() {
        if (!DatabaseConnection.isConnected()) {
            System.out.println("DEBUG: Database not connected");
            return;
        }

        try {
            int offset = (currentPage - 1) * rowsPerPage;
            System.out.println("DEBUG: Loading data - Page: " + currentPage + ", RowsPerPage: " + rowsPerPage + ", Offset: " + offset + ", Search: '" + currentSearch + "'");

            List<Customer> customers = controller.getCustomers(rowsPerPage, offset, currentSearch);
            totalRecords = controller.getTotalCustomers(currentSearch);
            totalPages = totalRecords > 0 ? (int) Math.ceil((double) totalRecords / rowsPerPage) : 1;

            System.out.println("DEBUG: Retrieved " + customers.size() + " customers, Total records: " + totalRecords + ", Total pages: " + totalPages);

            ObservableList<Customer> data = FXCollections.observableArrayList(customers);
            tableView.setItems(data);

            updatePaginationControls();
        } catch (SQLException e) {
            System.err.println("DEBUG: SQL Error: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load customer data", e.getMessage());
        }
    }
    
    private void updatePaginationControls() {
        String pageText = String.format("Page %d of %d (Total records: %d)", currentPage, totalPages, totalRecords);
        pageInfoLabel.setText(pageText);

        boolean prevDisabled = currentPage <= 1;
        boolean nextDisabled = currentPage >= totalPages || totalPages == 0;

        prevButton.setDisable(prevDisabled);
        nextButton.setDisable(nextDisabled);

        System.out.println("DEBUG: Pagination updated - " + pageText);
        System.out.println("DEBUG: Previous button disabled: " + prevDisabled + ", Next button disabled: " + nextDisabled);
    }
    
    private void showAddCustomerDialog() {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Add New Customer");
        dialog.setHeaderText("Enter customer details");
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField nikField = new TextField();
        TextField nameField = new TextField();
        DatePicker bornDatePicker = new DatePicker();
        CheckBox activeCheckBox = new CheckBox();
        TextField salaryField = new TextField();
        
        grid.add(new Label("NIK:"), 0, 0);
        grid.add(nikField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Born Date:"), 0, 2);
        grid.add(bornDatePicker, 1, 2);
        grid.add(new Label("Active:"), 0, 3);
        grid.add(activeCheckBox, 1, 3);
        grid.add(new Label("Salary:"), 0, 4);
        grid.add(salaryField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the nik field by default
        Platform.runLater(nikField::requestFocus);
        
        // Convert the result to a customer when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Customer customer = new Customer();
                    customer.setNik(nikField.getText());
                    customer.setName(nameField.getText());
                    customer.setBorn(bornDatePicker.getValue());
                    customer.setActive(activeCheckBox.isSelected());
                    customer.setSalary(Integer.parseInt(salaryField.getText()));
                    return customer;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid salary format", "Please enter a valid number for salary.");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Customer> result = dialog.showAndWait();
        
        result.ifPresent(customer -> {
            try {
                controller.addCustomer(customer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Added", "Customer has been successfully added.");
                loadData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add customer", e.getMessage());
            }
        });
    }
    
    private void showCustomerDetailDialog(Customer customer) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Customer Details");
        dialog.setHeaderText("Customer: " + customer.getName());
        
        // Set the button types
        ButtonType editButtonType = new ButtonType("Edit", ButtonBar.ButtonData.LEFT);
        ButtonType deleteButtonType = new ButtonType("Delete", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(editButtonType, deleteButtonType, ButtonType.CLOSE);
        
        // Create the details grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        grid.add(new Label("ID:"), 0, 0);
        grid.add(new Label(String.valueOf(customer.getIdx())), 1, 0);
        grid.add(new Label("NIK:"), 0, 1);
        grid.add(new Label(customer.getNik()), 1, 1);
        grid.add(new Label("Name:"), 0, 2);
        grid.add(new Label(customer.getName()), 1, 2);
        grid.add(new Label("Born Date:"), 0, 3);
        grid.add(new Label(customer.getBorn() != null ? customer.getBorn().toString() : ""), 1, 3);
        grid.add(new Label("Active:"), 0, 4);
        grid.add(new Label(customer.isActive() ? "Yes" : "No"), 1, 4);
        grid.add(new Label("Salary:"), 0, 5);
        grid.add(new Label(String.format("%,d", customer.getSalary())), 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        
        Optional<ButtonType> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            if (result.get() == editButtonType) {
                showEditCustomerDialog(customer);
            } else if (result.get() == deleteButtonType) {
                showDeleteConfirmation(customer);
            }
        }
    }
    
    private void showEditCustomerDialog(Customer customer) {
        Dialog<Customer> dialog = new Dialog<>();
        dialog.setTitle("Edit Customer");
        dialog.setHeaderText("Edit customer details");
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);
        
        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField nikField = new TextField(customer.getNik());
        TextField nameField = new TextField(customer.getName());
        DatePicker bornDatePicker = new DatePicker(customer.getBorn());
        CheckBox activeCheckBox = new CheckBox();
        activeCheckBox.setSelected(customer.isActive());
        TextField salaryField = new TextField(String.valueOf(customer.getSalary()));
        
        grid.add(new Label("NIK:"), 0, 0);
        grid.add(nikField, 1, 0);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Born Date:"), 0, 2);
        grid.add(bornDatePicker, 1, 2);
        grid.add(new Label("Active:"), 0, 3);
        grid.add(activeCheckBox, 1, 3);
        grid.add(new Label("Salary:"), 0, 4);
        grid.add(salaryField, 1, 4);
        
        dialog.getDialogPane().setContent(grid);
        
        // Request focus on the name field by default
        Platform.runLater(nameField::requestFocus);
        
        // Convert the result to a customer when the save button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    Customer updatedCustomer = new Customer();
                    updatedCustomer.setIdx(customer.getIdx());
                    updatedCustomer.setNik(nikField.getText());
                    updatedCustomer.setName(nameField.getText());
                    updatedCustomer.setBorn(bornDatePicker.getValue());
                    updatedCustomer.setActive(activeCheckBox.isSelected());
                    updatedCustomer.setSalary(Integer.parseInt(salaryField.getText()));
                    return updatedCustomer;
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Input Error", "Invalid salary format", "Please enter a valid number for salary.");
                    return null;
                }
            }
            return null;
        });
        
        Optional<Customer> result = dialog.showAndWait();
        
        result.ifPresent(updatedCustomer -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Update");
            confirmAlert.setHeaderText("Update Customer");
            confirmAlert.setContentText("Are you sure you want to update this customer?");
            
            Optional<ButtonType> confirmResult = confirmAlert.showAndWait();
            if (confirmResult.isPresent() && confirmResult.get() == ButtonType.OK) {
                try {
                    controller.updateCustomer(updatedCustomer);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Updated", "Customer has been successfully updated.");
                    loadData();
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update customer", e.getMessage());
                }
            }
        });
    }
    
    private void showDeleteConfirmation(Customer customer) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Customer");
        alert.setContentText("Are you sure you want to delete customer: " + customer.getName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                controller.deleteCustomer(customer.getIdx());
                showAlert(Alert.AlertType.INFORMATION, "Success", "Customer Deleted", "Customer has been successfully deleted.");
                loadData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete customer", e.getMessage());
            }
        }
    }
    
    private void showNotConnectedAlert() {
        showAlert(Alert.AlertType.WARNING, "Not Connected", "Database Connection Required", 
                "Please connect to the database first using Connect > Open Connection menu.");
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // JasperReports Handler Methods

    @FXML
    private void handlePrintPreview() {
        System.out.println("DEBUG: ========== PRINT PREVIEW CLICKED ==========");

        if (!DatabaseConnection.isConnected()) {
            System.out.println("DEBUG: Database not connected for print preview");
            showNotConnectedAlert();
            return;
        }

        try {
            System.out.println("DEBUG: Loading customers for print preview...");
            System.out.println("DEBUG: Current search term: '" + currentSearch + "'");

            // Get customers based on current search filter
            List<Customer> filteredCustomers;
            if (currentSearch == null || currentSearch.trim().isEmpty()) {
                // If no search, get all customers
                filteredCustomers = controller.getCustomers(totalRecords, 0, "");
                System.out.println("DEBUG: No search filter - loading all " + filteredCustomers.size() + " customers");
            } else {
                // If there's a search, get filtered customers (use a large limit to get all filtered results)
                filteredCustomers = controller.getCustomers(10000, 0, currentSearch);
                System.out.println("DEBUG: Search filter '" + currentSearch + "' - loading " + filteredCustomers.size() + " customers");
            }

            if (filteredCustomers.isEmpty()) {
                System.out.println("DEBUG: No customers found for report");
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No Data to Preview",
                         "There are no customers to display in the report based on current filter.");
                return;
            }

            System.out.println("DEBUG: Calling JasperReportsUtil.showPrintPreview...");
            // Show print preview
            JasperReportsUtil.showPrintPreview(filteredCustomers, primaryStage);
            System.out.println("DEBUG: Print preview completed");

        } catch (SQLException e) {
            System.err.println("DEBUG: SQL Error in print preview: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load data for preview", e.getMessage());
        } catch (Exception e) {
            System.err.println("DEBUG: General Error in print preview: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to generate preview", e.getMessage());
        }
    }

    @FXML
    private void handleExportPDF() {
        if (!DatabaseConnection.isConnected()) {
            showNotConnectedAlert();
            return;
        }

        try {
            System.out.println("DEBUG: Current search term for export: '" + currentSearch + "'");

            // Get customers based on current search filter
            List<Customer> filteredCustomers;
            if (currentSearch == null || currentSearch.trim().isEmpty()) {
                // If no search, get all customers
                filteredCustomers = controller.getCustomers(totalRecords, 0, "");
                System.out.println("DEBUG: Export - No search filter, loading all " + filteredCustomers.size() + " customers");
            } else {
                // If there's a search, get filtered customers (use a large limit to get all filtered results)
                filteredCustomers = controller.getCustomers(10000, 0, currentSearch);
                System.out.println("DEBUG: Export - Search filter '" + currentSearch + "', loading " + filteredCustomers.size() + " customers");
            }

            if (filteredCustomers.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "No Data to Export",
                         "There are no customers to export to PDF based on current filter.");
                return;
            }

            // Export to PDF
            JasperReportsUtil.exportToPDF(filteredCustomers, primaryStage);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load data for export", e.getMessage());
        }
    }
}