package org.wifijava.mailproject.io.provider;

import java.util.Properties;

public interface MailProvider {
    Properties getSmtpProperties();

    Properties getImapProperties();

    String getStandardInboxName();

    String getStandardOutboxName();

    String getProviderKeyword();
}
