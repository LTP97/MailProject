package org.wifijava.mailproject.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.data.MessageContent;
import org.wifijava.mailproject.exceptions.MailSendingException;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.MailService;


public class WritingWindowController {
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
    private void handleSendButtonPress() {
        MailService mailService = new MailService();
        String subject = subjectField.getText();
        String body = bodyArea.getText();
        String[] toRecipients = getRecipients(toFieldContainer);
        String[] ccRecipients = getRecipients(ccFieldContainer);
        String[] bccRecipients = getRecipients(bccFieldContainer);
        MessageContent messageContent = new MessageContent(
                new String[0], subject, body, toRecipients, ccRecipients, bccRecipients
        );

        try {
            MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
            mailService.buildAndSendMail(messageContent,currentAccount);
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

    private String[] getRecipients(VBox container){
        return container.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .flatMap(node -> ((HBox) node).getChildren().stream())
                .filter(innerNode -> innerNode instanceof TextField)
                .map(innerNode -> ((TextField) innerNode).getText())
                .filter(email -> email != null && !email.trim().isEmpty())
                .toArray(String[]::new);
    }

    private HBox createRecipientField(VBox container) {
        HBox recipientBox = new HBox(5);
        TextField recipientField = new TextField();
        recipientField.setPromptText("Enter email");

        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> container.getChildren().remove(recipientBox));

        recipientBox.getChildren().addAll(recipientField, removeButton);
        return recipientBox;
    }
}


