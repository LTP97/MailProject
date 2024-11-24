package org.wifijava.mailproject.data;


import org.wifijava.mailproject.io.provider.MailProvider;


public record MailAccount(String mailAddress, MailProvider mailProvider, String password) {

}
