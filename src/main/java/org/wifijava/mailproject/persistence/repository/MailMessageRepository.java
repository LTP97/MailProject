package org.wifijava.mailproject.persistence.repository;

import jakarta.persistence.TypedQuery;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.wifijava.mailproject.persistence.entity.MailAccountEntity;
import org.wifijava.mailproject.persistence.entity.MailMessageEntity;

import java.util.List;

@AllArgsConstructor
public class MailMessageRepository {
    private final SessionFactory sessionFactory;

    public void persistNewMessage(MailMessageEntity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(entity);
            session.getTransaction().commit();
        }
    }

    public List<MailMessageEntity> getMessagesByOwner(MailAccountEntity account) {
        List<MailMessageEntity> result;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            String queryString = "from MailMessageEntity where belongsTo = :belongsToId";
            TypedQuery<MailMessageEntity> query = session.createQuery(queryString, MailMessageEntity.class);
            query.setParameter("belongsToId", account);
            result = query.getResultList();
            session.getTransaction().commit();
        }
        return result;
    }
}
