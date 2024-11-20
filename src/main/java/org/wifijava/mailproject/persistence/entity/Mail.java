package org.wifijava.mailproject.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "mails")
@Getter
@Setter
@AllArgsConstructor
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;


}
