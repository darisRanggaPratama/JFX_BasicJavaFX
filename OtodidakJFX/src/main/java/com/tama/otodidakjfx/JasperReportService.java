package com.tama.otodidakjfx;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.type.*;
import net.sf.jasperreports.view.JasperViewer;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Service class untuk menangani Jasper Reports
 * 
 * Konsep yang dipelajari:
 * - JasperReports integration dengan JavaFX
 * - Dynamic report generation
 * - Report design programmatically
 * - Export ke berbagai format (PDF, HTML, Excel)
 * - Report viewer integration
 */
public class JasperReportService {

    private static final String REPORTS_PATH = "reports/";
    private static final String OUTPUT_PATH = System.getProperty("user.home") + "/Desktop/";

    /**
     * Generate student registration report
     */
    public void generateStudentReport(Student student) throws JRException {
        // Create report design
        JasperDesign jasperDesign = createStudentReportDesign();
        
        // Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        
        // Create data source
        List<Student> studentList = Arrays.asList(student);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentList);
        
        // Parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "KARTU PENDAFTARAN MAHASISWA");
        parameters.put("UNIVERSITY_NAME", "UNIVERSITAS TEKNOLOGI INDONESIA");
        parameters.put("GENERATED_DATE", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")));
        parameters.put("GENERATED_BY", "Sistem Pendaftaran Mahasiswa");
        
        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
        // Show report viewer
        showReportViewer(jasperPrint, "Kartu Pendaftaran - " + student.getFullName());
        
        // Export to PDF
        exportToPDF(jasperPrint, "student_registration_" + student.getNim());
    }

    /**
     * Generate students list report
     */
    public void generateStudentsListReport(List<Student> students) throws JRException {
        // Create report design
        JasperDesign jasperDesign = createStudentsListReportDesign();
        
        // Compile report
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        
        // Create data source
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(students);
        
        // Parameters
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", "DAFTAR MAHASISWA TERDAFTAR");
        parameters.put("UNIVERSITY_NAME", "UNIVERSITAS TEKNOLOGI INDONESIA");
        parameters.put("GENERATED_DATE", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss")));
        parameters.put("TOTAL_STUDENTS", students.size());
        
        // Fill report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        
        // Show report viewer
        showReportViewer(jasperPrint, "Daftar Mahasiswa");
        
        // Export to PDF
        exportToPDF(jasperPrint, "students_list_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
    }

    /**
     * Create student registration report design
     */
    private JasperDesign createStudentReportDesign() throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("StudentRegistrationReport");
        jasperDesign.setPageWidth(595);
        jasperDesign.setPageHeight(842);
        jasperDesign.setColumnWidth(555);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);

        // Add parameters
        addParameter(jasperDesign, "REPORT_TITLE", String.class);
        addParameter(jasperDesign, "UNIVERSITY_NAME", String.class);
        addParameter(jasperDesign, "GENERATED_DATE", String.class);
        addParameter(jasperDesign, "GENERATED_BY", String.class);

        // Add fields
        addField(jasperDesign, "nim", String.class);
        addField(jasperDesign, "fullName", String.class);
        addField(jasperDesign, "email", String.class);
        addField(jasperDesign, "phone", String.class);
        addField(jasperDesign, "formattedBirthDate", String.class);
        addField(jasperDesign, "gender", String.class);
        addField(jasperDesign, "faculty", String.class);
        addField(jasperDesign, "major", String.class);
        addField(jasperDesign, "entryYear", Integer.class);
        addField(jasperDesign, "gpaString", String.class);
        addField(jasperDesign, "address", String.class);
        addField(jasperDesign, "hobbiesString", String.class);
        addField(jasperDesign, "formattedRegistrationDate", String.class);

        // Create bands
        createStudentReportBands(jasperDesign);

        return jasperDesign;
    }

