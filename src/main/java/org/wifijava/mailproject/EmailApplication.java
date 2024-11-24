package org.wifijava.mailproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.SceneSwitcher;
import org.wifijava.mailproject.controller.SettingsWindowController;
import org.wifijava.mailproject.controller.StartupWindowController;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.AppStartupService;
import org.wifijava.mailproject.logic.storage.MailAccountMapper;
import org.wifijava.mailproject.logic.storage.MailFileStorageService;
import org.wifijava.mailproject.logic.storage.MailMessageMapper;
import org.wifijava.mailproject.logic.storage.SyncService;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;
import org.wifijava.mailproject.persistence.repository.MailMessageRepository;

import java.io.IOException;

public class EmailApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppStartupService startupService = getAppStartupService();
        startupService.prepApplication(stage);
        MailAccountRepository accountRepository = new MailAccountRepository(AppData.getInstance().getSessionFactory());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(Constants.STARTUP_WINDOW));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Simple Mail");
        stage.setScene(scene);
        stage.show();

        if(accountRepository.getFirstMailAccount() == null){
            SceneSwitcher.switchToScene(Constants.SETTINGS_WINDOW);
        }
        else{
            SceneSwitcher.switchToScene(Constants.MAIN_WINDOW);
        }

    }

    private static AppStartupService getAppStartupService() {
        MailMessageRepository messageRepository = new MailMessageRepository(AppData.getInstance().getSessionFactory());
        MailAccountRepository accountRepository = new MailAccountRepository(AppData.getInstance().getSessionFactory());
        MailMessageMapper messageMapper = new MailMessageMapper(accountRepository);
        MailAccountMapper accountMapper = new MailAccountMapper();
        return new AppStartupService(new SyncService(messageRepository,new MailFileStorageService(),messageMapper,accountMapper));
    }


}