package com.tama.customer;

import com.tama.customer.controller.DatabaseConnectionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Show database connection dialog first
        showDatabaseConnectionDialog();

        // Load main view
        FXMLLoader fxmlLoader = new FXMLLoader(CustomerApplication.class.getResource("view/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Customer Data Management");
        stage.setScene(scene);
        stage.show();
    }

    private void showDatabaseConnectionDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader(CustomerApplication.class.getResource("view/database-connection.fxml"));
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Database Connection");
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(loader.load());
        dialogStage.setScene(scene);

        DatabaseConnectionController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        // Show the dialog and wait for user response
        dialogStage.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
