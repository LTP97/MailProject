package org.wifijava.mailproject.data;

import jakarta.mail.Message;

public record MessageContent(String[] headers, String subject, String body, String recipient, Message.RecipientType type) {
}
