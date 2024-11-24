package org.wifijava.mailproject.logic;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Data;
import org.wifijava.mailproject.controller.StartupWindowController;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.logic.storage.MailAccountMapper;
import org.wifijava.mailproject.logic.storage.SyncService;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;


@Data
public class AppStartupService {
    public AppStartupService(SyncService syncService) {
        this.syncService = syncService;
    }

    private final SyncService syncService;

    public void prepApplication(Stage stage) {
        AppData.getInstance().setStage(stage);
        MailAccountEntity defaultAccountEntity = syncService.getAccountRepository().getFirstMailAccount();
        if(defaultAccountEntity!=null){
            MailAccount instanceFromEntity = new MailAccountMapper().getInstanceFromEntity(defaultAccountEntity);
            AppData.getInstance().setCurrentAccount(instanceFromEntity);
            try {
                syncService.updateMailStorage();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }
}
