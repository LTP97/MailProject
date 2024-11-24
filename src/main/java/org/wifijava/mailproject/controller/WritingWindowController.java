package org.wifijava.mailproject.controller;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
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
import org.wifijava.mailproject.logic.MailContentUtil;
import org.wifijava.mailproject.logic.outgoing.MailOutService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WritingWindowController {
    private final List<String> attachments = new ArrayList<>();

    @FXML
    public TextField firstRecipientField;
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


    public void initData(String passedMessageType, Message passedMessage) {
        String fillString;
        if (passedMessageType.equals(Constants.MESSAGE_TYPE_ANSWER)) {
            firstRecipientField.setText(MailContentUtil.getFrom(passedMessage));
            fillString = "AW: ";
        }else{
            fillString = "FW: ";
        }
            String fillText;
            try {
                fillText = buildAnswerText(passedMessage);
                if (passedMessage.getSubject() != null)
                    subjectField.setText(fillString + passedMessage.getSubject());
                else subjectField.setText(fillString);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            bodyArea.setText(fillText);



    }

    private String buildAnswerText(Message message) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        String from = MailContentUtil.getFrom(message);
        sb.append("\n\n\n-----------------------------------------------\n");
        sb.append("Message sent at: " + message.getSentDate());
        sb.append("\nFrom:    " + from);
        sb.append("\n");
        if (!message.getSubject().isEmpty()) {
            sb.append("Subject: " + message.getSubject());
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("Content:\n");
        try {
            sb.append(MailContentUtil.extractTextFromMessage(message));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }


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
        MailOutService mailOutService = new MailOutService();
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
            mailOutService.buildAndSendMail(messageContent, currentAccount);
        } catch (MailSendingException e) {
            AlertService alertService = new AlertService();
            alertService.showErrorDialog(e.getMessage());
        }
        SceneSwitcher.switchToScene(Constants.MAIN_WINDOW);
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

    @FXML
    public void handleExitButtonPress() {
        SceneSwitcher.switchToScene(Constants.MAIN_WINDOW);
    }
}


