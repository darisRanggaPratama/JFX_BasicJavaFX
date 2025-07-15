package com.tama.datasharing.controller;

import com.tama.datasharing.App;
import com.tama.datasharing.entity.Citizen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private ObservableList<Citizen> citizens;

    @FXML
    private void closeAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    private void openFirstAction(ActionEvent actionEvent) throws IOException {
       FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(App.class.getResource("/com/tama/datasharing/first_view.fxml")));
       Parent root = loader.load();
       FirstController controller = loader.getController();
       controller.setMainController(this);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("First View JFX");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openSecondAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(App.class.getResource("/com/tama/datasharing" +
                "/second_view" +
                ".fxml")));
        Parent root = loader.load();
        SecondController controller = loader.getController();
        controller.setMainController(this);
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Second View JFX");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        citizens = FXCollections.observableArrayList();
    }

    public ObservableList<Citizen> getCitizens() {
        return citizens;
    }

    public void setCitizens(ObservableList<Citizen> citizens) {
        this.citizens = citizens;
    }
}
