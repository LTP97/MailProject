package org.wifijava.mailproject.io.provider;

import java.util.Properties;

public interface MailProvider {
    public Properties getSmtpProperties();
    public Properties getInProperties();
}
