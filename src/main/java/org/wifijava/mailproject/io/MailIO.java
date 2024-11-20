package org.wifijava.mailproject.io;

import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.data.MessageContent;
import org.wifijava.mailproject.logic.AppData;

import java.util.Arrays;
import java.util.Properties;


public class MailIO {

    public static void sendMail(Message message) throws MessagingException {
        try {
            Transport.send(message);
            System.out.println("Message(s) sent");
        }
        catch(MessagingException e){
            throw new MessagingException(Constants.SEND_MESSAGE_ERROR,e);
        }
    }

    public static Message[] recieveMail(MailAccount mailAccount) throws MessagingException {
        Properties properties = mailAccount.getMailProvider().getImapProperties();
        Session session = Session.getDefaultInstance(properties, null);
        Store store = session.getStore("imap");
        String host = properties.getProperty("mail.imap.host");

        store.connect(host, mailAccount.getMailAddress(), mailAccount.getPassword());

        Folder inbox = store.getFolder(mailAccount.getMailProvider().getInboxName());
        inbox.open(Folder.READ_ONLY);
        return inbox.getMessages();
    }
}
