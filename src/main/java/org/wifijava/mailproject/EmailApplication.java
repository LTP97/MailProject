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

    }
}