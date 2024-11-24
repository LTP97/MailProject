package org.wifijava.mailproject.logic.storage;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.MailContentUtil;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class MailMessageMapper {
    private final MailAccountRepository accountRepository;

    public MailMessageEntity getEntityFromInstance(Message message, String filePath, String label) throws MessagingException {
        MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
        MailAccountEntity currentAccountEntity = accountRepository.getEntityByAddress(currentAccount.mailAddress());
        MailMessageEntity result = new MailMessageEntity();
        String messageId = message.getHeader("Message-ID")[0];

        result.setBelongsTo(currentAccountEntity);
        result.setId(messageId.hashCode());
        result.setFilePath(filePath);
        result.setHasAttachment(MailContentUtil.hasAttachment(message));
        result.setLabel(label);

        return result;
    }

    public Message[] getInstancesFromEntitys(MailMessageEntity[] entities){
        List<Message> resultList = new ArrayList<>(entities.length);
        for(MailMessageEntity entity : entities){
            resultList.add(getInstanceFromEntity(entity));
        }
        return resultList.toArray(Message[]::new);
    }

    public Message getInstanceFromEntity(MailMessageEntity entity) {
        return loadMessageFromFile(entity.getFilePath());
    }

    private Message loadMessageFromFile(String filePath) {
        String actualFilePath = filePath + "/message.eml";
        Session session = Session.getDefaultInstance(System.getProperties());
        try (FileInputStream inputStream = new FileInputStream(actualFilePath)) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            return new MimeMessage(session, bufferedInputStream);
        } catch (IOException | MessagingException e) {
            throw new RuntimeException(e);
        }

    }
}
