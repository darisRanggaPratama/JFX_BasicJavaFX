package com.tama.basicjfxmultistage.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SecondController {
    @FXML
    public Label otherText;
    @FXML
    public Button First;

    public void insertToLabel(String text) {
        otherText.setText(text);
    }

    public void btnFirst(ActionEvent actionEvent) {
        otherText.setText("Hello Galaxy");
        ((Node) actionEvent.getSource()).getScene().getWindow().hide(); //hide stage (window
    }

    public String getFromLabel(){
        return otherText.getText();
    }
}
