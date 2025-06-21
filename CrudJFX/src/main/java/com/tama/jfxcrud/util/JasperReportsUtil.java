package com.tama.jfxcrud.util;

import com.tama.jfxcrud.model.Customer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JasperReportsUtil {

    private static final String REPORT_TEMPLATE = "/reports/customer_report.jrxml";
    private static Object compiledReport;

    /**
     * Compile the JasperReports template using reflection
     */
    private static void compileReport() throws Exception {
        if (compiledReport == null) {
            System.out.println("DEBUG: Looking for report template: " + REPORT_TEMPLATE);
            InputStream reportStream = JasperReportsUtil.class.getResourceAsStream(REPORT_TEMPLATE);
            if (reportStream == null) {
                System.err.println("DEBUG: Report template not found: " + REPORT_TEMPLATE);
                // Try alternative path
                reportStream = JasperReportsUtil.class.getClassLoader().getResourceAsStream("reports/customer_report_simple.jrxml");
                if (reportStream == null) {
                    throw new Exception("Report template not found: " + REPORT_TEMPLATE);
                }
                System.out.println("DEBUG: Found report template using alternative path");
            } else {
                System.out.println("DEBUG: Found report template successfully");
            }

            try {
                // Use reflection to compile report
                System.out.println("DEBUG: Starting report compilation...");
                Class<?> compileManagerClass = Class.forName("net.sf.jasperreports.engine.JasperCompileManager");
                var compileMethod = compileManagerClass.getMethod("compileReport", InputStream.class);
                compiledReport = compileMethod.invoke(null, reportStream);
                System.out.println("DEBUG: Report compilation successful");
            } catch (Exception e) {
                System.err.println("DEBUG: Report compilation failed: " + e.getMessage());
                if (e.getCause() != null) {
                    System.err.println("DEBUG: Root cause: " + e.getCause().getMessage());
                }
                throw e;
            } finally {
                if (reportStream != null) {
                    reportStream.close();
                }
            }
        }
    }

    /**
     * Generate JasperPrint object from customer data using reflection
     */
    public static Object generateReport(List<Customer> customers) throws Exception {
        compileReport();

        // Convert customers to Map format for easier handling
        List<Map<String, Object>> customerMaps = new ArrayList<>();
        for (Customer customer : customers) {
            Map<String, Object> customerMap = new HashMap<>();
            customerMap.put("idx", customer.getIdx());
            customerMap.put("nik", customer.getNik());
            customerMap.put("name", customer.getName());
            customerMap.put("born", customer.getBorn() != null ? customer.getBorn().toString() : "");
            customerMap.put("active", customer.isActive());
            customerMap.put("salary", customer.getSalary());
            customerMaps.add(customerMap);
        }

        // Create data source using reflection
        Class<?> dataSourceClass = Class.forName("net.sf.jasperreports.engine.data.JRBeanCollectionDataSource");
        var dataSourceConstructor = dataSourceClass.getConstructor(java.util.Collection.class);
        Object dataSource = dataSourceConstructor.newInstance(customerMaps);

        // Create parameters map
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "Customer Management Report");
        parameters.put("GeneratedBy", "Customer Management System");
        parameters.put("TotalRecords", customers.size());

        // Fill the report using reflection
        Class<?> fillManagerClass = Class.forName("net.sf.jasperreports.engine.JasperFillManager");
        var fillMethod = fillManagerClass.getMethod("fillReport",
            Class.forName("net.sf.jasperreports.engine.JasperReport"),
            Map.class,
            Class.forName("net.sf.jasperreports.engine.JRDataSource"));

        return fillMethod.invoke(null, compiledReport, parameters, dataSource);
    }
    
    /**
     * Show print preview by generating PDF and opening with default application
     */
    public static void showPrintPreview(List<Customer> customers, Stage parentStage) {
        System.out.println("DEBUG: JasperReportsUtil.showPrintPreview called with " + customers.size() + " customers");

        try {
            System.out.println("DEBUG: Generating report...");
            Object jasperPrint = generateReport(customers);
            System.out.println("DEBUG: Report generated successfully");

            // Create temporary PDF file
            File tempFile = File.createTempFile("customer_report_preview", ".pdf");
            tempFile.deleteOnExit();
            System.out.println("DEBUG: Temporary file created: " + tempFile.getAbsolutePath());

            // Export to PDF using reflection
            System.out.println("DEBUG: Exporting to PDF...");
            exportToPDFFile(jasperPrint, tempFile);
            System.out.println("DEBUG: PDF export completed");

            // Open PDF with default application
            if (java.awt.Desktop.isDesktopSupported()) {
                System.out.println("DEBUG: Desktop supported, opening PDF...");
                java.awt.Desktop.getDesktop().open(tempFile);
                showInfoAlert("Preview", "PDF Preview Opened",
                             "The report preview has been opened in your default PDF viewer.");
            } else {
                System.out.println("DEBUG: Desktop not supported");
                showInfoAlert("Preview Generated", "PDF file created",
                             "Preview saved to: " + tempFile.getAbsolutePath());
            }

        } catch (Exception e) {
            System.err.println("DEBUG: Error in showPrintPreview: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Report Error", "Failed to generate report preview", e.getMessage());
        }
    }
    
    /**
     * Export report to PDF file
     */
    public static void exportToPDF(List<Customer> customers, Stage parentStage) {
        try {
            Object jasperPrint = generateReport(customers);

            // Show file chooser
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF Report");
            fileChooser.setInitialFileName("customer_report.pdf");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
            );

            File file = fileChooser.showSaveDialog(parentStage);
            if (file != null) {
                exportToPDFFile(jasperPrint, file);

                // Show success message
                showInfoAlert("Export Success", "PDF report has been saved successfully!",
                             "File saved to: " + file.getAbsolutePath());
            }

        } catch (Exception e) {
            showErrorAlert("Export Error", "Failed to export PDF", e.getMessage());
        }
    }

    /**
     * Helper method to export JasperPrint to PDF file using reflection
     */
    private static void exportToPDFFile(Object jasperPrint, File file) throws Exception {
        // Export to PDF using reflection
        Class<?> pdfExporterClass = Class.forName("net.sf.jasperreports.engine.export.JRPdfExporter");
        Object exporter = pdfExporterClass.getDeclaredConstructor().newInstance();

        // Set exporter input
        Class<?> simpleExporterInputClass = Class.forName("net.sf.jasperreports.export.SimpleExporterInput");
        var inputConstructor = simpleExporterInputClass.getConstructor(Class.forName("net.sf.jasperreports.engine.JasperPrint"));
        Object exporterInput = inputConstructor.newInstance(jasperPrint);

        var setExporterInputMethod = pdfExporterClass.getMethod("setExporterInput",
            Class.forName("net.sf.jasperreports.export.ExporterInput"));
        setExporterInputMethod.invoke(exporter, exporterInput);

        // Set exporter output
        Class<?> simpleOutputStreamExporterOutputClass = Class.forName("net.sf.jasperreports.export.SimpleOutputStreamExporterOutput");
        var outputConstructor = simpleOutputStreamExporterOutputClass.getConstructor(java.io.OutputStream.class);
        Object exporterOutput = outputConstructor.newInstance(new FileOutputStream(file));

        var setExporterOutputMethod = pdfExporterClass.getMethod("setExporterOutput",
            Class.forName("net.sf.jasperreports.export.ExporterOutput"));
        setExporterOutputMethod.invoke(exporter, exporterOutput);

        // Export report
        var exportReportMethod = pdfExporterClass.getMethod("exportReport");
        exportReportMethod.invoke(exporter);
    }
    
    /**
     * Print report directly to printer
     */
    public static void printReport(List<Customer> customers) {
        try {
            Object jasperPrint = generateReport(customers);

            // Show confirmation dialog
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Print Confirmation");
            confirmAlert.setHeaderText("Print Customer Report");
            confirmAlert.setContentText("Are you sure you want to print the customer report?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Print the report using reflection
                Class<?> printManagerClass = Class.forName("net.sf.jasperreports.engine.JasperPrintManager");
                var printMethod = printManagerClass.getMethod("printReport",
                    Class.forName("net.sf.jasperreports.engine.JasperPrint"), boolean.class);
                printMethod.invoke(null, jasperPrint, true);

                showInfoAlert("Print Success", "Report sent to printer",
                             "The customer report has been sent to the default printer.");
            }

        } catch (Exception e) {
            showErrorAlert("Print Error", "Failed to print report", e.getMessage());
        }
    }
    
    /**
     * Show error alert dialog
     */
    private static void showErrorAlert(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
    
    /**
     * Show information alert dialog
     */
    private static void showInfoAlert(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }
}
