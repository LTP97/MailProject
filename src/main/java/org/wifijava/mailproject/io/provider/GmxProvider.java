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
    public Properties getInProperties() {
        return null;
    }
}
