package com.tama.otodidakjfx;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import net.sf.jasperreports.engine.JRException;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Controller untuk Student Form FXML
 * 
 * Konsep yang dipelajari:
 * - Form validation dengan real-time feedback
 * - ComboBox dengan dependent data
 * - DatePicker dan Spinner controls
 * - TextArea dan ListView
 * - Complex form handling
 * - Data binding dan validation
 */
public class StudentFormController implements Initializable {

    // Personal Information Fields
    @FXML private TextField nimField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private DatePicker birthDatePicker;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    
    // Academic Information Fields
    @FXML private ComboBox<String> facultyComboBox;
    @FXML private ComboBox<String> majorComboBox;
    @FXML private Spinner<Integer> entryYearSpinner;
    @FXML private TextField gpaField;
    
    // Additional Information Fields
    @FXML private TextArea addressArea;
    @FXML private ListView<String> hobbiesListView;
    @FXML private CheckBox agreementCheckBox;
    
    // Error Labels
    @FXML private Label nimErrorLabel;
    @FXML private Label nameErrorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label phoneErrorLabel;
    @FXML private Label birthDateErrorLabel;
    @FXML private Label genderErrorLabel;
    @FXML private Label facultyErrorLabel;
    @FXML private Label majorErrorLabel;
    @FXML private Label entryYearErrorLabel;
    @FXML private Label gpaErrorLabel;
    @FXML private Label addressErrorLabel;
    @FXML private Label agreementErrorLabel;
    
    // Buttons
    @FXML private Button submitButton;

    // Data structures
    private ToggleGroup genderGroup;
    private Map<String, String[]> facultyMajorMap;
    private JasperReportService reportService;
    private List<Student> registeredStudents;
    
    // Validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile("^08\\d{8,11}$");
    private static final Pattern NIM_PATTERN = Pattern.compile("^\\d{8}$");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGenderRadioButtons();
        setupFacultyAndMajor();
        setupEntryYearSpinner();
        setupHobbiesList();
        setupValidationListeners();
        clearErrorLabels();

        // Initialize Jasper Reports service and student list
        reportService = new JasperReportService();
        registeredStudents = new ArrayList<>();