    /**
     * Create students list report design
     */
    private JasperDesign createStudentsListReportDesign() throws JRException {
        JasperDesign jasperDesign = new JasperDesign();
        jasperDesign.setName("StudentsListReport");
        jasperDesign.setPageWidth(842);
        jasperDesign.setPageHeight(595);
        jasperDesign.setOrientation(OrientationEnum.LANDSCAPE);
        jasperDesign.setColumnWidth(802);
        jasperDesign.setColumnSpacing(0);
        jasperDesign.setLeftMargin(20);
        jasperDesign.setRightMargin(20);
        jasperDesign.setTopMargin(20);
        jasperDesign.setBottomMargin(20);

        // Add parameters
        addParameter(jasperDesign, "REPORT_TITLE", String.class);
        addParameter(jasperDesign, "UNIVERSITY_NAME", String.class);
        addParameter(jasperDesign, "GENERATED_DATE", String.class);
        addParameter(jasperDesign, "TOTAL_STUDENTS", Integer.class);

        // Add fields
        addField(jasperDesign, "nim", String.class);
        addField(jasperDesign, "fullName", String.class);
        addField(jasperDesign, "faculty", String.class);
        addField(jasperDesign, "major", String.class);
        addField(jasperDesign, "entryYear", Integer.class);
        addField(jasperDesign, "gpaString", String.class);
        addField(jasperDesign, "gender", String.class);

        // Create bands
        createStudentsListReportBands(jasperDesign);

        return jasperDesign;
    }

    /**
     * Create bands for student registration report
     */
    private void createStudentReportBands(JasperDesign jasperDesign) throws JRException {
        // Title Band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(100);
        
        // University name
        JRDesignStaticText universityText = new JRDesignStaticText();
        universityText.setX(0);
        universityText.setY(10);
        universityText.setWidth(555);
        universityText.setHeight(25);
        universityText.setText("UNIVERSITAS TEKNOLOGI INDONESIA");
        universityText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        universityText.setFontSize(16f);
        universityText.setBold(true);
        titleBand.addElement(universityText);
        
        // Report title
        JRDesignTextField titleField = new JRDesignTextField();
        titleField.setX(0);
        titleField.setY(40);
        titleField.setWidth(555);
        titleField.setHeight(25);
        titleField.setExpression(new JRDesignExpression("$P{REPORT_TITLE}"));
        titleField.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleField.setFontSize(14f);
        titleField.setBold(true);
        titleBand.addElement(titleField);
        
        jasperDesign.setTitle(titleBand);

        // Detail Band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(400);
        
        int yPos = 20;
        int lineHeight = 20;
        
        // Add student details
        addDetailField(detailBand, "NIM:", "$F{nim}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Nama Lengkap:", "$F{fullName}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Email:", "$F{email}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Telepon:", "$F{phone}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Tanggal Lahir:", "$F{formattedBirthDate}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Jenis Kelamin:", "$F{gender}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Fakultas:", "$F{faculty}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Program Studi:", "$F{major}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Tahun Masuk:", "$F{entryYear}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "IPK:", "$F{gpaString}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Alamat:", "$F{address}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Hobi:", "$F{hobbiesString}", 0, yPos, 100, 555);
        yPos += lineHeight;
        
        addDetailField(detailBand, "Tanggal Daftar:", "$F{formattedRegistrationDate}", 0, yPos, 100, 555);
        
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);

        // Page Footer
        JRDesignBand pageFooterBand = new JRDesignBand();
        pageFooterBand.setHeight(30);
        
        JRDesignTextField generatedDate = new JRDesignTextField();
        generatedDate.setX(0);
        generatedDate.setY(5);
        generatedDate.setWidth(555);
        generatedDate.setHeight(15);
        generatedDate.setExpression(new JRDesignExpression("\"Generated on: \" + $P{GENERATED_DATE}"));
        generatedDate.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        generatedDate.setFontSize(8f);
        pageFooterBand.addElement(generatedDate);
        
        jasperDesign.setPageFooter(pageFooterBand);
    }

