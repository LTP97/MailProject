package org.wifijava.mailproject.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.wifijava.mailproject.io.provider.MailProvider;

@Data
@AllArgsConstructor
public class MailAccount {
    private final String mailAddress;
    private final MailProvider mailProvider;
    private String password;
}
