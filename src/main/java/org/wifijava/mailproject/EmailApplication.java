package org.wifijava.mailproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.AppStartupService;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;
import org.wifijava.mailproject.persistence.repository.MailMessageRepository;

import java.io.IOException;

public class EmailApplication extends Application {

    public static void main(String[] args) {
        AppStartupService startupService = new AppStartupService();
        startupService.prepApplication();

        SessionFactory sessionFactory = AppData.getInstance().getSessionFactory();
        MailAccountRepository mailAccountRepository = new MailAccountRepository(sessionFactory);
        MailAccountEntity entity = mailAccountRepository.getFirstMailAccount();
        System.out.println(entity.getMailAdress());

        MailMessageRepository messageRepository = new MailMessageRepository(sessionFactory);
        System.out.println(messageRepository.getMessagesByOwner(entity).get(1));
//        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppStartupService startupService = new AppStartupService();
        startupService.prepApplication();



        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/wifijava/mailproject/fxml/MainWindow.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Write Mail");
        stage.setScene(scene);
        stage.show();
    }
}