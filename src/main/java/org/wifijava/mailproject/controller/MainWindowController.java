package org.wifijava.mailproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.wifijava.mailproject.logic.AppData;

public class MainWindowController {

    @FXML
    public Button attachmentButton;
    @FXML
    private Label accountDisplay = new Label(AppData.getInstance().getCurrentAccount().getMailAddress());
    @FXML
    private ListView<String> directoryList;
    @FXML
    private ListView<String> emailList;
    @FXML
    private TextArea emailContent;

    public void initialize(){

    }


    @FXML
    private void handleSettingsButton() {
        System.out.println("Settings button clicked");
    }

    @FXML
    private void handleEmailSelection(MouseEvent event) {
        String selectedEmail = emailList.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            emailContent.setText("Content of email: " + selectedEmail);
        }
    }

    @FXML
    public void handleAnswerButtonPress(){

    }

    @FXML
    public void handleForwardButtonPress() {

    }

    @FXML
    public void handleAttachmentButtonPress() {

    }

    @FXML
    public void handleRefreshButton() {

    }

    private void buildMailList(){

    }
}
