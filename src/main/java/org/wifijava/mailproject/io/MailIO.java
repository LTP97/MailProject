package org.wifijava.mailproject.io;

import jakarta.mail.*;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;

import java.util.Arrays;
import java.util.Properties;


public class MailIO {

    public static void sendMail(Message message) throws MessagingException {
        try {
            Transport.send(message);
            System.out.println("Message(s) sent");
        } catch (MessagingException e) {
            throw new MessagingException(Constants.SEND_MESSAGE_ERROR, e);
        }
    }

    public static String[] fetchMailFolderNames(MailAccount mailAccount) throws MessagingException {
        Folder defaultFolder;
        try (Store store = connectToStore(mailAccount)) {
            defaultFolder = store.getDefaultFolder();
        }
        return Arrays.stream(defaultFolder.list())
                .map(Folder::getName)
                .toArray(String[]::new);
    }

    public static Message[] recieveMail(MailAccount mailAccount,String folderName) throws MessagingException {
        Folder folder;
        try (Store store = connectToStore(mailAccount)) {
            if(folderName == null || folderName.isEmpty()){
                folderName = mailAccount.getMailProvider().getStandardInboxName();
            }
            folder = store.getFolder(folderName);
        }
        folder.open(Folder.READ_ONLY);
        return folder.getMessages();
    }

    private static Store connectToStore(MailAccount mailAccount) throws MessagingException {
        Properties properties = mailAccount.getMailProvider().getImapProperties();
        Session session = Session.getDefaultInstance(properties,null);
        Store store = session.getStore(Constants.STORE_PROTOCOL);
        String host = properties.getProperty(Constants.HOST_TYPE);
        store.connect(host, mailAccount.getMailAddress(),mailAccount.getPassword());
        return store;
    }
}
