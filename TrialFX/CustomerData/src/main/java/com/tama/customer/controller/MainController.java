package com.tama.customer.controller;

import com.tama.customer.dao.CustomerDAO;
import com.tama.customer.dao.CustomerDAOImpl;
import com.tama.customer.model.Customer;
import com.tama.customer.util.DBConnectionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MainController {
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nikColumn;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, LocalDate> bornColumn;
    @FXML private TableColumn<Customer, Boolean> activeColumn;
    @FXML private TableColumn<Customer, Double> salaryColumn;

    @FXML private TextField searchField;
    @FXML private ComboBox<Integer> recordsPerPageComboBox;
    @FXML private Label resultCountLabel;
    @FXML private Label totalSalaryLabel;
    @FXML private Label pageInfoLabel;

    private CustomerDAO customerDAO;
    private ObservableList<Customer> customerData;
    private int currentPage = 1;
    private int totalPages = 1;
    private String currentSearch = "";

    @FXML
    private void initialize() {
        customerDAO = new CustomerDAOImpl();
        customerData = FXCollections.observableArrayList();

        // Initialize table columns
        nikColumn.setCellValueFactory(new PropertyValueFactory<>("nik"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bornColumn.setCellValueFactory(new PropertyValueFactory<>("born"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

        // Format salary column
        salaryColumn.setCellFactory(column -> new TableCell<Customer, Double>() {
            private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(item));
                }
            }
        });

        // Initialize records per page combo box
        recordsPerPageComboBox.setItems(FXCollections.observableArrayList(10, 25, 50, 100));
        recordsPerPageComboBox.setValue(25);

        // Load initial data
        refreshTable();
    }

    @FXML
    private void handleOpenConnection() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/customer/view/database-connection.fxml"));
            Scene scene = new Scene(loader.load());
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Database Connection");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            DatabaseConnectionController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isConnectionSuccessful()) {
                refreshTable();
            }
        } catch (IOException e) {
            showError("Error", "Could not load database connection dialog", e.getMessage());
        }
    }

    @FXML
    private void handleCloseConnection() {
        DBConnectionManager.getInstance().closeConnection();
        customerData.clear();
        updateResultInfo();
    }

    @FXML
    private void handleSearch() {
        currentSearch = searchField.getText();
        currentPage = 1;
        refreshTable();
    }

    @FXML
    private void handleRecordsPerPageChange() {
        currentPage = 1;
        refreshTable();
    }

    @FXML
    private void handleAddData() {
        Customer customer = new Customer();
        if (showCustomerDialog(customer, false)) {
            customerDAO.insert(customer);
            refreshTable();
        }
    }

    @FXML
    private void handleRefreshTable() {
        refreshTable();
    }

    @FXML
    private void handleTableClick() {
        if (customerTable.getSelectionModel().getSelectedItem() != null) {
            Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
            if (customerTable.getSelectionModel().getSelectedCells().size() > 0) {
                TablePosition pos = customerTable.getSelectionModel().getSelectedCells().get(0);
                if (pos.getColumn() >= 0 && customerTable.getSelectionModel().getSelectedItem() != null) {
                    showEditDialog(selectedCustomer);
                }
            }
        }
    }

    private void showEditDialog(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/customer/view/customer-dialog.fxml"));
            Scene scene = new Scene(loader.load());
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            CustomerDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            Customer tempCustomer = new Customer(
                customer.getNik(),
                customer.getName(),
                customer.getBorn(),
                customer.isActive(),
                customer.getSalary()
            );
            controller.setCustomer(tempCustomer, true);

            dialogStage.showAndWait();

            if (controller.isSaveClicked()) {
                customerDAO.updateCustomer(tempCustomer);
                refreshTable();
            } else if (controller.isDeleteClicked()) {
                customerDAO.deleteCustomer(customer.getNik());
                refreshTable();
            }
        } catch (IOException e) {
            e.printStackTrace();
            showError("Error", "Could not load the dialog", e.getMessage());
        }
    }

    private boolean showCustomerDialog(Customer customer, boolean isEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tama/customer/view/customer-dialog.fxml"));
            Scene scene = new Scene(loader.load());

            Stage dialogStage = new Stage();
            dialogStage.setTitle(isEdit ? "Edit Customer" : "Add Customer");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(scene);

            CustomerDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCustomer(customer, isEdit);

            dialogStage.showAndWait();
            return controller.isSaveClicked();
        } catch (IOException e) {
            showError("Error", "Could not load customer dialog", e.getMessage());
            return false;
        }
    }

    @FXML
    private void handleFirst() {
        if (currentPage > 1) {
            currentPage = 1;
            refreshTable();
        }
    }

    @FXML
    private void handlePrevious() {
        if (currentPage > 1) {
            currentPage--;
            refreshTable();
        }
    }

    @FXML
    private void handleNext() {
        if (currentPage < totalPages) {
            currentPage++;
            refreshTable();
        }
    }

    @FXML
    private void handleLast() {
        if (currentPage < totalPages) {
            currentPage = totalPages;
            refreshTable();
        }
    }

    @FXML
    private void handlePrintPreview() {
        try {
            // Load the report template
            InputStream reportTemplate = getClass().getResourceAsStream("/com/tama/customer/reports/CustomerReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

            // Set parameters
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("SEARCH_TERM", currentSearch);

            // Create the report
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parameters,
                DBConnectionManager.getInstance().getConnection()
            );

            // Show the report viewer
            JasperViewer.viewReport(jasperPrint, false);

        } catch (Exception e) {
            showError("Error", "Failed to generate report", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleExportPDF() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Report");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(customerTable.getScene().getWindow());

            if (file != null) {
                // Load the report template
                InputStream reportTemplate = getClass().getResourceAsStream("/com/tama/customer/reports/CustomerReport.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(reportTemplate);

                // Set parameters
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("SEARCH_TERM", currentSearch);

                // Create the report
                JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parameters,
                    DBConnectionManager.getInstance().getConnection()
                );

                // Export to PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, file.getAbsolutePath());

                showInformation("Success", "Report exported successfully",
                    "The report has been exported to: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            showError("Error", "Failed to export report", e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUploadCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload CSV File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showOpenDialog(customerTable.getScene().getWindow());
        if (file != null) {
            importCSV(file);
        }
    }

    @FXML
    private void handleUploadExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload Excel File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        File file = fileChooser.showOpenDialog(customerTable.getScene().getWindow());
        if (file != null) {
            importExcel(file);
        }
    }

    @FXML
    private void handleDownloadCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );

        File file = fileChooser.showSaveDialog(customerTable.getScene().getWindow());
        if (file != null) {
            exportCSV(file);
        }
    }

    @FXML
    private void handleDownloadExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Excel File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );

        File file = fileChooser.showSaveDialog(customerTable.getScene().getWindow());
        if (file != null) {
            exportToExcel(file);
        }
    }

    private void refreshTable() {
        try {
            System.out.println("Refreshing table...");
            int recordsPerPage = recordsPerPageComboBox.getValue();
            int offset = (currentPage - 1) * recordsPerPage;

            System.out.println("Current search term: '" + currentSearch + "'");
            System.out.println("Records per page: " + recordsPerPage);
            System.out.println("Current page: " + currentPage);
            System.out.println("Offset: " + offset);

            List<Customer> customers = customerDAO.getPaginatedResults(offset, recordsPerPage, currentSearch);
            System.out.println("Retrieved " + customers.size() + " customers from database");

            customerData.clear();
            customerData.addAll(customers);
            customerTable.setItems(customerData);
            customerTable.refresh();

            int totalRecords = customerDAO.getSearchResultCount(currentSearch);
            totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
            System.out.println("Total records: " + totalRecords);
            System.out.println("Total pages: " + totalPages);

            updateResultInfo();
        } catch (Exception e) {
            System.err.println("Error refreshing table: " + e.getMessage());
            e.printStackTrace();
            showError("Error", "Could not refresh table", e.getMessage());
        }
    }

    private void updateResultInfo() {
        int totalRecords = customerDAO.getSearchResultCount(currentSearch);
        double totalSalary = customerDAO.getTotalSalaryFromSearch(currentSearch);

        resultCountLabel.setText(String.format("%d record(s) found", totalRecords));
        totalSalaryLabel.setText(NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(totalSalary));
        pageInfoLabel.setText(String.format("Page %d of %d", currentPage, totalPages));
    }

    private void exportToExcel(File file) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Customers");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("NIK");
            headerRow.createCell(1).setCellValue("Name");
            headerRow.createCell(2).setCellValue("Born");
            headerRow.createCell(3).setCellValue("Active");
            headerRow.createCell(4).setCellValue("Salary");

            // Add data rows
            int rowNum = 1;
            for (Customer customer : customerData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(customer.getNik());
                row.createCell(1).setCellValue(customer.getName());
                row.createCell(2).setCellValue(customer.getBorn().toString());
                row.createCell(3).setCellValue(customer.isActive());
                row.createCell(4).setCellValue(customer.getSalary());
            }

            // Auto-size columns
            for (int i = 0; i < 5; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }
        } catch (IOException e) {
            showError("Error", "Could not export to Excel", e.getMessage());
        }
    }

    private void exportCSV(File file) {
        try (PrintWriter writer = new PrintWriter(file)) {
            // Write header
            writer.println("NIK;Name;Born;Active;Salary");

            // Write data
            for (Customer customer : customerData) {
                writer.printf("%s;%s;%s;%b;%.2f%n",
                    customer.getNik(),
                    customer.getName(),
                    customer.getBorn(),
                    customer.isActive(),
                    customer.getSalary()
                );
            }
        } catch (IOException e) {
            showError("Error", "Could not export to CSV", e.getMessage());
        }
    }

    private void importCSV(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;
            int importedCount = 0;

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header row
                }

                String[] data = line.split(";");
                if (data.length == 5) {
                    Customer customer = new Customer(
                        data[0].trim(),
                        data[1].trim(),
                        LocalDate.parse(data[2].trim()),
                        Boolean.parseBoolean(data[3].trim()),
                        Double.parseDouble(data[4].trim())
                    );
                    customerDAO.insert(customer);
                    importedCount++;
                }
            }
            refreshTable();
            showInformation("Import Berhasil", "Data CSV berhasil diimpor",
                String.format("Berhasil mengimpor %d data dari file: %s", importedCount, file.getName()));
        } catch (IOException | NumberFormatException | DateTimeParseException e) {
            showError("Error", "Could not import CSV file", e.getMessage());
        }
    }

    private void importExcel(File file) {
        try (Workbook workbook = WorkbookFactory.create(file)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            int importedCount = 0;

            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                try {
                    String nik = getStringCellValue(row.getCell(0));
                    String name = getStringCellValue(row.getCell(1));
                    LocalDate born = getDateCellValue(row.getCell(2));
                    boolean active = getBooleanCellValue(row.getCell(3));
                    double salary = getDoubleCellValue(row.getCell(4));

                    Customer customer = new Customer(nik, name, born, active, salary);
                    customerDAO.insert(customer);
                    importedCount++;
                } catch (Exception e) {
                    // Log the error but continue processing other rows
                    System.err.println("Error processing row: " + row.getRowNum() + " - " + e.getMessage());
                }
            }
            refreshTable();
            showInformation("Import Berhasil", "Data Excel berhasil diimpor",
                String.format("Berhasil mengimpor %d data dari file: %s", importedCount, file.getName()));
        } catch (IOException e) {
            showError("Error", "Could not import Excel file", e.getMessage());
        }
    }

    private String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            default: return "";
        }
    }

    private LocalDate getDateCellValue(Cell cell) {
        if (cell == null) return LocalDate.now();
        switch (cell.getCellType()) {
            case STRING: return LocalDate.parse(cell.getStringCellValue());
            case NUMERIC: return cell.getLocalDateTimeCellValue().toLocalDate();
            default: return LocalDate.now();
        }
    }

    private boolean getBooleanCellValue(Cell cell) {
        if (cell == null) return false;
        switch (cell.getCellType()) {
            case BOOLEAN: return cell.getBooleanCellValue();
            case STRING: return Boolean.parseBoolean(cell.getStringCellValue());
            case NUMERIC: return cell.getNumericCellValue() != 0;
            default: return false;
        }
    }

    private double getDoubleCellValue(Cell cell) {
        if (cell == null) return 0.0;
        switch (cell.getCellType()) {
            case NUMERIC: return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default: return 0.0;
        }
    }

    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInformation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
