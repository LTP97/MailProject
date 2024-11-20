package org.wifijava.mailproject.io.provider;

import java.util.Properties;

public class GmxProvider implements MailProvider {


    @Override
    public Properties getSmtpProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "mail.gmx.com");
        properties.put("mail.smtp.port", "587");
        return properties;
    }

    @Override
    public Properties getImapProperties() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap"); // Use IMAP over SSL
        properties.put("mail.imap.host", "imap.gmx.com"); // GMX IMAP server
        properties.put("mail.imap.port", "993"); // GMX IMAP SSL port
        properties.put("mail.imap.ssl.enable", "true"); // Enable SSL
        return properties;
    }

    @Override
    public String getInboxName() {
        return "INBOX";
    }
}
