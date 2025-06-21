package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Contoh 5: TableView dan Data Management
 * 
 * Konsep yang dipelajari:
 * - TableView dan TableColumn
 * - ObservableList untuk data binding
 * - CRUD operations (Create, Read, Update, Delete)
 * - Data validation
 * - Selection handling
 */
public class TableViewApp extends Application {

    private TableView<Person> tableView;
    private ObservableList<Person> personData;
    
    // Form fields
    private TextField idField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField ageField;
    private ComboBox<String> cityComboBox;
    
    // Buttons
    private Button addButton;
    private Button updateButton;
    private Button deleteButton;
    private Button clearButton;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Title
        Label title = new Label("Manajemen Data Person dengan TableView");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        VBox topContainer = new VBox(10);
        topContainer.setAlignment(Pos.CENTER);
        topContainer.getChildren().add(title);
        root.setTop(topContainer);

        // Create table
        createTableView();
        root.setCenter(tableView);

        // Create form
        VBox formContainer = createForm();
        root.setRight(formContainer);

        // Initialize data
        initializeData();

        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setTitle("Belajar JavaFX - TableView");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createTableView() {
        tableView = new TableView<>();
        
        // ID Column
        TableColumn<Person, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);

        // First Name Column
        TableColumn<Person, String> firstNameColumn = new TableColumn<>("Nama Depan");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameColumn.setPrefWidth(120);

        // Last Name Column
        TableColumn<Person, String> lastNameColumn = new TableColumn<>("Nama Belakang");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameColumn.setPrefWidth(120);

        // Email Column
        TableColumn<Person, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(180);

        // Age Column
        TableColumn<Person, Integer> ageColumn = new TableColumn<>("Umur");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageColumn.setPrefWidth(60);

        // City Column
        TableColumn<Person, String> cityColumn = new TableColumn<>("Kota");
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        cityColumn.setPrefWidth(100);

