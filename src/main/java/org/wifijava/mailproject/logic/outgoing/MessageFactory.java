package org.wifijava.mailproject.logic.outgoing;

import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.data.MessageContent;

import java.util.Arrays;
import java.util.Properties;

public class MessageFactory {

    public static Message buildMessage(MessageContent messageContent, MailAccount currentAccount) throws MessagingException {
        try {

            Properties properties = currentAccount.mailProvider().getSmtpProperties();
            String mailAddress = currentAccount.mailAddress();
            String password = currentAccount.password();

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(mailAddress, password);
                }
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailAddress));
            addRecipientsByType(Message.RecipientType.TO, message, messageContent);
            addRecipientsByType(Message.RecipientType.CC, message, messageContent);
            addRecipientsByType(Message.RecipientType.BCC, message, messageContent);
            message.setSubject(messageContent.subject());

            //todo: Header verarbeitung entfernen
            for (int i = 0; i < messageContent.headers().length; i += 2) {
                String header1 = messageContent.headers()[i];
                String header2 = messageContent.headers()[i + 1];
                message.setHeader(header1, header2);
            }

            MimeBodyPart messageBody = new MimeBodyPart();
            messageBody.setText(messageContent.body());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBody);

            for (String fileUrl : messageContent.attachmentPaths()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(fileUrl);
                attachmentPart.setDataHandler(new DataHandler(fds));
                attachmentPart.setFileName(fds.getName());
                multipart.addBodyPart(attachmentPart);
            }

            message.setContent(multipart);

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
