package org.wifijava.mailproject.logic;

import lombok.Getter;
import lombok.Setter;
import org.wifijava.mailproject.data.MailAccount;



@Setter
@Getter
public class AppData {
    private static AppData instance;

    private MailAccount currentAccount;


    private AppData(){};

    public static AppData getInstance() {
        if(instance == null) instance = new AppData();
        return instance;
    }

}
