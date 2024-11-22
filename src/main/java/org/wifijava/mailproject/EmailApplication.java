package org.wifijava.mailproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wifijava.mailproject.logic.AppStartupService;

import java.io.IOException;

public class EmailApplication extends Application {

    public static void main(String[] args) {
//        SessionFactory sessionFactory = AppData.getInstance().getSessionFactory();
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//
//            User user = new User();
//            user.setPassword("test123");
//            user.setMailAdress("test@mail.com");
//            session.persist(user);
//
//            session.getTransaction().commit();
//            System.out.println("Saved successfully");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppStartupService startupService = new AppStartupService();
        startupService.prepApplication();


        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/wifijava/mailproject/fxml/WritingWindow.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 800, 600);
        stage.setTitle("Writing Mail window test");
        stage.setScene(scene);
        stage.show();
    }
}