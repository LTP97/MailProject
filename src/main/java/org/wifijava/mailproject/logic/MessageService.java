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

public class MessageService {

    public static Message buildMessage(MessageContent messageContent) throws MessagingException {
        try {
            MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
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

            message.addRecipients(Message.RecipientType.TO,
                    Arrays.stream(messageContent.toRecipients())
                            .map(address -> {
                                try {
                                    return new InternetAddress(address);
                                } catch (AddressException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .toArray(InternetAddress[]::new)
            );

            message.addRecipients(Message.RecipientType.CC,
                    Arrays.stream(messageContent.ccRecipients())
                            .map(address -> {
                                try {
                                    return new InternetAddress(address);
                                } catch (AddressException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .toArray(InternetAddress[]::new)
            );

            message.addRecipients(Message.RecipientType.BCC,
                    Arrays.stream(messageContent.bccRecipients())
                            .map(address -> {
                                try {
                                    return new InternetAddress(address);
                                } catch (AddressException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .toArray(InternetAddress[]::new)
            );


            for (int i = 0; i < messageContent.headers().length; i += 2) {
                String header1 = messageContent.headers()[i];
                String header2 = messageContent.headers()[i + 1];
                message.setHeader(header1, header2);
            }
            message.setSubject(messageContent.subject());
            message.setText(messageContent.body());

            return message;
        }
        catch(MessagingException e){
            throw new MessagingException(Constants.BUILD_MESSAGE_ERROR,e);
        }
    }
}
