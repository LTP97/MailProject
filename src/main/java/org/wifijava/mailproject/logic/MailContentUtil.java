package org.wifijava.mailproject.logic;

import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;
import org.jsoup.Jsoup;

import java.io.IOException;

public class MailContentUtil {



    public static long getIdFromMessage(Message message){
        String messageId;
        try {
            messageId = message.getHeader("Message-ID")[0];
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return messageId.hashCode();
    }

    public static String stripHtml(String html) {
        return Jsoup.parse(html).text();
    }

    public static String getFrom(Message message){
        try {
            return message.getFrom()[0].toString();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractTextFromMessage(Message message) throws MessagingException, IOException {
        Object content = message.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof MimeMultipart) {
            String result = extractTextFromMimeMultipart((MimeMultipart) content);
            return stripHtml(result);
        }
        return null;
    }

    private static String extractTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
        StringBuilder result = new StringBuilder();
        int partCount = mimeMultipart.getCount();

        for (int i = 0; i < partCount; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);

            if (bodyPart.isMimeType("text/plain")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.isMimeType("text/html")) {
                result.append(bodyPart.getContent());
            } else if (bodyPart.getContent() instanceof MimeMultipart) {
                result.append(extractTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }

        return result.toString();
    }

    public static boolean hasAttachment(Message message){
        try {
            if (message.isMimeType("multipart/*")) {
                Multipart multipart = (Multipart) message.getContent();
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())) {
                        return true;
                    }
                }
            }
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
