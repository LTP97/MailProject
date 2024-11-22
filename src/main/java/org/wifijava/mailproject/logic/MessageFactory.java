package org.wifijava.mailproject.logic;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.data.MessageContent;

import java.util.Arrays;
import java.util.Properties;

public class MessageFactory {

    public static Message buildMessage(MessageContent messageContent,MailAccount currentAccount) throws MessagingException {
        try {

            Properties properties = currentAccount.getMailProvider().getSmtpProperties();
            String mailAddress = currentAccount.getMailAddress();
            String password = currentAccount.getPassword();

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailAddress, password);
                }
            });
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(mailAddress));

            addRecipientsByType(Message.RecipientType.TO,message,messageContent);
            addRecipientsByType(Message.RecipientType.CC,message,messageContent);
            addRecipientsByType(Message.RecipientType.BCC,message,messageContent);

            //todo: Header verarbeitung entfernen
            for (int i = 0; i < messageContent.headers().length; i += 2) {
                String header1 = messageContent.headers()[i];
                String header2 = messageContent.headers()[i + 1];
                message.setHeader(header1, header2);
            }

            message.setSubject(messageContent.subject());
            message.setText(messageContent.body());

            return message;
        } catch (MessagingException e) {
            throw new MessagingException(Constants.BUILD_MESSAGE_ERROR, e);
        }
    }

    private static void addRecipientsByType(Message.RecipientType type, Message message, MessageContent messageContent) throws MessagingException {
        String[] recipients = messageContent.getRecipients(type.toString());

        message.addRecipients(type,
                Arrays.stream(recipients)
                        .map(address -> {
                            try {
                                return new InternetAddress(address);
                            } catch (AddressException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .toArray(InternetAddress[]::new)
        );
    }
}
