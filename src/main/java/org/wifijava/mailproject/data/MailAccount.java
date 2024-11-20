package org.wifijava.mailproject.data;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.wifijava.mailproject.io.provider.MailProvider;

@Getter
@AllArgsConstructor
public class MailAccount {
    private final String mailAddress;
    private final MailProvider mailProvider;

    @Setter
    private String password;
}
