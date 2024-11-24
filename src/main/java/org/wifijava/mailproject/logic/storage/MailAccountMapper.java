package org.wifijava.mailproject.logic.storage;

import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.io.provider.GmxProvider;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;


public class MailAccountMapper {


    public MailAccountEntity getEntityFromInstance(MailAccount account){
        MailAccountEntity result = new MailAccountEntity();
        result.setPassword(account.password());
        result.setMailAddress(account.mailAddress());
        return result;
    }

    public MailAccount getInstanceFromEntity(MailAccountEntity entity){
        //todo: make more providers possible
        return new MailAccount(entity.getMailAddress(), new GmxProvider(), entity.getPassword());
    }

}
