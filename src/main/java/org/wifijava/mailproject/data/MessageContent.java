package org.wifijava.mailproject.data;

public record MessageContent(String[] headers, String subject, String body,
                             String[] toRecipients, String[] ccRecipients, String[] bccRecipients, String[] attachmentPaths) {

    public String[] getRecipients(String name) {
        if (name.equalsIgnoreCase("to")) return toRecipients;
        else if (name.equalsIgnoreCase("cc")) return ccRecipients;
        else if (name.equalsIgnoreCase("bcc")) return bccRecipients;
        else return new String[0];
    }
}
