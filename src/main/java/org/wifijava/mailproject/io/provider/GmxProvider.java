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
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", "imap.gmx.com");
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");
        return properties;
    }

    @Override
    public String getStandardInboxName() {
        return "INBOX";
    }

    @Override
    public String getStandardOutboxName() {
        return "Gesendet";
    }

    @Override
    public String getProviderKeyword() {
        return "gmx";
    }

    public String getSpamFolderName(){
        return "Spamverdacht";
    }
}
