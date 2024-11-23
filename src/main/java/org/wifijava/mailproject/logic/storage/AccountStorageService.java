package org.wifijava.mailproject.logic.storage;

import lombok.AllArgsConstructor;
import org.wifijava.mailproject.persistence.repository.MailAccountRepository;

@AllArgsConstructor
public class AccountStorageService {
    private final MailAccountRepository repository;
}
