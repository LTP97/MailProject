package org.wifijava.mailproject.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.io.provider.GmxProvider;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.storage.MailAccountMapper;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;

import java.util.List;
import java.util.Optional;

public class SettingsWindowController {
    @FXML
    private ListView<String> accountsList;

    @FXML
    private TextArea signatureArea;

    @FXML
    private ListView<String> foldersList;

    private final MailAccountRepository accountRepository = new MailAccountRepository(AppData.getInstance().getSessionFactory());



    @FXML
    public void handleChangePassword() {
        String newPassword = openChangePasswordDialog();
        if(!newPassword.isEmpty()){
            MailAccountEntity entity =  accountRepository.getEntityByAddress(accountsList.getSelectionModel().getSelectedItem());
            accountRepository.updateEntityPassword(entity,newPassword);
        }
    }

    private static String openChangePasswordDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Enter your new password and confirm it.");
        Label passwordLabel = new Label("New Password:");
        PasswordField passwordField = new PasswordField();
        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(passwordLabel, 0, 0);
        gridPane.add(passwordField, 1, 0);
        gridPane.add(confirmPasswordLabel, 0, 1);
        gridPane.add(confirmPasswordField, 1, 1);
        dialog.getDialogPane().setContent(gridPane);
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);
        Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
        confirmButton.setDisable(true);
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!newVal.equals(confirmPasswordField.getText()) || newVal.isEmpty());
        });
        confirmPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            confirmButton.setDisable(!newVal.equals(passwordField.getText()) || newVal.isEmpty());
        });
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButtonType) {
                return passwordField.getText();
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }


    @FXML
    public void initialize() {
        loadAccounts();
        loadFolders();
    }

    @FXML
    private void handleAddAccount() {
        MailAccount newAccount = openNewAccountDialog();
        if(newAccount!=null){
            MailAccountMapper accountMapper = new MailAccountMapper();
            MailAccountEntity newEntity = accountMapper.getEntityFromInstance(newAccount);
            accountRepository.persistNewAccountEnitity(newEntity);
        }
        loadAccounts();
    }



    @FXML
    private void handleRemoveAccount() {
        String selected = accountsList.getSelectionModel().getSelectedItem();
        accountRepository.deleteAccountByEmail(selected);
        loadAccounts();
    }

    @FXML
    private void handleSetActiveAccount() {
        String selected = accountsList.getSelectionModel().getSelectedItem();
        MailAccountMapper accountMapper = new MailAccountMapper();
        if (selected != null) {
            MailAccountEntity entity = accountRepository.getEntityByAddress(selected);
            AppData.getInstance().setCurrentAccount(accountMapper.getInstanceFromEntity(entity));
        }
    }

    private void loadAccounts() {
        List<MailAccountEntity> entities = accountRepository.getAllAccounts();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (MailAccountEntity account : entities) {
            items.add(account.getMailAddress());
        }
        accountsList.setItems(items);
    }

    private void loadFolders() {
        ObservableList<String> folders = FXCollections.observableArrayList("Inbox", "Sent");
        foldersList.setItems(folders);
    }

    @FXML
    private void handleExitButtonPress() {
        MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
        boolean validAccountSelected = accountsList.getItems().contains(currentAccount.mailAddress());
        if(!validAccountSelected){
            new AlertService().showErrorDialog("Please select or create an account before exiting the settings window");
        }else {
            SceneSwitcher.switchToScene(Constants.MAIN_WINDOW);
        }
    }

    private MailAccount openNewAccountDialog(){
        Dialog<MailAccount> dialog = new Dialog<>();
        dialog.setTitle("Add New Account");
        dialog.setHeaderText("Enter your Email and Password");
        ButtonType addButton = new ButtonType("Add Account",ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton,ButtonType.CANCEL);
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPrefWidth(300);
        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(new Label("Confirm Password:"), 0, 2);
        grid.add(confirmPasswordField, 1, 2);
        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String email = emailField.getText();
                String password = passwordField.getText();
                String confirmPassword = confirmPasswordField.getText();
                if (!password.equals(confirmPassword)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Password Mismatch");
                    alert.setHeaderText("The passwords do not match. Account was not created");
                    alert.showAndWait();
                    return null;
                }
                return new MailAccount(email,new GmxProvider(),password);
            }
            return null;
        });
        Optional<MailAccount> result = dialog.showAndWait();
        return result.orElse(null);
    }

    //todo: improvements in the future:
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
