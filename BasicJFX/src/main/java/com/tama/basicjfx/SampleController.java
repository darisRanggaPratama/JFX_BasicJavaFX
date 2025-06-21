package com.tama.basicjfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SampleController {
    @FXML
    private Label sayHello;
    @FXML
    private Label sayBottom;
    @FXML
    private Button clickMe;

    @FXML
    public void buttonClick(ActionEvent actionEvent) {
        try {
            sayHello.setText("Hello World!");
            sayBottom.setText("Clicked the button!");
            System.out.println("Button clicked!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
