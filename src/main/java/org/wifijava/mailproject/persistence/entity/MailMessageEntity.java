package org.wifijava.mailproject.persistence.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "mails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessageEntity {

    @Id
    private UUID uuid;

    @Column(name = "belongsTo", nullable = false)
    private long belongsTo;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "hasAttachment", nullable = false)
    private boolean hasAttachment;

}