        tableView.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, 
                                     emailColumn, ageColumn, cityColumn);

        // Selection listener
        tableView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    populateForm(newValue);
                }
            }
        );
    }

    private VBox createForm() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.setPrefWidth(300);
        formBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 10px;");

        Label formTitle = new Label("Form Data Person");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Create form fields
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        int row = 0;

        // ID Field (read-only)
        formGrid.add(new Label("ID:"), 0, row);
        idField = new TextField();
        idField.setEditable(false);
        idField.setStyle("-fx-background-color: #e9ecef;");
        formGrid.add(idField, 1, row++);

        // First Name
        formGrid.add(new Label("Nama Depan:"), 0, row);
        firstNameField = new TextField();
        firstNameField.setPromptText("Masukkan nama depan");
        formGrid.add(firstNameField, 1, row++);

        // Last Name
        formGrid.add(new Label("Nama Belakang:"), 0, row);
        lastNameField = new TextField();
        lastNameField.setPromptText("Masukkan nama belakang");
        formGrid.add(lastNameField, 1, row++);

        // Email
        formGrid.add(new Label("Email:"), 0, row);
        emailField = new TextField();
        emailField.setPromptText("contoh@email.com");
        formGrid.add(emailField, 1, row++);

        // Age
        formGrid.add(new Label("Umur:"), 0, row);
        ageField = new TextField();
        ageField.setPromptText("Masukkan umur");
        formGrid.add(ageField, 1, row++);

        // City
        formGrid.add(new Label("Kota:"), 0, row);
        cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll("Jakarta", "Bandung", "Surabaya", 
                                      "Medan", "Semarang", "Yogyakarta", "Malang");
        cityComboBox.setPromptText("Pilih kota");
        formGrid.add(cityComboBox, 1, row++);

        // Buttons
        HBox buttonBox = createButtonBox();

        formBox.getChildren().addAll(formTitle, formGrid, buttonBox);
        return formBox;
    }

    private HBox createButtonBox() {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);

        addButton = new Button("Tambah");
        updateButton = new Button("Update");
        deleteButton = new Button("Hapus");
        clearButton = new Button("Clear");

        addButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white;");
        clearButton.setStyle("-fx-background-color: #6c757d; -fx-text-fill: white;");

        // Event handlers
        addButton.setOnAction(e -> handleAdd());
        updateButton.setOnAction(e -> handleUpdate());
        deleteButton.setOnAction(e -> handleDelete());
        clearButton.setOnAction(e -> handleClear());

        buttonBox.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        return buttonBox;
    }

    private void initializeData() {
        personData = FXCollections.observableArrayList();
        
        // Sample data
        personData.addAll(
            new Person(1, "John", "Doe", "john.doe@email.com", 25, "Jakarta"),
            new Person(2, "Jane", "Smith", "jane.smith@email.com", 30, "Bandung"),
            new Person(3, "Bob", "Johnson", "bob.johnson@email.com", 35, "Surabaya"),
            new Person(4, "Alice", "Brown", "alice.brown@email.com", 28, "Medan"),
            new Person(5, "Charlie", "Wilson", "charlie.wilson@email.com", 32, "Semarang")
        );
        
        tableView.setItems(personData);
    }

    private void populateForm(Person person) {
        idField.setText(String.valueOf(person.getId()));
        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        emailField.setText(person.getEmail());
        ageField.setText(String.valueOf(person.getAge()));
        cityComboBox.setValue(person.getCity());
    }

    private void handleAdd() {
        if (validateForm()) {
            int newId = getNextId();
            Person newPerson = new Person(
                newId,
                firstNameField.getText().trim(),
                lastNameField.getText().trim(),
                emailField.getText().trim(),
                Integer.parseInt(ageField.getText().trim()),
                cityComboBox.getValue()
            );
            
            personData.add(newPerson);
            handleClear();
            showAlert("Sukses", "Data berhasil ditambahkan!", Alert.AlertType.INFORMATION);
        }
    }

    private void handleUpdate() {
        Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            showAlert("Error", "Pilih data yang akan diupdate!", Alert.AlertType.WARNING);
            return;
        }

        if (validateForm()) {
            selectedPerson.setFirstName(firstNameField.getText().trim());
            selectedPerson.setLastName(lastNameField.getText().trim());
            selectedPerson.setEmail(emailField.getText().trim());
            selectedPerson.setAge(Integer.parseInt(ageField.getText().trim()));
            selectedPerson.setCity(cityComboBox.getValue());
            
            tableView.refresh();
            showAlert("Sukses", "Data berhasil diupdate!", Alert.AlertType.INFORMATION);
        }
    }

    private void handleDelete() {
        Person selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson == null) {
            showAlert("Error", "Pilih data yang akan dihapus!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Konfirmasi");
        confirmAlert.setHeaderText("Hapus Data");
        confirmAlert.setContentText("Apakah Anda yakin ingin menghapus data " + 
                                   selectedPerson.getFullName() + "?");

        if (confirmAlert.showAndWait().get() == ButtonType.OK) {
            personData.remove(selectedPerson);
            handleClear();
            showAlert("Sukses", "Data berhasil dihapus!", Alert.AlertType.INFORMATION);
        }
    }

    private void handleClear() {
        idField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        ageField.clear();
        cityComboBox.setValue(null);
        tableView.getSelectionModel().clearSelection();
    }

    private boolean validateForm() {
        if (firstNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Nama depan tidak boleh kosong!", Alert.AlertType.WARNING);
            return false;
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            showAlert("Error", "Nama belakang tidak boleh kosong!", Alert.AlertType.WARNING);
            return false;
        }
        
        if (emailField.getText().trim().isEmpty()) {
            showAlert("Error", "Email tidak boleh kosong!", Alert.AlertType.WARNING);
            return false;
        }
        
        try {
            int age = Integer.parseInt(ageField.getText().trim());
            if (age < 1 || age > 150) {
                showAlert("Error", "Umur harus antara 1-150!", Alert.AlertType.WARNING);
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Umur harus berupa angka!", Alert.AlertType.WARNING);
            return false;
        }
        
        if (cityComboBox.getValue() == null) {
            showAlert("Error", "Pilih kota!", Alert.AlertType.WARNING);
            return false;
        }
        
        return true;
    }

    private int getNextId() {
        return personData.stream()
                .mapToInt(Person::getId)
                .max()
                .orElse(0) + 1;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
