package org.wifijava.mailproject.logic;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MessageContent;
import org.wifijava.mailproject.exceptions.MailSendingException;
import org.wifijava.mailproject.io.MailIO;

public class HelperService {


    public static void sendMailOut(MessageContent messageContent) throws MailSendingException {
        try {
            Message message = MessageService.buildMessage(messageContent);
            MailIO.sendMail(message);

        } catch (MessagingException e) {
            String errorMessage = "Unknown exception occurred";

            if (e.getMessage().equals(Constants.BUILD_MESSAGE_ERROR)) {
                errorMessage = "Error occurred while building the message. Please check the email content and addresses.";
            }
            else if (e.getMessage().equals(Constants.SEND_MESSAGE_ERROR)) {
                errorMessage = "Error occurred while sending the message. Please check your network or email configuration.";
            }
            throw new MailSendingException(errorMessage, e);
        }
    }
}
