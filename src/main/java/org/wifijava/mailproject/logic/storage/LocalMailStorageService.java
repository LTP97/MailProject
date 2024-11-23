package org.wifijava.mailproject.logic.storage;

import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.AlertService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class LocalMailStorageService {
    private final Path baseDirectory;

    public LocalMailStorageService() {
        this.baseDirectory = Paths.get(System.getProperty("user.home"), "MailAppData");
        createDirectoryIfNotPresent(baseDirectory);
    }

    public void storeMailInDirectory() {

    }


    private void createDirectoryIfNotPresent(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            AlertService alertService = new AlertService();
            alertService.showErrorDialog(Constants.COULD_NOT_CREATE_APPDATA_DIRECTORY + " @(" + path + ")");
            System.exit(1337);
        }
    }

}
