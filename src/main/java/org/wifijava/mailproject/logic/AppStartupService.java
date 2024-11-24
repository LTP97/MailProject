package org.wifijava.mailproject.logic;

import jakarta.mail.MessagingException;
import javafx.application.Platform;
import javafx.stage.Stage;
import lombok.Data;
import org.wifijava.mailproject.controller.StartupWindowController;
import org.wifijava.mailproject.logic.storage.SyncService;


@Data
public class AppStartupService {
    public AppStartupService(SyncService syncService) {
        this.syncService = syncService;
    }

    private final SyncService syncService;

    public void prepApplication(Stage stage) {
        AppData.getInstance().setStage(stage);
        try {
            syncService.updateMailStorage();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
