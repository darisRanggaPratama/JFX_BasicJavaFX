package com.tama.customer.view;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class ProgressDialog extends Dialog<Void> {
    private final ProgressBar progressBar;
    private final Label messageLabel;

    public ProgressDialog(String title) {
        initStyle(StageStyle.DECORATED);
        setTitle(title);
        setHeaderText(null);

        // Konfigurasi close behavior
        setResultConverter(dialogButton -> null);
        getDialogPane().getScene().getWindow().setOnCloseRequest(event -> hide());

        progressBar = new ProgressBar();
        progressBar.setMinWidth(300);
        progressBar.setProgress(-1); // indeterminate progress

        messageLabel = new Label("Please wait...");
        messageLabel.setStyle("-fx-font-size: 12px;");

        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);
        content.getChildren().addAll(progressBar, messageLabel);

        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(content);

        // Tambahkan tombol close
        dialogPane.getButtonTypes().add(ButtonType.CLOSE);
        Button closeButton = (Button) dialogPane.lookupButton(ButtonType.CLOSE);
        closeButton.setVisible(false);

        setResizable(false);
    }

    public ProgressDialog(Task<?> task) {
        this("Progress");
        progressBar.progressProperty().bind(task.progressProperty());
        messageLabel.textProperty().bind(task.messageProperty());
    }

    public void updateMessage(String text) {
        messageLabel.setText(text);
    }

    public void cleanup() {
        if (progressBar != null) progressBar.progressProperty().unbind();
        if (messageLabel != null) messageLabel.textProperty().unbind();
    }
}
