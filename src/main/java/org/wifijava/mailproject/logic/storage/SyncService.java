package org.wifijava.mailproject.logic.storage;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import javafx.concurrent.Task;
import lombok.AllArgsConstructor;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.io.MailIO;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;
import org.wifijava.mailproject.persistence.repository.MailMessageRepository;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class SyncService {
    private final MailMessageRepository messageRepository;
    private final MailAccountRepository accountRepository = new MailAccountRepository(AppData.getInstance().getSessionFactory());
    private final MailFileStorageService fileStorageService;
    private final MailMessageMapper messageMapper;
    private final MailAccountMapper accountMapper;


    public void updateAccountStorage() {

    }


    public void updateMailStorage() throws MessagingException {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
                Message[] inbox;
                Message[] sent;
                try {
                    inbox = MailIO.recieveMail(currentAccount, currentAccount.mailProvider().getStandardInboxName());
                    sent = MailIO.recieveMail(currentAccount, currentAccount.mailProvider().getStandardOutboxName());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
                Message[] inboxFiltered = filterAlreadyPersisted(inbox);
                Message[] sentFiltered = filterAlreadyPersisted(sent);

                for (Message curMessage : inboxFiltered) {
                    String messagePath = fileStorageService.storeMailInDirectory(curMessage);
                    MailMessageEntity entity = null;
                    try {
                        entity = messageMapper.getEntityFromInstance(curMessage, messagePath, Constants.INBOX_LABEL);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    messageRepository.persistMessage(entity);
                }
                for (Message curMessage : sentFiltered) {
                    String messagePath = fileStorageService.storeMailInDirectory(curMessage);
                    MailMessageEntity entity = null;
                    try {
                        entity = messageMapper.getEntityFromInstance(curMessage, messagePath, Constants.OUTBOX_LABEL);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                    messageRepository.persistMessage(entity);
                }
                return null;
            }
        };
        new Thread(task).start();
        task.setOnSucceeded(event -> {
            System.out.println("Update completed successfully.");
        });
        task.setOnFailed(event -> {
            System.out.println("Update failed.");
        });
    }

    private Message[] filterAlreadyPersisted(Message[] unfilteredMessages) {
        Set<Long> persistedMessageIds = fetchPersistedMessageIds();
        return Arrays.stream(unfilteredMessages)
                .filter(message -> {
                    try {
                        Long currentId = (long) message.getHeader("Message-ID")[0].hashCode();
                        return !persistedMessageIds.contains(currentId);
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(Message[]::new);
    }

    private Set<Long> fetchPersistedMessageIds() {
        MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
        MailAccountEntity currentAccountEntity = accountRepository.getEntityByAddress(currentAccount.mailAddress());
        return Arrays.stream(messageRepository.getMessagesByOwner(currentAccountEntity))
                .map(entity -> entity.getId())
                .collect(Collectors.toSet());
    }

}
