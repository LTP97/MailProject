package org.wifijava.mailproject.data;

public record MessageContent(String[] headers, String subject, String body,
                             String[] toRecipients, String[] ccRecipients, String[] bccRecipients) {
}
