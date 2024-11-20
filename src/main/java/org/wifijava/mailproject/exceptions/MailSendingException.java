package org.wifijava.mailproject.exceptions;

public class MailSendingException extends Exception{
    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailSendingException(String message) {
        super(message);
    }
}
