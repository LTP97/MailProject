package org.wifijava.mailproject.constants;


public interface Constants {
    String STORE_PROTOCOL = "imap";
    String HOST_TYPE = "mail.imap.host";

    String BUILD_MESSAGE_ERROR = "Could not build message";
    String SEND_MESSAGE_ERROR = "Could not send message";
    String DB_PROPERTIES_ERROR = "Database credentials could not be read or are incorrect";
    String DB_CONNECTION_ERROR = "Database connection failed";
    String DB_CONFIG_FILE_ERROR = "Database config file is broken or database isn't running";
    String UNKNOWN_ERROR = "Unknown error occurred";
    String DB_NOT_CREATED = "Database does not exist. Please check if database was created before running";
}
