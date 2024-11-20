package org.wifijava.mailproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.constants.TestConstants;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.io.provider.GmxProvider;
import org.wifijava.mailproject.logic.AppData;

import java.io.IOException;

public class EmailApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/wifijava/mailproject/fxml/WritingWindow.fxml"));

        Parent root = fxmlLoader.load();

        AppData.getInstance().setCurrentAccount(new MailAccount(TestConstants.TESTMAIL,new GmxProvider(),TestConstants.TESTPASSWORD));

        Scene scene = new Scene(root,800,600);
        stage.setTitle("Writing Mail window test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);

////        MessageContent messageContent = new MessageContent(new String[0],"Test1","test1","testaccountlukas@gmx.at", Message.RecipientType.TO);
////        try {
////            mailService.sendMail(test1,messageContent);
////        } catch (MessagingException e) {
////            e.printStackTrace();
////        }
//        try {
//            Message[] messages = MailIO.recieveMail(test1);
//            for(Message message : messages){
//                System.out.println("Subject: " + message.getSubject());
//                System.out.println("From: " + message.getFrom()[0]);
//            }
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
    }
}