    /**
     * Create bands for students list report
     */
    private void createStudentsListReportBands(JasperDesign jasperDesign) throws JRException {
        // Title Band
        JRDesignBand titleBand = new JRDesignBand();
        titleBand.setHeight(80);
        
        JRDesignStaticText titleText = new JRDesignStaticText();
        titleText.setX(0);
        titleText.setY(10);
        titleText.setWidth(802);
        titleText.setHeight(25);
        titleText.setText("DAFTAR MAHASISWA TERDAFTAR");
        titleText.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
        titleText.setFontSize(16f);
        titleText.setBold(true);
        titleBand.addElement(titleText);
        
        jasperDesign.setTitle(titleBand);

        // Column Header
        JRDesignBand columnHeaderBand = new JRDesignBand();
        columnHeaderBand.setHeight(25);
        
        String[] headers = {"NIM", "Nama Lengkap", "Fakultas", "Program Studi", "Tahun", "IPK", "Gender"};
        int[] widths = {80, 150, 120, 150, 60, 60, 80};
        int xPos = 0;
        
        for (int i = 0; i < headers.length; i++) {
            JRDesignStaticText header = new JRDesignStaticText();
            header.setX(xPos);
            header.setY(0);
            header.setWidth(widths[i]);
            header.setHeight(25);
            header.setText(headers[i]);
            header.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            header.setBold(true);
            header.setBackcolor(Color.LIGHT_GRAY);
            header.setMode(ModeEnum.OPAQUE);
            columnHeaderBand.addElement(header);
            xPos += widths[i];
        }
        
        jasperDesign.setColumnHeader(columnHeaderBand);

        // Detail Band
        JRDesignBand detailBand = new JRDesignBand();
        detailBand.setHeight(20);
        
        String[] fields = {"$F{nim}", "$F{fullName}", "$F{faculty}", "$F{major}", "$F{entryYear}", "$F{gpaString}", "$F{gender}"};
        xPos = 0;
        
        for (int i = 0; i < fields.length; i++) {
            JRDesignTextField field = new JRDesignTextField();
            field.setX(xPos);
            field.setY(0);
            field.setWidth(widths[i]);
            field.setHeight(20);
            field.setExpression(new JRDesignExpression(fields[i]));
            field.setHorizontalTextAlign(HorizontalTextAlignEnum.CENTER);
            detailBand.addElement(field);
            xPos += widths[i];
        }
        
        ((JRDesignSection) jasperDesign.getDetailSection()).addBand(detailBand);
    }

    // Helper methods
    private void addParameter(JasperDesign jasperDesign, String name, Class<?> type) throws JRException {
        JRDesignParameter parameter = new JRDesignParameter();
        parameter.setName(name);
        parameter.setValueClass(type);
        jasperDesign.addParameter(parameter);
    }

    private void addField(JasperDesign jasperDesign, String name, Class<?> type) throws JRException {
        JRDesignField field = new JRDesignField();
        field.setName(name);
        field.setValueClass(type);
        jasperDesign.addField(field);
    }

    private void addDetailField(JRDesignBand band, String label, String expression, int x, int y, int labelWidth, int totalWidth) {
        // Label
        JRDesignStaticText labelText = new JRDesignStaticText();
        labelText.setX(x);
        labelText.setY(y);
        labelText.setWidth(labelWidth);
        labelText.setHeight(15);
        labelText.setText(label);
        labelText.setBold(true);
        band.addElement(labelText);
        
        // Value
        JRDesignTextField valueField = new JRDesignTextField();
        valueField.setX(x + labelWidth + 10);
        valueField.setY(y);
        valueField.setWidth(totalWidth - labelWidth - 10);
        valueField.setHeight(15);
        valueField.setExpression(new JRDesignExpression(expression));
        band.addElement(valueField);
    }

    private void showReportViewer(JasperPrint jasperPrint, String title) {
        // Show in separate thread to avoid blocking JavaFX
        new Thread(() -> {
            JasperViewer viewer = new JasperViewer(jasperPrint, false);
            viewer.setTitle(title);
            viewer.setVisible(true);
        }).start();
    }

    private void exportToPDF(JasperPrint jasperPrint, String fileName) {
        try {
            String outputFile = OUTPUT_PATH + fileName + ".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile);
            System.out.println("Report exported to: " + outputFile);
        } catch (JRException e) {
            System.err.println("Error exporting to PDF: " + e.getMessage());
        }
    }

    public void exportToHTML(JasperPrint jasperPrint, String fileName) {
        try {
            String outputFile = OUTPUT_PATH + fileName + ".html";
            JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile);
            System.out.println("Report exported to: " + outputFile);
        } catch (JRException e) {
            System.err.println("Error exporting to HTML: " + e.getMessage());
        }
    }
}
