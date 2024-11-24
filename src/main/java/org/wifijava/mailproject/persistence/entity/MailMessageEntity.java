package org.wifijava.mailproject.persistence.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "mails")
@Data
@NoArgsConstructor
public class MailMessageEntity {

    @Id
    private long id;

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
