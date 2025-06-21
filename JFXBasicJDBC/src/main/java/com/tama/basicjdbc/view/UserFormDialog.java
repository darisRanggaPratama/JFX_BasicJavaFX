package com.tama.basicjdbc.view;

import com.tama.basicjdbc.model.Users;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class UserFormDialog extends Dialog<Users> {
    private TextField txtName;
    private TextField txtAddress;
    private TextField txtPhone;
    private final Users user;
    private Label errorLabel;

    public UserFormDialog(Users user, String title) {
        this.user = user;
        setTitle(title);
        
        // Set the button types
        ButtonType saveButtonType = new ButtonType("Simpan", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Create the form grid
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        txtName = new TextField();
        txtName.setPromptText("Nama");
        txtAddress = new TextField();
        txtAddress.setPromptText("Alamat");
        txtPhone = new TextField();
        txtPhone.setPromptText("Telepon");
        
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);

        // Fill fields with user data if editing
        if (user != null) {
            txtName.setText(user.getName());
            txtAddress.setText(user.getAddress());
            txtPhone.setText(user.getPhone());
        }

        grid.add(new Label("Nama:"), 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(new Label("Alamat:"), 0, 1);
        grid.add(txtAddress, 1, 1);
        grid.add(new Label("Telepon:"), 0, 2);
        grid.add(txtPhone, 1, 2);
        grid.add(errorLabel, 0, 3, 2, 1);

        getDialogPane().setContent(grid);

        // Request focus on the name field by default
        txtName.requestFocus();
        
        // Disable save button if name is empty
        Button saveButton = (Button) getDialogPane().lookupButton(saveButtonType);
        saveButton.setDisable(true);
        
        // Add validation listeners
        txtName.textProperty().addListener((observable, oldValue, newValue) -> {
            boolean disableButton = newValue.trim().isEmpty();
            saveButton.setDisable(disableButton);
            if (disableButton) {
                errorLabel.setText("Nama tidak boleh kosong");
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
            }
        });
        
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !newValue.matches("\\d*")) {
                errorLabel.setText("Nomor telepon hanya boleh berisi angka");
                errorLabel.setVisible(true);
                saveButton.setDisable(true);
            } else if (!txtName.getText().trim().isEmpty()) {
                errorLabel.setVisible(false);
                saveButton.setDisable(false);
            }
        });

        // Convert the result to a user object when the save button is clicked
        setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                Users result = user != null ? user : new Users();
                result.setName(txtName.getText().trim());
                result.setAddress(txtAddress.getText().trim());
                result.setPhone(txtPhone.getText().trim());
                return result;
            }
            return null;
        });
    }
}