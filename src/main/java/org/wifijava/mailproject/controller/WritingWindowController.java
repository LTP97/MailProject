package org.wifijava.mailproject.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.wifijava.mailproject.data.MessageContent;
import org.wifijava.mailproject.exceptions.MailSendingException;
import org.wifijava.mailproject.logic.HelperService;


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
        String subject = subjectField.getText();
        String body = bodyArea.getText();

        String[] toRecipients = toFieldContainer.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .flatMap(node -> ((HBox) node).getChildren().stream())
                .filter(innerNode -> innerNode instanceof TextField)
                .map(innerNode -> ((TextField) innerNode).getText())
                .filter(email -> email != null && !email.trim().isEmpty())
                .toArray(String[]::new);

        String[] ccRecipients = ccFieldContainer.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .flatMap(node -> ((HBox) node).getChildren().stream())
                .filter(innerNode -> innerNode instanceof TextField)
                .map(innerNode -> ((TextField) innerNode).getText())
                .filter(email -> email != null && !email.trim().isEmpty())
                .toArray(String[]::new);

        String[] bccRecipients = bccFieldContainer.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .flatMap(node -> ((HBox) node).getChildren().stream())
                .filter(innerNode -> innerNode instanceof TextField)
                .map(innerNode -> ((TextField) innerNode).getText())
                .filter(email -> email != null && !email.trim().isEmpty())
                .toArray(String[]::new);

        MessageContent messageContent = new MessageContent(
                new String[0], subject, body, toRecipients, ccRecipients, bccRecipients
        );


        try {
            HelperService.sendMailOut(messageContent);
        } catch (MailSendingException e) {
            showErrorDialog(e.getMessage());
        }


        // Debugging output
        System.out.println("Sending email to: " + String.join(", ", toRecipients));
        System.out.println("CC: " + String.join(", ", ccRecipients));
        System.out.println("BCC: " + String.join(", ", bccRecipients));
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
    }

    // Method to handle adding a new "To" recipient
    @FXML
    private void handleAddRecipient() {
        HBox newRecipientBox = createRecipientField(toFieldContainer);
        toFieldContainer.getChildren().add(newRecipientBox);
    }

    // Method to handle adding a new "CC" recipient
    @FXML
    private void handleAddCC() {
        HBox newCCBox = createRecipientField(ccFieldContainer);
        ccFieldContainer.getChildren().add(newCCBox);
    }

    // Method to handle adding a new "BCC" recipient
    @FXML
    private void handleAddBCC() {
        HBox newBCCBox = createRecipientField(bccFieldContainer);
        bccFieldContainer.getChildren().add(newBCCBox);
    }

    // Helper method to create a new HBox with a TextField and a remove button
    private HBox createRecipientField(VBox container) {
        HBox recipientBox = new HBox(5); // 5 is the spacing between elements
        TextField recipientField = new TextField();
        recipientField.setPromptText("Enter email");

        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> container.getChildren().remove(recipientBox));

        recipientBox.getChildren().addAll(recipientField, removeButton);
        return recipientBox;
    }

    private void showErrorDialog(String message) {
        System.out.println("Error Dialog Triggered: " + message);
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("An error occured");
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}


//    @FXML
//    private TextField toField;
//    @FXML
//    private TextField subjectField;
//    @FXML
//    private TextArea bodyArea;
//
//
//
//    @FXML
//    private void handleSendButtonPress() {
//        String recipient = toField.getText();
//        String subject = subjectField.getText();
//        String body = bodyArea.getText();
//
//        MessageContent messageContent = new MessageContent(new String[0],subject,body,recipient, Message.RecipientType.TO);
//        try {
//            MailIO.sendMail(AppData.getInstance().getCurrentAccount(), messageContent);
//        } catch (MessagingException e) {
//            bodyArea.setText("COULD NOT SEND MESSAGE - try again");
//        }
//
//
//        System.out.println("Sending email to: " + recipient);
//        System.out.println("Subject: " + subject);
//        System.out.println("Body: " + body);
//    }
//}