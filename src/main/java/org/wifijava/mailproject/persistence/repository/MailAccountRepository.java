package org.wifijava.mailproject.persistence.repository;

import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;

import java.util.List;

@AllArgsConstructor
public class MailAccountRepository {
    private final SessionFactory sessionFactory;



    public MailAccountEntity getFirstMailAccount() {
        MailAccountEntity result;
        try(Session session = sessionFactory.openSession()) {
            Query<MailAccountEntity> query = session.createQuery("FROM MailAccountEntity", MailAccountEntity.class);
            query.setMaxResults(1);
            result = query.uniqueResult();
        }
        return result;
    }
}
