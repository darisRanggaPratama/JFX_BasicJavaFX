package com.tama.otodidakjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Contoh 3: Controls Dasar dan Event Handling
 * 
 * Konsep yang dipelajari:
 * - TextField, TextArea
 * - Button, CheckBox, RadioButton
 * - ComboBox, ListView
 * - Event Handling
 * - User Input Validation
 */
public class ControlsAndEventsApp extends Application {

    private TextField nameField;
    private TextArea outputArea;
    private CheckBox agreeCheckBox;
    private RadioButton maleRadio, femaleRadio;
    private ComboBox<String> cityComboBox;
    private ListView<String> hobbiesList;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Title
        Label title = new Label("Form Input dengan Berbagai Controls");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Create form
        GridPane form = createForm();
        
        // Output area
        outputArea = new TextArea();
        outputArea.setPrefRowCount(8);
        outputArea.setEditable(false);
        outputArea.setPromptText("Output akan ditampilkan di sini...");

        // Buttons
        Button submitButton = new Button("Submit");
        Button clearButton = new Button("Clear");
        
        submitButton.setOnAction(e -> handleSubmit());
        clearButton.setOnAction(e -> handleClear());

        root.getChildren().addAll(title, form, outputArea, 
                                 new VBox(10, submitButton, clearButton));

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Belajar JavaFX - Controls dan Events");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createForm() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        int row = 0;

        // Name field
        grid.add(new Label("Nama:"), 0, row);
        nameField = new TextField();
        nameField.setPromptText("Masukkan nama Anda");
        grid.add(nameField, 1, row++);

        // Gender radio buttons
        grid.add(new Label("Jenis Kelamin:"), 0, row);
        ToggleGroup genderGroup = new ToggleGroup();
        maleRadio = new RadioButton("Laki-laki");
        femaleRadio = new RadioButton("Perempuan");
        maleRadio.setToggleGroup(genderGroup);
        femaleRadio.setToggleGroup(genderGroup);
        maleRadio.setSelected(true); // default selection
        
        VBox genderBox = new VBox(5, maleRadio, femaleRadio);
        grid.add(genderBox, 1, row++);

        // City combo box
        grid.add(new Label("Kota:"), 0, row);
        cityComboBox = new ComboBox<>();
        cityComboBox.getItems().addAll("Jakarta", "Bandung", "Surabaya", 
                                      "Medan", "Semarang", "Yogyakarta");
        cityComboBox.setPromptText("Pilih kota");
        grid.add(cityComboBox, 1, row++);

        // Hobbies list
        grid.add(new Label("Hobi:"), 0, row);
        hobbiesList = new ListView<>();
        hobbiesList.getItems().addAll("Membaca", "Olahraga", "Musik", 
                                     "Gaming", "Traveling", "Fotografi");
        hobbiesList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        hobbiesList.setPrefHeight(100);
        grid.add(hobbiesList, 1, row++);

        // Agreement checkbox
        agreeCheckBox = new CheckBox("Saya setuju dengan syarat dan ketentuan");
        grid.add(agreeCheckBox, 0, row, 2, 1);

        return grid;
    }

    private void handleSubmit() {
        StringBuilder result = new StringBuilder();
        result.append("=== DATA YANG DIINPUT ===\n\n");

        // Validate name
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showAlert("Error", "Nama tidak boleh kosong!");
            return;
        }
        result.append("Nama: ").append(name).append("\n");

        // Gender
        String gender = maleRadio.isSelected() ? "Laki-laki" : "Perempuan";
        result.append("Jenis Kelamin: ").append(gender).append("\n");

        // City
        String city = cityComboBox.getValue();
        if (city == null) {
            showAlert("Error", "Silakan pilih kota!");
            return;
        }
        result.append("Kota: ").append(city).append("\n");

        // Hobbies
        var selectedHobbies = hobbiesList.getSelectionModel().getSelectedItems();
        if (selectedHobbies.isEmpty()) {
            result.append("Hobi: Tidak ada yang dipilih\n");
        } else {
            result.append("Hobi: ").append(String.join(", ", selectedHobbies)).append("\n");
        }

        // Agreement
        if (!agreeCheckBox.isSelected()) {
            showAlert("Error", "Anda harus menyetujui syarat dan ketentuan!");
            return;
        }
        result.append("Persetujuan: Ya\n");

        result.append("\n✅ Data berhasil disubmit!");
        outputArea.setText(result.toString());
    }

    private void handleClear() {
        nameField.clear();
        maleRadio.setSelected(true);
        cityComboBox.setValue(null);
        hobbiesList.getSelectionModel().clearSelection();
        agreeCheckBox.setSelected(false);
        outputArea.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
