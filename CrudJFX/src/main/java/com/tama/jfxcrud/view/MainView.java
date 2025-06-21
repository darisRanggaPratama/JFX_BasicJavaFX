package com.tama.jfxcrud.view;

import com.tama.jfxcrud.controller.CustomerController;
import com.tama.jfxcrud.model.Customer;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainView {
    private final CustomerController controller;
    private final Stage primaryStage;
    
    private TableView<Customer> tableView;
    private TextField searchField;
    private ComboBox<Integer> rowsPerPageComboBox;
    private Label pageInfoLabel;
    private Button prevButton;
    private Button nextButton;
    
    private int currentPage = 1;
    private int totalPages = 1;
    private int totalRecords = 0;
    private int rowsPerPage = 10;
    private String currentSearch = "";
    
    public MainView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new CustomerController();
        
        createUI();
        loadData();
    }
    
    private void createUI() {
        primaryStage.setTitle("Customer Management System");
        
        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(10));
        
        // Top section - Search
        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setPadding(new Insets(0, 0, 10, 0));
        
        Label searchLabel = new Label("Search:");
        searchField = new TextField();
        searchField.setPrefWidth(300);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            currentSearch = newVal;
            currentPage = 1;
            loadData();
        });
        
        searchBox.getChildren().addAll(searchLabel, searchField);
        mainLayout.setTop(searchBox);
        
        // Center - Table
        tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Customer, Integer> idxCol = new TableColumn<>("ID");
        idxCol.setCellValueFactory(new PropertyValueFactory<>("idx"));
        
        TableColumn<Customer, String> nikCol = new TableColumn<>("NIK");
        nikCol.setCellValueFactory(new PropertyValueFactory<>("nik"));
        
        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<Customer, LocalDate> bornCol = new TableColumn<>("Born Date");
        bornCol.setCellValueFactory(new PropertyValueFactory<>("born"));
        bornCol.setCellFactory(column -> new TableCell<>() {
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
        
        TableColumn<Customer, Boolean> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeCol.setCellFactory(column -> new TableCell<>() {
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
        
        TableColumn<Customer, Integer> salaryCol = new TableColumn<>("Salary");
        salaryCol.setCellValueFactory(new PropertyValueFactory<>("salary"));
        salaryCol.setCellFactory(column -> new TableCell<>() {
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
        
        tableView.getColumns().addAll(idxCol, nikCol, nameCol, bornCol, activeCol, salaryCol);
        
        // Row click event
        tableView.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    showCustomerDetailDialog(row.getItem());
                }
            });
            return row;
        });
        
        mainLayout.setCenter(tableView);
        
        // Bottom - Pagination and buttons
        VBox bottomBox = new VBox(10);
        bottomBox.setPadding(new Insets(10, 0, 0, 0));
        
        // Pagination controls
        HBox paginationBox = new HBox(10);
        paginationBox.setAlignment(Pos.CENTER);
        
        prevButton = new Button("Previous");
        prevButton.setOnAction(e -> {
            if (currentPage > 1) {
                currentPage--;
                loadData();
            }
        });
        
        nextButton = new Button("Next");
        nextButton.setOnAction(e -> {
            if (currentPage < totalPages) {
                currentPage++;
                loadData();
            }
        });
        
        Label rowsPerPageLabel = new Label("Rows per page:");
        rowsPerPageComboBox = new ComboBox<>(FXCollections.observableArrayList(1, 5, 10, 25, 50, 100));
        rowsPerPageComboBox.setValue(10);
        rowsPerPageComboBox.setOnAction(e -> {
            rowsPerPage = rowsPerPageComboBox.getValue();
            currentPage = 1;
            loadData();
        });
        
        pageInfoLabel = new Label();
        
        paginationBox.getChildren().addAll(prevButton, pageInfoLabel, nextButton, rowsPerPageLabel, rowsPerPageComboBox);
        
        // Action buttons
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER);
        
        Button addButton = new Button("Add Customer");
        addButton.setOnAction(e -> showAddCustomerDialog());
        
        Button uploadCSVButton = new Button("Upload CSV");
        uploadCSVButton.setOnAction(e -> handleUploadCSV());
        
        Button downloadCSVButton = new Button("Download CSV");
        downloadCSVButton.setOnAction(e -> handleDownloadCSV());
        
        Button uploadExcelButton = new Button("Upload Excel");
        uploadExcelButton.setOnAction(e -> handleUploadExcel());
        
        Button downloadExcelButton = new Button("Download Excel");
        downloadExcelButton.setOnAction(e -> handleDownloadExcel());
        
        actionBox.getChildren().addAll(addButton, uploadCSVButton, downloadCSVButton, uploadExcelButton, downloadExcelButton);
        
        bottomBox.getChildren().addAll(paginationBox, actionBox);
        mainLayout.setBottom(bottomBox);
        
        // Set scene
        Scene scene = new Scene(mainLayout, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/tama/jfxcrud/styles.css").toExternalForm());
        primaryStage.setScene(scene);
    }
    
    private void loadData() {
        try {
            int offset = (currentPage - 1) * rowsPerPage;
            List<Customer> customers = controller.getCustomers(rowsPerPage, offset, currentSearch);
            totalRecords = controller.getTotalCustomers(currentSearch);
            totalPages = (int) Math.ceil((double) totalRecords / rowsPerPage);
            
            ObservableList<Customer> data = FXCollections.observableArrayList(customers);
            tableView.setItems(data);
            
            updatePaginationControls();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load customer data", e.getMessage());
        }
    }
    
    private void updatePaginationControls() {
        pageInfoLabel.setText(String.format("Page %d of %d (Total records: %d)", currentPage, totalPages, totalRecords));
        prevButton.setDisable(currentPage <= 1);
        nextButton.setDisable(currentPage >= totalPages);
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
    
    private void handleUploadCSV() {
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
    
    private void handleDownloadCSV() {
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
    
    private void handleUploadExcel() {
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
    
    private void handleDownloadExcel() {
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
    
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}