package com.tama.customer;

import com.tama.customer.config.DatabaseConfig;
import com.tama.customer.dao.DBConnectionManager;
import com.tama.customer.model.Customer;
import com.tama.customer.service.CustomerService;
import com.tama.customer.service.ExportService;
import com.tama.customer.service.ImportService;
import com.tama.customer.view.CustomerDialog;
import com.tama.customer.view.DatabaseConnectionView;
import com.tama.customer.view.ProgressDialog;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.BiFunction;

public class MainApplication extends Application {
    private CustomerService customerService;
    private Stage primaryStage;
    private TableView<Customer> tableView;
    private Label statusLabel;
    private Label totalRecordsLabel;
    private TextField searchField;  // Add this field at class level

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        customerService = new CustomerService();

        // Create an initial empty scene to ensure proper initialization
        Scene initialScene = new Scene(new javafx.scene.layout.StackPane(), 1, 1);
        stage.setScene(initialScene);
        stage.hide();

        // Show database connection dialog
        DatabaseConnectionView dbView = new DatabaseConnectionView(primaryStage);
        dbView.showAndWait().ifPresent(success -> {
            if (success) {
                showMainWindow();
            } else {
                System.exit(0);
            }
        });
    }

    private void showMainWindow() {
        BorderPane root = new BorderPane();
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);

        // Create search box and table container
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(10));

        // Add search box
        HBox searchBox = new HBox(10);
        searchBox.getStyleClass().add("search-box");
        searchField = new TextField();  // Use the class field instead of local variable
        searchField.setPromptText("Search by NIK or Name");
        searchField.setPrefWidth(300);
        Button searchButton = new Button("Search");
        searchBox.getChildren().addAll(new Label("Search:"), searchField, searchButton);

        // Create table
        tableView = createTableView();

        centerBox.getChildren().addAll(searchBox, tableView);
        root.setCenter(centerBox);

        // Add search functionality
        searchButton.setOnAction(e -> {
            try {
                ObservableList<Customer> searchResults = customerService.searchCustomers(searchField.getText());
                tableView.setItems(searchResults);
                updateStatus();
            } catch (SQLException ex) {
                showError("Search Error", "Failed to search customers: " + ex.getMessage());
            }
        });

        // Add enter key handler for search
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                searchButton.fire();
            }
        });

        // Create status bar
        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        // Setup keyboard shortcuts
        setupKeyboardShortcuts(scene);

        primaryStage.setTitle("Customer Management System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load initial data
        refreshData();
        updateStatus();

        primaryStage.setOnCloseRequest(event -> {
            event.consume(); // Prevent default close operation
            closeApplication();
        });
    }

    private void closeApplication() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Exit Application");
        confirmation.setHeaderText("Are you sure you want to exit?");
        confirmation.setContentText("Any unsaved changes will be lost.");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            // Close database connection
            DBConnectionManager.getInstance().closeConnection();
            Platform.exit();
        }
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Connect Menu
        Menu connectMenu = new Menu("Connect");
        MenuItem openConnection = new MenuItem("Open Connection");
        MenuItem closeConnection = new MenuItem("Close Connection");
        connectMenu.getItems().addAll(openConnection, closeConnection);

        // Data Menu
        Menu dataMenu = new Menu("Data");
        MenuItem addData = new MenuItem("Add Data");
        addData.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        MenuItem editData = new MenuItem("Edit Selected");
        editData.setAccelerator(KeyCombination.keyCombination("Ctrl+E"));
        MenuItem deleteData = new MenuItem("Delete Selected");
        deleteData.setAccelerator(KeyCombination.keyCombination("Delete"));
        MenuItem refreshTable = new MenuItem("Refresh Data");
        refreshTable.setAccelerator(KeyCombination.keyCombination("Ctrl+R"));
        dataMenu.getItems().addAll(addData, editData, deleteData, new SeparatorMenuItem(), refreshTable);

        // View Menu
        Menu viewMenu = new Menu("View");
        MenuItem search = new MenuItem("Search");
        search.setAccelerator(KeyCombination.keyCombination("Ctrl+F"));
        MenuItem clearSearch = new MenuItem("Clear Search");
        MenuItem showActive = new MenuItem("Show Active Only");
        MenuItem showAll = new MenuItem("Show All");
        viewMenu.getItems().addAll(search, clearSearch, new SeparatorMenuItem(),
                                 showActive, showAll);

        // Export/Import Menu
        Menu exportMenu = new Menu("Export");
        MenuItem exportPDF = new MenuItem("Export to PDF");
        exportPDF.setAccelerator(KeyCombination.keyCombination("Ctrl+P"));
        MenuItem exportCSV = new MenuItem("Export to CSV");
        exportCSV.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        MenuItem exportExcel = new MenuItem("Export to Excel");
        exportExcel.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+E"));
        exportMenu.getItems().addAll(exportPDF, new SeparatorMenuItem(),
                                   exportCSV, exportExcel);

        Menu importMenu = new Menu("Import");
        MenuItem importCSV = new MenuItem("Import from CSV");
        MenuItem importExcel = new MenuItem("Import from Excel");
        importMenu.getItems().addAll(importCSV, importExcel);

        // Event handlers for import
        importCSV.setOnAction(e -> handleImportCSV());
        importExcel.setOnAction(e -> handleImportExcel());

        // Help Menu
        Menu helpMenu = new Menu("Help");
        MenuItem shortcuts = new MenuItem("Keyboard Shortcuts");
        shortcuts.setAccelerator(KeyCombination.keyCombination("F1"));
        MenuItem about = new MenuItem("About");
        helpMenu.getItems().addAll(shortcuts, about);

        menuBar.getMenus().addAll(connectMenu, dataMenu, viewMenu, exportMenu, importMenu, helpMenu);

        // Add event handlers
        // Connect handlers
        openConnection.setOnAction(e -> {
            DatabaseConnectionView connectionView = new DatabaseConnectionView(primaryStage);
            connectionView.showAndWait().ifPresent(success -> {
                if (success) {
                    refreshData();
                }
            });
        });

        closeConnection.setOnAction(e -> {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Close Connection");
            confirmation.setHeaderText("Close Database Connection");
            confirmation.setContentText("Are you sure you want to close the database connection?");

            if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                DBConnectionManager.getInstance().closeConnection();
                statusLabel.setText("Database Status: Disconnected");
                statusLabel.getStyleClass().remove("connected");
                statusLabel.getStyleClass().add("disconnected");
                tableView.getItems().clear();
                totalRecordsLabel.setText("Total Records: 0");

                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Connection Closed");
                info.setHeaderText(null);
                info.setContentText("Database connection has been closed successfully.");
                info.showAndWait();
            }
        });

        // Data handlers
        addData.setOnAction(e -> showAddCustomerDialog());
        editData.setOnAction(e -> {
            Customer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                showAddEditDialog(selected);
            } else {
                showError("No Selection", "Please select a customer to edit");
            }
        });
        deleteData.setOnAction(e -> deleteCustomer());
        refreshTable.setOnAction(e -> refreshData());

        // View handlers
        search.setOnAction(e -> searchField.requestFocus());
        clearSearch.setOnAction(e -> {
            searchField.clear();
            refreshData();
        });
        showActive.setOnAction(e -> filterActiveCustomers());
        showAll.setOnAction(e -> refreshData());

        // Export handlers
        exportPDF.setOnAction(e -> exportToPdf());
        exportCSV.setOnAction(e -> exportToCsv());
        exportExcel.setOnAction(e -> exportToExcel());

        // Import handlers
        importCSV.setOnAction(e -> handleImportCSV());
        importExcel.setOnAction(e -> handleImportExcel());

        // Help handlers
        shortcuts.setOnAction(e -> showKeyboardShortcuts());
        about.setOnAction(e -> showAboutDialog());

        return menuBar;
    }

    private TableView<Customer> createTableView() {
        TableView<Customer> table = new TableView<>();

        TableColumn<Customer, String> nikCol = new TableColumn<>("NIK");
        nikCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNik()));

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Customer, LocalDate> bornCol = new TableColumn<>("Birth Date");
        bornCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getBorn()));
        bornCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                }
            }
        });

        TableColumn<Customer, Boolean> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isActive()));
        activeCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    CheckBox checkBox = new CheckBox();
                    checkBox.setSelected(item);
                    checkBox.setDisable(true);
                    setGraphic(checkBox);
                }
            }
        });

        TableColumn<Customer, BigDecimal> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getSalary()));
        salaryCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal salary, boolean empty) {
                super.updateItem(salary, empty);
                if (empty || salary == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.2f", salary));
                }
            }
        });
        salaryCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        // Set column widths
        nikCol.setPrefWidth(100);
        nameCol.setPrefWidth(200);
        bornCol.setPrefWidth(100);
        activeCol.setPrefWidth(60);
        salaryCol.setPrefWidth(120);

        table.getColumns().addAll(nikCol, nameCol, bornCol, activeCol, salaryCol);

        // Add context menu for row operations
        table.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();

            MenuItem editItem = new MenuItem("Edit");
            editItem.setOnAction(event -> showAddEditDialog(row.getItem()));

            MenuItem deleteItem = new MenuItem("Delete");
            deleteItem.setOnAction(event -> {
                if (row.getItem() != null) {
                    try {
                        customerService.deleteCustomer(row.getItem().getIdx());
                        refreshData();
                    } catch (SQLException e) {
                        showError("Error", "Failed to delete customer: " + e.getMessage());
                    }
                }
            });

            contextMenu.getItems().addAll(editItem, deleteItem);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );

            return row;
        });

        return table;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(20);
        statusBar.setPadding(new Insets(10));
        statusBar.getStyleClass().add("status-bar");

        statusLabel = new Label();
        totalRecordsLabel = new Label();

        statusBar.getChildren().addAll(statusLabel, totalRecordsLabel);
        return statusBar;
    }

    private void refreshData() {
        try {
            ObservableList<Customer> customers = customerService.getAllCustomers();
            tableView.setItems(customers);
            updateStatus();
        } catch (SQLException e) {
            showError("Error loading data", e.getMessage());
        }
    }

    private void updateStatus() {
        try {
            boolean connected = DatabaseConfig.getInstance().testConnection();
            statusLabel.setText("Database Status: " + (connected ? "Connected" : "Disconnected"));
            statusLabel.getStyleClass().add(connected ? "connected" : "disconnected");

            int total = customerService.getTotalCustomers();
            totalRecordsLabel.setText("Total Records: " + total);
        } catch (SQLException e) {
            statusLabel.setText("Database Status: Error");
            statusLabel.getStyleClass().add("disconnected");
        }
    }

    private void showError(String title, String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        showError(title, null, message);
    }

    private void deleteCustomer() {
        Customer selected = tableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Error", "Please select a customer to delete");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete this customer?");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                customerService.deleteCustomer(selected.getIdx());
                refreshData();
            } catch (SQLException e) {
                showError("Error deleting customer", e.getMessage());
            }
        }
    }

    private String getDownloadsPath() {
        return System.getProperty("user.home") + "/Downloads";
    }

    private void exportToPdf() {
        // Get the current items shown in the table view (filtered by search)
        List<Customer> customers = tableView.getItems();

        if (customers.isEmpty()) {
            showError("Export Error", "No data to export. The search results are empty.");
            return;
        }

        String fileName = "customers_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".pdf";
        String path = getDownloadsPath() + "/" + fileName;

        Task<Void> task = new ExportService().exportToPdf(customers, path);

        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.setTitle("Export Progress");
        progressDialog.setHeaderText(null);
        progressDialog.setContentText("Exporting searched data to PDF...");

        task.setOnSucceeded(e -> {
            progressDialog.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Complete");
            alert.setHeaderText(null);
            alert.setContentText("Export completed successfully!\nFile saved in Downloads folder as: " + fileName);
            alert.showAndWait();
        });

        task.setOnFailed(e -> {
            progressDialog.close();
            showError("Export Error", task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void exportToExcel() {
        // Get the current items shown in the table view (filtered by search)
        List<Customer> customers = tableView.getItems();
        
        if (customers.isEmpty()) {
            showError("Export Error", "No data to export. The search results are empty.");
            return;
        }

        String fileName = "customers_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        String path = getDownloadsPath() + "/" + fileName;

        Task<Void> task = new ExportService().exportToExcel(customers, path);

        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.setTitle("Export Progress");
        progressDialog.setHeaderText(null);
        progressDialog.setContentText("Exporting searched data to Excel...");

        task.setOnSucceeded(e -> {
            progressDialog.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Complete");
            alert.setHeaderText(null);
            alert.setContentText("Export completed successfully!\nFile saved in Downloads folder as: " + fileName);
            alert.showAndWait();
        });

        task.setOnFailed(e -> {
            progressDialog.close();
            showError("Export Error", task.getException().getMessage());
        });

        new Thread(task).start();
    }

    private void exportToCsv() {
        // Get the current items shown in the table view (filtered by search)
        List<Customer> customers = tableView.getItems();

        if (customers.isEmpty()) {
            showError("Export Error", "No data to export. The search results are empty.");
            return;
        }

        String fileName = "customers_export_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
        String path = getDownloadsPath() + "/" + fileName;

        Task<Void> task = new ExportService().exportToCsv(customers, path);

        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.setTitle("Export Progress");
        progressDialog.setHeaderText(null);
        progressDialog.setContentText("Exporting searched data to CSV...");

        task.setOnSucceeded(e -> {
            progressDialog.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Export Complete");
            alert.setHeaderText(null);
            alert.setContentText("Export completed successfully!\nFile saved in Downloads folder as: " + fileName);
            alert.showAndWait();
        });

        task.setOnFailed(e -> {
            progressDialog.close();
            showError("Export Error", task.getException().getMessage());
        });

        new Thread(task).start();
    }


    private void showAddEditDialog(Customer customer) {
        CustomerDialog dialog = new CustomerDialog(customer);
        dialog.showAndWait().ifPresent(result -> {
            try {
                if (customer == null) {
                    customerService.saveCustomer(result);
                } else {
                    customerService.updateCustomer(result);
                }
                refreshData();
            } catch (SQLException e) {
                showError("Error saving customer", e.getMessage());
            }
        });
    }

    private void showAddCustomerDialog() {
        showAddEditDialog(null);
    }

    private void setupKeyboardShortcuts(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                switch (e.getCode()) {
                    case N -> showAddCustomerDialog();
                    case R -> refreshData();
                    case F -> tableView.requestFocus();
                    case DELETE -> deleteCustomer();
                    default -> {}
                }
            }
        });
    }

    private void showKeyboardShortcuts() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Keyboard Shortcuts");
        alert.setHeaderText("Available Keyboard Shortcuts");
        alert.setContentText("""
            Data Management:
            Ctrl+N: Add new customer
            Ctrl+E: Edit selected customer
            Delete: Delete selected customer
            Ctrl+R: Refresh data
            
            View & Search:
            Ctrl+F: Focus search box
            Enter: Trigger search
            
            Export:
            Ctrl+P: Export to PDF
            Ctrl+S: Export to CSV
            Ctrl+Shift+E: Export to Excel
            
            General:
            F1: Show this help
            Esc: Close dialogs
            """);
        alert.showAndWait();
    }

    private void showAboutDialog() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("About Customer Management System");
        about.setHeaderText("Customer Management System");
        about.setContentText("""
            Version 1.0
            Built with JavaFX 21
            
            A modern desktop application for managing customer data
            with support for various data formats and reporting.""");
        about.showAndWait();
    }

    private void filterActiveCustomers() {
        try {
            ObservableList<Customer> customers = customerService.getAllCustomers();
            ObservableList<Customer> activeCustomers = FXCollections.observableArrayList(
                customers.filtered(Customer::isActive)
            );
            tableView.setItems(activeCustomers);
            updateStatus();
        } catch (SQLException e) {
            showError("Error loading data", e.getMessage());
        }
    }

    private void handleImportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            ImportService importService = new ImportService(customerService.getCustomerDAO());
            Task<ImportService.ImportResult> task = new Task<>() {
                @Override
                protected ImportService.ImportResult call() throws Exception {
                    return importService.importFromCSV(file, progress -> {
                        updateProgress(progress * 100, 100);
                        updateMessage(String.format("Importing... %.0f%%", progress * 100));
                    });
                }
            };

            ProgressDialog progressDialog = new ProgressDialog(task);
            progressDialog.setTitle("Importing CSV");
            progressDialog.initOwner(primaryStage);

            task.setOnSucceeded(event -> {
                ImportService.ImportResult result = task.getValue();
                Platform.runLater(() -> {
                    try {
                        progressDialog.cleanup();
                        progressDialog.hide();

                        if (!result.getSuccessfulImports().isEmpty()) {
                            importService.saveImportedCustomers(result.getSuccessfulImports());
                        }

                        // Show confirmation dialog
                        Alert alert = new Alert(
                            result.getFailures().isEmpty() ? Alert.AlertType.INFORMATION : Alert.AlertType.WARNING
                        );
                        alert.setTitle("Import Result");
                        alert.setHeaderText(null);

                        StringBuilder message = new StringBuilder();
                        message.append("Import completed!\n\n");
                        message.append(String.format("Total rows processed: %d\n", result.getTotalRows()));
                        message.append(String.format("Successfully imported: %d\n", result.getSuccessfulImports().size()));
                        message.append(String.format("Failed to import: %d\n", result.getFailures().size()));

                        if (!result.getFailures().isEmpty()) {
                            message.append("\nFailed rows:\n");
                            result.getFailures().forEach((row, error) ->
                                message.append(String.format("Row %d: %s\n", row, error)));
                        }

                        // Use TextArea for long messages
                        if (message.length() > 200) {
                            TextArea textArea = new TextArea(message.toString());
                            textArea.setEditable(false);
                            textArea.setWrapText(true);
                            textArea.setMaxWidth(Double.MAX_VALUE);
                            textArea.setMaxHeight(Double.MAX_VALUE);
                            alert.getDialogPane().setContent(textArea);
                        } else {
                            alert.setContentText(message.toString());
                        }

                        alert.showAndWait();
                        refreshData();
                    } catch (SQLException e) {
                        showError("Import Error", "Failed to save imported data: " + e.getMessage());
                    }
                });
            });

            task.setOnFailed(event -> {
                Platform.runLater(() -> {
                    progressDialog.cleanup();
                    progressDialog.hide();
                    showError("Import Error", "Failed to import CSV file", task.getException().getMessage());
                });
            });

            Thread importThread = new Thread(task);
            importThread.setDaemon(true);
            importThread.start();
            progressDialog.show();
        }
    }

    private void handleImportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            ImportService importService = new ImportService(customerService.getCustomerDAO());
            Task<ImportService.ImportResult> task = new Task<>() {
                @Override
                protected ImportService.ImportResult call() throws Exception {
                    updateProgress(0, 100);
                    updateMessage("Importing Excel file...");
                    return importService.importFromExcel(file);
                }
            };

            ProgressDialog progressDialog = new ProgressDialog(task);
            progressDialog.setTitle("Importing Excel");
            progressDialog.initOwner(primaryStage);

            task.setOnSucceeded(event -> {
                ImportService.ImportResult result = task.getValue();
                Platform.runLater(() -> {
                    try {
                        progressDialog.close();

                        if (!result.getSuccessfulImports().isEmpty()) {
                            importService.saveImportedCustomers(result.getSuccessfulImports());
                        }
                        showImportResult(result);
                        refreshData();
                    } catch (SQLException e) {
                        showError("Import Error", "Failed to save imported data: " + e.getMessage());
                    }
                });
            });

            task.setOnFailed(event -> {
                Platform.runLater(() -> {
                    progressDialog.close();
                    showError("Import Error", "Failed to import Excel file", task.getException().getMessage());
                });
            });

            Thread importThread = new Thread(task);
            importThread.setDaemon(true);
            importThread.start();
            progressDialog.show();
        }
    }

    private void showImportResult(ImportService.ImportResult result) {
        int successCount = result.getSuccessfulImports().size();
        int failureCount = result.getFailures().size();

        StringBuilder message = new StringBuilder();
        message.append(String.format("Total rows processed: %d\n", result.getTotalRows()));
        message.append(String.format("Successfully imported: %d\n", successCount));
        message.append(String.format("Failed to import: %d\n\n", failureCount));

        if (failureCount > 0) {
            message.append("Failed rows details:\n");
            result.getFailures().forEach((row, error) ->
                message.append(String.format("Row %d: %s\n", row, error)));
        }

        Alert alert = new Alert(failureCount > 0 ? Alert.AlertType.WARNING : Alert.AlertType.INFORMATION);
        alert.setTitle("Import Result");
        alert.setHeaderText(null);
        alert.setContentText(message.toString());

        // If the message is too long, show it in a scrollable text area
        if (message.length() > 200) {
            TextArea textArea = new TextArea(message.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setMaxHeight(400);
            textArea.setMaxWidth(500);

            alert.getDialogPane().setContent(textArea);
        }

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
