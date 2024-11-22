package org.wifijava.mailproject.controller;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AlertService {

    public void showErrorDialog(String message) {
        //todo: remove debugging output
        System.out.println("Error Dialog Triggered: " + message);
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
