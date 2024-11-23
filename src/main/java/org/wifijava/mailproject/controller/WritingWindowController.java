package org.wifijava.mailproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.data.MessageContent;
import org.wifijava.mailproject.exceptions.MailSendingException;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.MailService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WritingWindowController {
    private final List<String> attachments = new ArrayList<>();
    @FXML
    private VBox toFieldContainer;
    @FXML
    private VBox ccFieldContainer;
    @FXML
    private VBox bccFieldContainer;
    @FXML
    private TextField subjectField;
    @FXML
    private TextArea bodyArea;
    @FXML
    private HBox attachmentsDisplay;

    @FXML
    private void handleAddAttachment() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files", "*.*"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Label attachmentLabel = new Label(selectedFile.getName());
            Button removeButton = new Button("X");
            styleButton(removeButton);
            HBox attachmentBox = new HBox(5, attachmentLabel, removeButton);
            attachmentsDisplay.getChildren().add(attachmentBox);
            attachments.add(selectedFile.toString());
            removeButton.setOnAction(e -> {
                attachments.remove(selectedFile.toString());
                attachmentsDisplay.getChildren().remove(attachmentBox);
            });
        }
    }


    @FXML
    private void handleSendButtonPress() {
        MailService mailService = new MailService();
        String subject = subjectField.getText();
        String body = bodyArea.getText();
        String[] toRecipients = getRecipients(toFieldContainer);
        String[] ccRecipients = getRecipients(ccFieldContainer);
        String[] bccRecipients = getRecipients(bccFieldContainer);
        String[] attachmentPaths = attachments.toArray(String[]::new);
        MessageContent messageContent = new MessageContent(
                new String[0], subject, body, toRecipients, ccRecipients, bccRecipients, attachmentPaths
        );

        try {
            MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
            mailService.buildAndSendMail(messageContent, currentAccount);
        } catch (MailSendingException e) {
            AlertService alertService = new AlertService();
            alertService.showErrorDialog(e.getMessage());
        }

        //todo remove debugging output
        System.out.println("Sending email to: " + String.join(", ", toRecipients));
        System.out.println("CC: " + String.join(", ", ccRecipients));
        System.out.println("BCC: " + String.join(", ", bccRecipients));
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }

    @FXML
    private void handleAddRecipient() {
        HBox newRecipientBox = createRecipientField(toFieldContainer);
        toFieldContainer.getChildren().add(newRecipientBox);
    }

    @FXML
    private void handleAddCC() {
        HBox newCCBox = createRecipientField(ccFieldContainer);
        ccFieldContainer.getChildren().add(newCCBox);
    }

    @FXML
    private void handleAddBCC() {
        HBox newBCCBox = createRecipientField(bccFieldContainer);
        bccFieldContainer.getChildren().add(newBCCBox);
    }

    private String[] getRecipients(VBox container) {
        return container.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .flatMap(node -> ((HBox) node).getChildren().stream())
                .filter(innerNode -> innerNode instanceof TextField)
                .map(innerNode -> ((TextField) innerNode).getText())
                .filter(email -> email != null && !email.trim().isEmpty())
                .toArray(String[]::new);
    }

    private void styleButton(Button button) {
        button.setStyle(Constants.BUTTON_STYLE_CSS);
    }


    private HBox createRecipientField(VBox container) {
        HBox recipientBox = new HBox(5);
        TextField recipientField = new TextField();
        recipientField.setPromptText("Enter email");

        Button removeButton = new Button("-");
        styleButton(removeButton);
        removeButton.setOnAction(e -> container.getChildren().remove(recipientBox));

        recipientBox.getChildren().addAll(recipientField, removeButton);
        return recipientBox;
    }
}


