package org.wifijava.mailproject.logic.storage;

import lombok.AllArgsConstructor;
import org.wifijava.mailproject.persistence.repository.MailMessageRepository;

@AllArgsConstructor
public class MessageStorageService {
    private final MailMessageRepository repository;
}
