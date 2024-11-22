package org.wifijava.mailproject.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mailaccounts")
@Getter
@Setter
@NoArgsConstructor
public class MailAccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String mailAdress;

    @Column(nullable = false)
    private String password;
}
