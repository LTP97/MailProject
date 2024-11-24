package org.wifijava.mailproject.controller;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.MailContentUtil;
import org.wifijava.mailproject.logic.storage.MailAccountMapper;
import org.wifijava.mailproject.logic.storage.MailFileStorageService;
import org.wifijava.mailproject.logic.storage.MailMessageMapper;
import org.wifijava.mailproject.logic.storage.SyncService;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;
import org.wifijava.mailproject.persistence.repository.MailMessageRepository;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MainWindowController {

    @FXML
    public Button attachmentButton;
    @FXML
    private Label accountDisplay;
    @FXML
    private ListView<String> directoryList;
    @FXML
    private ListView<Message> emailList;
    @FXML
    private TextArea emailContent;

    private Message selectedEmail;
    private SyncService syncService;
    private final MailMessageRepository messageRepository = new MailMessageRepository(AppData.getInstance().getSessionFactory());
    private final MailAccountRepository accountRepository = new MailAccountRepository(AppData.getInstance().getSessionFactory());
    private final MailMessageMapper messageMapper = new MailMessageMapper(accountRepository);
    private final MailAccountMapper accountMapper = new MailAccountMapper();

    public void initialize() {
        accountDisplay.setText(AppData.getInstance().getCurrentAccount().mailAddress());
        MailFileStorageService fileStorageService = new MailFileStorageService();
        this.syncService = new SyncService(messageRepository, fileStorageService, messageMapper, accountMapper);
        buildDirectoryList();
        directoryList.getSelectionModel().select(0);
        buildMailList(directoryList.getSelectionModel().getSelectedItem());
        emailList.getSelectionModel().select(0);
        handleEmailSelection();
    }


    @FXML
    private void handleSettingsButton() {
        SceneSwitcher.switchToScene(Constants.SETTINGS_WINDOW);
    }

    @FXML
    private void handleEmailSelection() {
        selectedEmail = emailList.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            try {
                attachmentButton.setDisable(!MailContentUtil.hasAttachment(selectedEmail));
                emailContent.setText(buildStringFromMessage(selectedEmail));
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String buildStringFromMessage(Message message) throws MessagingException {
        StringBuilder sb = new StringBuilder();
        String from = MailContentUtil.getFrom(message);
        sb.append("From:    " + from);
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
    public void handleDirectorySelection() {
        String selectedFolder = directoryList.getSelectionModel().getSelectedItem();
        if (selectedFolder != null) {
            buildMailList(selectedFolder);
        }
    }


    @FXML
    public void handleAnswerButtonPress() {
        SceneSwitcher.switchToSceneWithInfo(selectedEmail, Constants.MESSAGE_TYPE_ANSWER, Constants.WRITING_WINDOW);
    }

    @FXML
    public void handleForwardButtonPress() {
        SceneSwitcher.switchToSceneWithInfo(selectedEmail, Constants.MESSAGE_TYPE_FORWARD, Constants.WRITING_WINDOW);
    }

    @FXML
    public void handleAttachmentButtonPress() {
        MailMessageEntity entity = messageRepository.getMessageById(MailContentUtil.getIdFromMessage(selectedEmail));
        File file = Path.of(entity.getFilePath() + "/attachments").toFile();
        if (file.exists()) {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    public void handleRefreshButton() {
        try {
            syncService.updateMailStorage();
            buildDirectoryList();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    private void buildDirectoryList() {
        ObservableList<String> folders = FXCollections.observableArrayList(Constants.INBOX_LABEL, Constants.OUTBOX_LABEL);
        directoryList.setItems(folders);
        directoryList.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> messageListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String text, boolean empty) {
                        super.updateItem(text, empty);
                        if (empty || text == null)
                            setText(null);
                        else {
                            setText(text);
                        }
                    }
                };
            }
        });
    }

    private void buildMailList(String label) {
        ObservableList<Message> items = FXCollections.observableArrayList(getAllMails(label));
        emailList.setItems(items);

        emailList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Message> call(ListView<Message> messageListView) {
                return new ListCell<>() {
                    private final Text text;

                    {
                        text = new Text();
                        text.wrappingWidthProperty().bind(emailList.widthProperty().subtract(20));
                        setGraphic(text);
                    }

                    @Override
                    protected void updateItem(Message message, boolean empty) {
                        super.updateItem(message, empty);
                        if (empty || message == null) {
                            text.setText(null);
                        } else {
                            try {
                                String from = message.getFrom()[0].toString();
                                String subject = message.getSubject();
                                text.setText(from + " - " + subject);
                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                };
            }
        });
    }


    private Message[] getAllMails(String label) {
        MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
        MailAccountEntity currentAccountEntity = accountRepository.getEntityByAddress(currentAccount.mailAddress());
        MailMessageEntity[] messagesByOwner = messageRepository.getMessagesByOwnerAndLabel(currentAccountEntity, label);
        return messageMapper.getInstancesFromEntitys(messagesByOwner);
    }

    @FXML
    public void handleWriteButtonPress() {
        SceneSwitcher.switchToScene(Constants.WRITING_WINDOW);
    }
}
