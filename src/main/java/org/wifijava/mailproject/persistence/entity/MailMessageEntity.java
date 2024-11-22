package org.wifijava.mailproject.persistence.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.UUID;


@Entity
@Table(name = "mails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailMessageEntity {

    @Id
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "belongs_to", nullable = false)
    private MailAccountEntity belongsTo;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "hasAttachment", nullable = false)
    private boolean hasAttachment;

    @Column(name = "label")
    private String label;

}
