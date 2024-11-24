package org.wifijava.mailproject.constants;


public interface Constants {
    String STORE_PROTOCOL = "imap";
    String HOST_TYPE = "mail.imap.host";

    String MAIN_WINDOW = "/org/wifijava/mailproject/fxml/MainWindow.fxml";
    String SETTINGS_WINDOW = "/org/wifijava/mailproject/fxml/SettingsWindow.fxml";
    String STARTUP_WINDOW = "/org/wifijava/mailproject/fxml/StartupWindow.fxml";
    String WRITING_WINDOW = "/org/wifijava/mailproject/fxml/WritingWindow.fxml";

    String MESSAGE_TYPE_FORWARD = "forward";
    String MESSAGE_TYPE_ANSWER = "answer";

    String INBOX_LABEL = "inbox";
    String OUTBOX_LABEL = "sent";

    String BUTTON_STYLE_CSS = "-fx-background-color: F19C79; -fx-border-color: black; -fx-border-width: 1px;";

    String BUILD_MESSAGE_ERROR = "Could not build message";
    String SEND_MESSAGE_ERROR = "Could not send message";
    String DB_PROPERTIES_ERROR = "Database credentials could not be read or are incorrect";
    String DB_CONNECTION_ERROR = "Database connection failed";
    String DB_CONFIG_FILE_ERROR = "Database config file is broken or database isn't running";
    String UNKNOWN_ERROR = "Unknown error occurred";
    String DB_NOT_CREATED_ERROR = "Database does not exist. Please check if database was created before running";
    String COULD_NOT_CREATE_APPDATA_DIRECTORY = "Could not create Appdata directory. Please check permissions of writing to user home directory";
}
