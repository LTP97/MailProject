package org.wifijava.mailproject.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "mailaccounts")
@Data
@NoArgsConstructor
public class MailAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String mailAddress;

    @Column(nullable = false)
    private String password;
}
