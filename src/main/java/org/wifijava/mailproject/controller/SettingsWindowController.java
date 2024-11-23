package org.wifijava.mailproject.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;

import java.util.Optional;

public class SettingsWindowController {
    @FXML
    private ListView<MailAccountEntity> accountsList;

    @FXML
    private TextArea signatureArea;

    @FXML
    private ListView<String> foldersList;

    @FXML
    private void handleExitSettings() {
        // Close the settings window and open main window
    }

    @FXML
    public void handleChangePassword() {
        // Create a custom dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your new password and confirm it.");

        // Create labels and text fields for password input
        Label passwordLabel = new Label("New Password:");
        PasswordField passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        // Layout for input fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(passwordLabel, 0, 0);
        gridPane.add(passwordField, 1, 0);
        gridPane.add(confirmPasswordLabel, 0, 1);
        gridPane.add(confirmPasswordField, 1, 1);

        // Add the grid pane to the dialog
        dialog.getDialogPane().setContent(gridPane);

        // Add buttons
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

        // Disable confirm button by default
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);

        // Enable confirm button only when inputs match
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!newVal.equals(confirmPasswordField.getText()) || newVal.isEmpty());
        });

        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!newVal.equals(passwordField.getText()) || newVal.isEmpty());
        });

        // Convert the result to the password when the confirm button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });

        // Show the dialog and get the result
        Optional<String> result = dialog.showAndWait();

        // Pass the new password to your update method (replace with your logic)
        result.ifPresent(this::updateEntityPassword);
    }

    private void updateEntityPassword(String newPassword) {
        // Logic to update the password in the database
        System.out.println("Password updated to: " + newPassword);
        // Replace this print statement with actual database update code
    }

    @FXML
    public void initialize() {
        accountsList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Perform actions on the selected account
                    }
                });
                // Initialize your lists, e.g., populate from database
                loadAccounts();
        loadFolders();
    }

    private void loadAccounts() {
        // Populate accountsList with MailAccountEntity from database
        // Example: accountsList.getItems().addAll(fetchMailAccounts());
    }

    private void loadFolders() {
        // Populate foldersList with existing folder names
        // Example: foldersList.getItems().addAll(fetchFolderNames());
    }

    @FXML
    private void handleAddAccount() {
        // Logic to add a mail account (e.g., show a dialog for account details)
    }

    @FXML
    private void handleRemoveAccount() {
        MailAccountEntity selected = accountsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Remove from database and refresh list
        }
    }

    @FXML
    private void handleSetActiveAccount() {
        MailAccountEntity selected = accountsList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Mark the selected account as active in the database
        }
    }

    @FXML
    private void handleSaveSignature() {
        String signature = signatureArea.getText();
        if (!signature.isEmpty()) {
            // Save signature to persistent storage
        }
    }

    @FXML
    private void handleAddFolder() {
        // Logic to add a folder (e.g., show a dialog for folder name)
    }

    @FXML
    private void handleRemoveFolder() {
        String selected = foldersList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Remove folder from internal structure and refresh list
        }
    }
}
