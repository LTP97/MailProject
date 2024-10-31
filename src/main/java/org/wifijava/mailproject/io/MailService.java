package org.wifijava.mailproject.io;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.wifijava.mailproject.data.MessageContent;
import org.wifijava.mailproject.data.User;
import org.wifijava.mailproject.io.provider.MailProvider;

import java.util.Properties;


public class MailService {
    private MailProvider mailProvider;

    public MailService(MailProvider mailProvider) {
        this.mailProvider = mailProvider;
    }

    public void sendMail(User user, MessageContent messageContent) throws MessagingException {
        Properties properties = mailProvider.getSmtpProperties();
        Message message = prepMessage(user,messageContent,properties);
        Transport.send(message);
        System.out.println("Message sent");
    }

    private Message prepMessage(User user,MessageContent messageContent,Properties properties) throws MessagingException {
        String emailAdress = user.getEmailAdress();
        String password = user.getPassword();

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAdress,password);
            }
        });
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(emailAdress));
        message.setRecipient(messageContent.type(), new InternetAddress(messageContent.recipient()));
        for(int i = 0; i < messageContent.headers().length ; i+=2 ){
            String header1 = messageContent.headers()[i];
            String header2 = messageContent.headers()[i + 1];
            message.setHeader(header1, header2);
        }
        message.setSubject(messageContent.subject());
        message.setText(messageContent.body());
        return message;
    }
}
