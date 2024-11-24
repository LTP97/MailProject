package org.wifijava.mailproject.controller;

import jakarta.mail.Message;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.wifijava.mailproject.logic.AppData;

import java.io.IOException;
import java.util.Objects;

public class SceneSwitcher {

    public static void switchToSceneWithInfo(Message message, String type, String window){
        Stage stage = AppData.getInstance().getStage();
        Parent root;
        WritingWindowController controller;
        FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(SceneSwitcher.class.getResource(window)));
        try {
            root = loader.load();
            controller = loader.getController();
            controller.initData(type,message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));

    }

    public static void switchToScene(String window){
        Stage stage = AppData.getInstance().getStage();
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(SceneSwitcher.class.getResource(window)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(new Scene(root));
    }

}