        System.out.println("Student Form Controller initialized!");
    }

    private void setupGenderRadioButtons() {
        genderGroup = new ToggleGroup();
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);
    }

    private void setupFacultyAndMajor() {
        // Initialize faculty-major mapping
        facultyMajorMap = new HashMap<>();
        facultyMajorMap.put("Teknik", new String[]{
            "Teknik Informatika", "Teknik Elektro", "Teknik Mesin", "Teknik Sipil"
        });
        facultyMajorMap.put("Ekonomi", new String[]{
            "Manajemen", "Akuntansi", "Ekonomi Pembangunan"
        });
        facultyMajorMap.put("Hukum", new String[]{
            "Ilmu Hukum"
        });
        facultyMajorMap.put("Kedokteran", new String[]{
            "Pendidikan Dokter", "Kedokteran Gigi", "Farmasi"
        });
        facultyMajorMap.put("Sastra", new String[]{
            "Sastra Indonesia", "Sastra Inggris", "Sastra Jepang"
        });

        // Populate faculty combo box
        facultyComboBox.setItems(FXCollections.observableArrayList(facultyMajorMap.keySet()));
        
        // Add listener for faculty selection
        facultyComboBox.setOnAction(e -> {
            String selectedFaculty = facultyComboBox.getValue();
            if (selectedFaculty != null) {
                String[] majors = facultyMajorMap.get(selectedFaculty);
                majorComboBox.setItems(FXCollections.observableArrayList(majors));
                majorComboBox.setValue(null); // Clear previous selection
            }
        });
    }

    private void setupEntryYearSpinner() {
        int currentYear = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> valueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, currentYear, currentYear);
        entryYearSpinner.setValueFactory(valueFactory);
    }

    private void setupHobbiesList() {
        hobbiesListView.setItems(FXCollections.observableArrayList(
            "Membaca", "Olahraga", "Musik", "Gaming", "Traveling", 
            "Fotografi", "Memasak", "Menggambar", "Programming", "Menulis"
        ));
        hobbiesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void setupValidationListeners() {
        // Real-time validation
        nimField.textProperty().addListener((obs, oldVal, newVal) -> validateNIM());
        nameField.textProperty().addListener((obs, oldVal, newVal) -> validateName());
        emailField.textProperty().addListener((obs, oldVal, newVal) -> validateEmail());
        phoneField.textProperty().addListener((obs, oldVal, newVal) -> validatePhone());
        gpaField.textProperty().addListener((obs, oldVal, newVal) -> validateGPA());
    }

    @FXML
    private void handleValidate(ActionEvent event) {
        boolean isValid = validateAllFields();
        
        if (isValid) {
            showAlert("Validasi Berhasil", "Semua field telah diisi dengan benar!", AlertType.INFORMATION);
        } else {
            showAlert("Validasi Gagal", "Masih ada field yang belum diisi dengan benar. Periksa kembali form Anda.", AlertType.WARNING);
        }
    }

    @FXML
    private void handleSubmit(ActionEvent event) {
        if (validateAllFields()) {
            // Create Student object
            Student student = createStudentFromForm();

            // Add to registered students list
            registeredStudents.add(student);

            // Show success dialog with options
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Pendaftaran Berhasil");
            successAlert.setHeaderText("Data mahasiswa berhasil disimpan!");
            successAlert.setContentText("Apa yang ingin Anda lakukan selanjutnya?");

            ButtonType printCardButton = new ButtonType("🖨️ Cetak Kartu");
            ButtonType viewListButton = new ButtonType("📋 Lihat Daftar");
            ButtonType continueButton = new ButtonType("➕ Daftar Lagi");
            ButtonType closeButton = new ButtonType("❌ Tutup", ButtonBar.ButtonData.CANCEL_CLOSE);

            successAlert.getButtonTypes().setAll(printCardButton, viewListButton, continueButton, closeButton);

            Optional<ButtonType> result = successAlert.showAndWait();

            if (result.isPresent()) {
                if (result.get() == printCardButton) {
                    generateStudentCard(student);
                } else if (result.get() == viewListButton) {
                    generateStudentsList();
                } else if (result.get() == continueButton) {
                    handleClear(null);
                }
            }

        } else {
            showAlert("Submit Gagal", "Lengkapi semua field yang diperlukan terlebih dahulu!", AlertType.ERROR);
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi");
        confirmAlert.setHeaderText("Clear Form");
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus semua data yang telah diinput?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            clearAllFields();
            clearErrorLabels();
            showAlert("Form Cleared", "Semua data telah dihapus!", AlertType.INFORMATION);
        }
    }

    @FXML
    private void handlePreview(ActionEvent event) {
        // Create preview window or dialog
        StringBuilder preview = new StringBuilder();
        preview.append("=== PREVIEW DATA ===\n\n");
        
        preview.append("NIM: ").append(nimField.getText().isEmpty() ? "[Belum diisi]" : nimField.getText()).append("\n");
        preview.append("Nama: ").append(nameField.getText().isEmpty() ? "[Belum diisi]" : nameField.getText()).append("\n");
        preview.append("Email: ").append(emailField.getText().isEmpty() ? "[Belum diisi]" : emailField.getText()).append("\n");
        preview.append("Telepon: ").append(phoneField.getText().isEmpty() ? "[Belum diisi]" : phoneField.getText()).append("\n");
        preview.append("Tanggal Lahir: ").append(birthDatePicker.getValue() == null ? "[Belum diisi]" : birthDatePicker.getValue().toString()).append("\n");
        preview.append("Jenis Kelamin: ").append(getSelectedGender().isEmpty() ? "[Belum dipilih]" : getSelectedGender()).append("\n");
        preview.append("Fakultas: ").append(facultyComboBox.getValue() == null ? "[Belum dipilih]" : facultyComboBox.getValue()).append("\n");
        preview.append("Program Studi: ").append(majorComboBox.getValue() == null ? "[Belum dipilih]" : majorComboBox.getValue()).append("\n");
        preview.append("Tahun Masuk: ").append(entryYearSpinner.getValue()).append("\n");
        preview.append("IPK: ").append(gpaField.getText().isEmpty() ? "[Belum diisi]" : gpaField.getText()).append("\n");
        
        Alert previewAlert = new Alert(AlertType.INFORMATION);
        previewAlert.setTitle("Preview Data");
        previewAlert.setHeaderText("Preview Data yang Akan Disubmit");
        previewAlert.setContentText(preview.toString());
        previewAlert.getDialogPane().setPrefWidth(400);
        previewAlert.showAndWait();
    }

    // Validation methods
    private boolean validateNIM() {
        String nim = nimField.getText();
        if (nim.isEmpty()) {
            setErrorLabel(nimErrorLabel, "NIM wajib diisi");
            return false;
        } else if (!NIM_PATTERN.matcher(nim).matches()) {
            setErrorLabel(nimErrorLabel, "NIM harus 8 digit angka");
            return false;
        } else {
            clearErrorLabel(nimErrorLabel);
            return true;
        }
    }

    private boolean validateName() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            setErrorLabel(nameErrorLabel, "Nama wajib diisi");
            return false;
        } else if (name.length() < 2) {
            setErrorLabel(nameErrorLabel, "Nama minimal 2 karakter");
            return false;
        } else {
            clearErrorLabel(nameErrorLabel);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            setErrorLabel(emailErrorLabel, "Email wajib diisi");
            return false;
        } else if (!EMAIL_PATTERN.matcher(email).matches()) {
            setErrorLabel(emailErrorLabel, "Format email tidak valid");
            return false;
        } else {
            clearErrorLabel(emailErrorLabel);
            return true;
        }
    }

    private boolean validatePhone() {
        String phone = phoneField.getText().trim();
        if (phone.isEmpty()) {
            setErrorLabel(phoneErrorLabel, "Telepon wajib diisi");
            return false;
        } else if (!PHONE_PATTERN.matcher(phone).matches()) {
            setErrorLabel(phoneErrorLabel, "Format: 08xxxxxxxxxx");
            return false;
        } else {
            clearErrorLabel(phoneErrorLabel);
            return true;
        }
    }

    private boolean validateGPA() {
        String gpaText = gpaField.getText().trim();
        if (gpaText.isEmpty()) {
            setErrorLabel(gpaErrorLabel, "IPK wajib diisi");
            return false;
        }
        
        try {
            double gpa = Double.parseDouble(gpaText);
            if (gpa < 0.0 || gpa > 4.0) {
                setErrorLabel(gpaErrorLabel, "IPK harus 0.00 - 4.00");
                return false;
            } else {
                clearErrorLabel(gpaErrorLabel);
                return true;
            }
        } catch (NumberFormatException e) {
            setErrorLabel(gpaErrorLabel, "IPK harus berupa angka");
            return false;
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;
        
        isValid &= validateNIM();
        isValid &= validateName();
        isValid &= validateEmail();
        isValid &= validatePhone();
        isValid &= validateGPA();
        
        // Birth date validation
        if (birthDatePicker.getValue() == null) {
            setErrorLabel(birthDateErrorLabel, "Tanggal lahir wajib dipilih");
            isValid = false;
        } else {
            clearErrorLabel(birthDateErrorLabel);
        }
        
        // Gender validation
        if (genderGroup.getSelectedToggle() == null) {
            setErrorLabel(genderErrorLabel, "Jenis kelamin wajib dipilih");
            isValid = false;
        } else {
            clearErrorLabel(genderErrorLabel);
        }
        
        // Faculty validation
        if (facultyComboBox.getValue() == null) {
            setErrorLabel(facultyErrorLabel, "Fakultas wajib dipilih");
            isValid = false;
        } else {
            clearErrorLabel(facultyErrorLabel);
        }
        
        // Major validation
        if (majorComboBox.getValue() == null) {
            setErrorLabel(majorErrorLabel, "Program studi wajib dipilih");
            isValid = false;
        } else {
            clearErrorLabel(majorErrorLabel);
        }
        
        // Address validation
        if (addressArea.getText().trim().isEmpty()) {
            setErrorLabel(addressErrorLabel, "Alamat wajib diisi");
            isValid = false;
        } else {
            clearErrorLabel(addressErrorLabel);
        }
        
        // Agreement validation
        if (!agreementCheckBox.isSelected()) {
            setErrorLabel(agreementErrorLabel, "Harus menyetujui syarat dan ketentuan");
            isValid = false;
        } else {
            clearErrorLabel(agreementErrorLabel);
        }
        
        return isValid;
    }

    // Helper methods
    private String getSelectedGender() {
        if (maleRadio.isSelected()) return "Laki-laki";
        if (femaleRadio.isSelected()) return "Perempuan";
        return "";
    }

    private void setErrorLabel(Label label, String message) {
        label.setText("⚠ " + message);
        label.setVisible(true);
    }

    private void clearErrorLabel(Label label) {
        label.setText("");
        label.setVisible(false);
    }

    private void clearErrorLabels() {
        Label[] errorLabels = {
            nimErrorLabel, nameErrorLabel, emailErrorLabel, phoneErrorLabel,
            birthDateErrorLabel, genderErrorLabel, facultyErrorLabel, majorErrorLabel,
            entryYearErrorLabel, gpaErrorLabel, addressErrorLabel, agreementErrorLabel
        };
        
        for (Label label : errorLabels) {
            clearErrorLabel(label);
        }
    }

    private void clearAllFields() {
        nimField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        birthDatePicker.setValue(null);
        genderGroup.selectToggle(null);
        facultyComboBox.setValue(null);
        majorComboBox.setValue(null);
        entryYearSpinner.getValueFactory().setValue(LocalDate.now().getYear());
        gpaField.clear();
        addressArea.clear();
        hobbiesListView.getSelectionModel().clearSelection();
        agreementCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Create Student object from form data
     */
    private Student createStudentFromForm() {
        Student student = new Student();
        student.setNim(nimField.getText().trim());

        // Split full name into first and last name
        String fullName = nameField.getText().trim();
        String[] nameParts = fullName.split(" ", 2);
        student.setFirstName(nameParts[0]);
        student.setLastName(nameParts.length > 1 ? nameParts[1] : "");

        student.setEmail(emailField.getText().trim());
        student.setPhone(phoneField.getText().trim());
        student.setBirthDate(birthDatePicker.getValue());
        student.setGender(getSelectedGender());
        student.setFaculty(facultyComboBox.getValue());
        student.setMajor(majorComboBox.getValue());
        student.setEntryYear(entryYearSpinner.getValue());
        student.setGpa(Double.parseDouble(gpaField.getText().trim()));
        student.setAddress(addressArea.getText().trim());

        // Get selected hobbies
        var selectedHobbies = hobbiesListView.getSelectionModel().getSelectedItems();
        student.setHobbies(new ArrayList<>(selectedHobbies));

        student.setAgreementAccepted(agreementCheckBox.isSelected());
        student.setRegistrationDate(LocalDate.now());

        return student;
    }

    /**
     * Generate student registration card using Jasper Reports
     */
    private void generateStudentCard(Student student) {
        try {
            showAlert("Generating Report", "Sedang membuat kartu pendaftaran...", AlertType.INFORMATION);
            reportService.generateStudentReport(student);

        } catch (JRException e) {
            showAlert("Error", "Gagal membuat kartu pendaftaran: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Generate students list report using Jasper Reports
     */
    private void generateStudentsList() {
        if (registeredStudents.isEmpty()) {
            showAlert("No Data", "Belum ada mahasiswa yang terdaftar!", AlertType.WARNING);
            return;
        }

        try {
            showAlert("Generating Report", "Sedang membuat daftar mahasiswa...", AlertType.INFORMATION);
            reportService.generateStudentsListReport(registeredStudents);

        } catch (JRException e) {
            showAlert("Error", "Gagal membuat daftar mahasiswa: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Add method to handle report generation from menu
     */
    @FXML
    private void handleGenerateReports(ActionEvent event) {
        if (registeredStudents.isEmpty()) {
            showAlert("No Data", "Belum ada mahasiswa yang terdaftar!\nSilakan daftarkan mahasiswa terlebih dahulu.", AlertType.WARNING);
            return;
        }

        Alert reportAlert = new Alert(AlertType.CONFIRMATION);
        reportAlert.setTitle("Generate Reports");
        reportAlert.setHeaderText("Pilih jenis laporan yang ingin dibuat:");
        reportAlert.setContentText("Total mahasiswa terdaftar: " + registeredStudents.size());

        ButtonType studentListButton = new ButtonType("📋 Daftar Mahasiswa");
        ButtonType lastStudentButton = new ButtonType("🎓 Kartu Mahasiswa Terakhir");
        ButtonType cancelButton = new ButtonType("❌ Batal", ButtonBar.ButtonData.CANCEL_CLOSE);

        reportAlert.getButtonTypes().setAll(studentListButton, lastStudentButton, cancelButton);

        Optional<ButtonType> result = reportAlert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == studentListButton) {
                generateStudentsList();
            } else if (result.get() == lastStudentButton && !registeredStudents.isEmpty()) {
                Student lastStudent = registeredStudents.get(registeredStudents.size() - 1);
                generateStudentCard(lastStudent);
            }
        }
    }
}
