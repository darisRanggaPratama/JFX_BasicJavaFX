package com.tama.basicjfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class VBoxLayout extends Application {
    @Override
    public void start(Stage PrimaryStage) throws Exception {
        PrimaryStage.setTitle("Registration Form-VBox");

        // Create VBox with 10 pixels of spacing between children
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-background-color: #f0f0f0;");

        // Add components to VBox
        Label titleLabel = new Label("Registration Form");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #333; -fx-padding: 10px;");

        TextField txtFirstName = new TextField();
        txtFirstName.setPromptText("First Name");
        txtFirstName.setMaxWidth(200);

        TextField txtLastName = new TextField();
        txtLastName.setPromptText("Last Name");
        txtLastName.setMaxWidth(200);

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");
        txtEmail.setMaxWidth(200);

        ComboBox<String> cbGender = new ComboBox<>();
        cbGender.getItems().addAll("Male", "Female");
        cbGender.setPromptText("Gender");
        cbGender.setMaxWidth(200);

        DatePicker dpBirthDate = new DatePicker();
        dpBirthDate.setPromptText("Birth Date");
        dpBirthDate.setMaxWidth(200);

        TextArea areaAddress = new TextArea();
        areaAddress.setPromptText("Address");
        areaAddress.setMaxWidth(200);
        areaAddress.setPrefRowCount(5);

        Button btnSubmit = new Button("Submit");
        btnSubmit.setStyle("-fx-font-size: 16px; -fx-background-color: #333; -fx-text-fill: #fff;");
        btnSubmit.setOnAction(event -> {
            System.out.println("\nData Validated");
            System.out.println("Name: " + txtFirstName.getText() + " " + txtLastName.getText());
            System.out.println("Email: " + txtEmail.getText());
            System.out.println("Gender: " + cbGender.getValue());
            System.out.println("Birth Date: " + dpBirthDate.getValue());
            System.out.println("Address: " + areaAddress.getText());
        });


        // Add All components to VBox
        vbox.getChildren().addAll(titleLabel, txtFirstName, txtLastName, txtEmail, cbGender, dpBirthDate, areaAddress, btnSubmit);

        // Create Scene and set it to the Stage
        Scene scene = new Scene(vbox, 800, 600);
        PrimaryStage.setScene(scene);
        PrimaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
