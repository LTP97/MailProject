package org.wifijava.mailproject.logic.storage;

import jakarta.activation.DataHandler;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.wifijava.mailproject.constants.Constants;
import org.wifijava.mailproject.controller.AlertService;
import org.wifijava.mailproject.data.MailAccount;
import org.wifijava.mailproject.logic.AppData;
import org.wifijava.mailproject.logic.MailContentUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;


public class MailFileStorageService {
    private final Path baseDirectory;

    public MailFileStorageService() {
        String userHome = System.getProperty("user.home");
        this.baseDirectory = Paths.get(userHome, "Documents", "MailAppData");
        createDirectoryIfNotPresent(baseDirectory);
    }

    public String storeMailInDirectory(Message message) {
        MailAccount currentAccount = AppData.getInstance().getCurrentAccount();
        Path messageFolder;
        Path userFolder = baseDirectory.resolve(currentAccount.mailAddress());
        String messageFolderName;
        createDirectoryIfNotPresent(userFolder);
        try {
            String time = LocalDateTime.now().toString().replace(':', '_').substring(0, 18);
            messageFolderName = time + "_" + message.getHeader("Message-ID")[0].hashCode();
            messageFolder = userFolder.resolve(messageFolderName);
            createDirectoryIfNotPresent(messageFolder);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        saveAttachmentsAsync(message, messageFolder);

        saveMessageToFile(message, messageFolder.toString());
        return messageFolder.toString();
    }

    private void saveAttachmentsAsync(Message message, Path messageFolder) {
        Platform.runLater(() -> {
            Task<Void> task = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    System.out.println("Thread started");
                    if (MailContentUtil.hasAttachment(message)) {
                        Path attachmentsFolder = messageFolder.resolve("attachments");
                        createDirectoryIfNotPresent(attachmentsFolder);
                        try {
                            saveAttachments(message, attachmentsFolder.toString());
                        } catch (MessagingException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            };
            new Thread(task).start();
            task.setOnSucceeded(event -> {
                System.out.println("Attachments saved successfully.");
            });
            task.setOnFailed(event -> {
                System.out.println("Failed to save attachments.");
            });
        });
    }


    public static void saveAttachments(Message message, String outputDir) throws MessagingException, IOException {
        Multipart multipart = (Multipart) message.getContent();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            String fileName = bodyPart.getFileName();
            if (fileName != null) {
                Path outputPath = Path.of(outputDir, fileName);
                saveFile(bodyPart.getDataHandler(), outputPath);
                System.out.println("Saved attachment: " + outputPath);
            }
        }
    }

    private static void saveFile(DataHandler dataHandler, Path outputPath) throws IOException {
        try (InputStream inputStream = dataHandler.getInputStream();
             OutputStream outputStream = Files.newOutputStream(outputPath)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private void saveMessageToFile(Message message, String filePath) {
        String fileName = filePath + "/message.eml";
        Path fileNamePath = Path.of(fileName);
        try (FileOutputStream fos = new FileOutputStream(fileNamePath.toFile()); BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            message.writeTo(bos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void createDirectoryIfNotPresent(Path path) {
        try {
            if (!Files.exists(path)) {
                Path file = Files.createDirectories(path);
            }
        } catch (IOException e) {
            System.out.println("error triggered");
            AlertService alertService = new AlertService();
            alertService.showErrorDialog(Constants.COULD_NOT_CREATE_APPDATA_DIRECTORY + " @(" + path + ")");
            System.exit(1337);
        }
    }

}
